/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package course_scheduler_beta;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.TextArea;

/**
 *
 * @author Victoria
 * 
 * This class handles the scheduling process
 */
public class CourseScheduler {
    List<String> timeSlots = new ArrayList();
    String[] courseMeetingTimes;
    int duration, gap;
    DatabaseUtility db = new DatabaseUtility();
    
    TextArea textArea;
    
    public CourseScheduler(DatabaseUtility db, TextArea errorTextArea){
        this.duration = db.getDuration()[0];
        this.gap = db.getDuration()[1];
        this.db = db;
        //System.out.println(db.getDuration()[0]);
        System.out.println("291 is = " + db.getSingleCourse(10099));
        textArea = errorTextArea;
        //scheduleCourses();        //since updateTable already calls this, and configureTable calls updateTable, this was unecesscary
    }
    
    public CourseScheduler(){
        this.duration = 60;
        this.gap = 15;
        db.setCurrentSemester("Fall 2016");
    }
    
    public void scheduleCourses(){  
        /* Get all courses, teachers and classrooms from database */
        List<Classroom> classrooms = db.getClassrooms(null, null);
        textArea.clear();  //this needs to be called to clear out error messages, but for some reason after init run the error messages aren't firing (that if inst triggering)
        
        List <Course> c = db.getCourses(null, null);   //clears all time data
       /* for(Course ele: c){                            //the null string problem is creeping into the
            ele.setDays(null);                         //    error message display stuff
            ele.setsTime(null);                        //doing this made the messages actually appear correctly 
            ele.seteTime(null);                        //   in the sense of the if statement in nonPrefs and teacherPrefs
            ele.setClassroom(null);                    //   actually fire and print the error messages 
            ele.setBuilding(null);                     //take note that courses without teachers do NOT display errors  
            db.alterCourse(ele);                       //   i guess that since a course without a teacher cannot be scheduled
        }*/                                              //I do not know if this clobbers anything important, so let me know if im destroying stuff that you need for your checking
        // Make the time slots for assigning classes
        makeTimeSlots(classrooms);
        // Try to assign teachers with time preferences first
        teacherPrefs(classrooms);        
        // Assign teachers without preferences next
        nonPrefs(classrooms); 
    }
    
    private void nonPrefs(List<Classroom> rooms) {
        List<Teacher> teachers = new ArrayList();
        teachers.addAll(db.getProfessors("TIME_PREF", "None"));
        //System.out.println("Teachers");
        //Parser.printList(teachers);
        
        for(Teacher t: teachers){
            List<Course> courses = db.getCourses("PROFESSOR", t.getName());
            //Parser.printList(courses);
            t.setTimeSlots(timeSlots.toArray(new String[0]));
            //for(int i=0;i<timeSlots.size();i++){
                //System.out.println(t.getEmptySlots()[i]);
            //}
            for(Course c: courses){
                //System.out.println(c);
                if(c.getClassroom() != null && !c.getClassroom().equalsIgnoreCase("null") && c.getClassroom().length() > 0){
                    Classroom r = new Classroom();
                    //System.out.println(r);
                    for(Classroom i: rooms){
                        if(i.getRoomNum().equalsIgnoreCase(c.getClassroom())){
                            r = i;
                        }   
                    }
                    
                    int done = 0;
                    for(int i = 0; done == 0 && i < timeSlots.size(); i++){
                        //System.out.println(c);
                        if(r.isSlotEmpty(timeSlots.get(i)) && t.isSlotEmpty(timeSlots.get(i))){
                            //System.out.println("choop");
                            done++;
                            r.useSlot(timeSlots.get(i));
                            t.useSlot(timeSlots.get(i));
                            c.setTime(timeSlots.get(i));
                            //if(r.getBuildingName()!=null)
                            c.setBuilding(r.getBuildingName());
                        }
                    }
                    //System.out.println(c.getClassroom_());
                }
                else {
                    int done = 0;
                    for(int i = 0; done == 0 && i < rooms.size(); i++){
                        for(int j = 0; done == 0 && j < timeSlots.size(); j++){
                            //System.out.println("room " + i + "slot " + j);
                            if(rooms.get(i).isSlotEmpty(timeSlots.get(j)) && t.isSlotEmpty(timeSlots.get(j))){
                                int match = 0;
                                if(rooms.get(i).getBuildingName().equalsIgnoreCase("Technology Hall") && c.getDepartment().equalsIgnoreCase("Computer Science")){
                                    match++;
                                }
                                else if(rooms.get(i).getBuildingName().equalsIgnoreCase("Engineering Building") && c.getDepartment().equalsIgnoreCase("ECE")){
                                    match++;
                                }
                                if(match > 0){
                                    done++;
                                    rooms.get(i).useSlot(timeSlots.get(j));
                                    t.useSlot(timeSlots.get(j));
                                    c.setClassroom(rooms.get(i).getRoomNum());
                                    c.setTime(timeSlots.get(j));
                                    c.setBuilding(rooms.get(i).getBuildingName());
                                }
                            }    
                        }
                    }
                    if(done == 0){
                        c.setBuilding("null");
                        textArea.appendText(c.getName() + " could not be scheduled. Not enough classrooms!\n");
                    }
                }
                db.alterCourse(c);
            }
            //System.out.println("Courses");
            //Parser.printList(courses);
        }
    }
    
    private void teacherPrefs(List<Classroom> rooms) {
        List<Teacher> teachers = new ArrayList();
        teachers.addAll(db.getProfessors("TIME_PREF","Evening"));
        teachers.addAll(db.getProfessors("TIME_PREF","Morning"));
        teachers.addAll(db.getProfessors("TIME_PREF","Afternoon"));
        teachers.addAll(db.getProfessors("TIME_PREF","MW"));
        teachers.addAll(db.getProfessors("TIME_PREF","TR"));
        //System.out.println("Teachers");
        //Parser.printList(teachers);
        
        for(Teacher t: teachers){
            List<Course> courses = db.getCourses("PROFESSOR", t.getName());
            List<String> possibleTimes = matchPref(t.getTimePreference());
            t.setTimeSlots(possibleTimes.toArray(new String[0]));
            //Parser.printList(possibleTimes);
            for(Course c:courses){
                                                                               //added below check to not fire on classroom actually holding string "null" 
                if(c.getClassroom() != null && c.getClassroom().length() > 0 && !c.getClassroom().equals("null")){
                    Classroom r = new Classroom();
                    for(Classroom i: rooms){
                        if(i.getRoomNum().equalsIgnoreCase(c.getClassroom())){
                            r = i;
                        }   
                    }
                    int done = 0;
                    for(int i = 0; done == 0 && i < possibleTimes.size(); i++){
                        if(r.isSlotEmpty(possibleTimes.get(i)) && t.isSlotEmpty(possibleTimes.get(i))){
                            done++;
                            r.useSlot(possibleTimes.get(i));
                            t.useSlot(possibleTimes.get(i));
                            c.setTime(possibleTimes.get(i));
                            c.setBuilding(r.getBuildingName());
                        }
                    }
                    
                }
                else {
                    int done = 0;
                    for(int i = 0; done == 0 && i < rooms.size(); i++){
                        for(int j = 0; done == 0 && j < possibleTimes.size(); j++){
                            if(rooms.get(i).isSlotEmpty(possibleTimes.get(j)) && t.isSlotEmpty(possibleTimes.get(j))){
                                int match = 0;
                                if(rooms.get(i).getBuildingName().equalsIgnoreCase("Technology Hall") && c.getDepartment().equalsIgnoreCase("Computer Science")){
                                    match++;
                                }
                                else if(rooms.get(i).getBuildingName().equalsIgnoreCase("Engineering Building") && c.getDepartment().equalsIgnoreCase("ECE")){
                                    match++;
                                }
                                if(match > 0){
                                    done++;
                                    rooms.get(i).useSlot(possibleTimes.get(j));
                                    t.useSlot(possibleTimes.get(j));
                                    c.setClassroom(rooms.get(i).getRoomNum());
                                    c.setTime(possibleTimes.get(j));
                                    c.setBuilding(rooms.get(i).getBuildingName());
                                }
                            }    
                        }
                    }
                    if(done == 0){
                        c.setBuilding("null");
                        textArea.appendText(c.getName() + " could not be scheduled. Teacher time preference conflict!\n");
                    }
                }
                db.alterCourse(c);
            } 
            //System.out.println("Courses");
            //Parser.printList(courses);
        }
        
        //Parser.printList(t);
    }
    
    private List matchPref(String time){
        List<String> s = new ArrayList();
        //System.out.println("t = " + timeSlots[0]);
        for(String i:timeSlots){
            String[] tmp = i.split(":| ");
            
            if(tmp[6].equalsIgnoreCase("pm") && time.equalsIgnoreCase("Evening") && Integer.parseInt(tmp[4])>6){
                s.add(i);
            }
            else if(tmp[6].equalsIgnoreCase("pm") && time.equalsIgnoreCase("Afternoon") && Integer.parseInt(tmp[4])<6){
                s.add(i);
            }
            else if(tmp[6].equalsIgnoreCase("am") && time.equalsIgnoreCase("Morning") && Integer.parseInt(tmp[4])<12){
                s.add(i);
            }
            else if(tmp[7].equalsIgnoreCase("MW") && time.equalsIgnoreCase("MW")){
                s.add(i);
            }
            else if(tmp[7].equalsIgnoreCase("TR") && time.equalsIgnoreCase("TR")){
                s.add(i);
            }
            else{
                //System.out.println(tmp[4]);
            }
        }
        return s; 
    }
    
    // create time slots (military time) based on duration of classes (dur), and time between classes (gap)
    private void makeTimeSlots(List<Classroom> rooms){
        int startOfDay = 805;
        int totalClassTime = duration + gap;
        int totalDayTime = 720; // Because there are MW and TR classes
        int numberOfTimeSlots = totalDayTime/totalClassTime;
        //System.out.println("***" + duration);
        courseMeetingTimes = new String[2*numberOfTimeSlots];
 
        // Find time slots
        for(int i = 0; i < numberOfTimeSlots; i++){
            String amORpm1 = "am";
            String amORpm2 = "am";
            int slot1 = startOfDay + ((int)((totalClassTime * i)/60))*(100) + (totalClassTime * i)%60;;
            if(slot1%100 >= 60) slot1+=40;
            if(slot1 >= 1300) { amORpm1 = "pm"; amORpm2 = "pm"; slot1-=1200;}
            
            
            int slot2 = slot1 + ((int)((duration)/60))*(100) + ((duration)%60);
            if(slot2%100 >= 60) slot2+=40;
            if(slot2 >= 1300) {amORpm2 = " pm"; slot2-=1200; }
            
            // the top one is for monday/wednesday classes, bottom is for tuesday/thursday classes.
            courseMeetingTimes[i] = String.format("%d:%02d %s - %d:%02d %s MW",(int)(slot1/100),slot1%100, amORpm1, (int)(slot2/100),slot2%100, amORpm2);
            courseMeetingTimes[i + numberOfTimeSlots] = String.format("%d:%02d %s - %d:%02d %s TR",(int)(slot1/100),slot1%100, amORpm1, (int)(slot2/100),slot2%100, amORpm2);
        }
        
        /* Myk: Prints out the courseMeetingTimes array*/ 
        for(String ele:courseMeetingTimes){
            timeSlots.add(ele);
            //System.out.println(ele);
        }
        for(Classroom c: rooms){
            c.setTimeSlots(courseMeetingTimes);
        }
    }
    
    // Using main as a unit test for the scheduler
    public static void main(String[] argv){
        
        System.out.println("you should NOT see this");
        CourseScheduler cs = new CourseScheduler();
        cs.scheduleCourses();
    }
}
