/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package course_scheduler_beta;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    
    public CourseScheduler(DatabaseUtility db){
        this.duration = db.getDuration()[0];
        this.gap = db.getDuration()[1];
        this.db = db;
        //System.out.println(db.getCurrentSemester().equalsIgnoreCase("Fall 2016"));
        scheduleCourses();
    }
    
    public CourseScheduler(){
        this.duration = 60;
        this.gap = 15;
        db.setCurrentSemester("Fall 2016");
    }
    
    public void scheduleCourses(){  
        /* Get all courses, teachers and classrooms from database */
        List<Classroom> classrooms = db.getClassrooms(null, null);
        
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
                
                if(c.getClassroom() != null && !c.getClassroom().equalsIgnoreCase("null") && c.getClassroom().length() > 0){
                    Classroom r = new Classroom();
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
                                
                                done++;
                                rooms.get(i).useSlot(timeSlots.get(j));
                                t.useSlot(timeSlots.get(j));
                                c.setClassroom(rooms.get(i).getRoomNum());
                                c.setTime(timeSlots.get(j));
                                c.setBuilding(rooms.get(i).getBuildingName());
                            }    
                        }
                    }
                    if(done == 0){
                        c.setBuilding("null");
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
                if(c.getClassroom() != null && c.getClassroom().length() > 0){
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
                                done++;
                                rooms.get(i).useSlot(possibleTimes.get(j));
                                t.useSlot(possibleTimes.get(j));
                                c.setClassroom(rooms.get(i).getRoomNum());
                                c.setTime(possibleTimes.get(j));
                                c.setBuilding(rooms.get(i).getBuildingName());
                            }    
                        }
                    }
                    if(done == 0){
                        c.setBuilding("null");
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
        
        CourseScheduler cs = new CourseScheduler();
        cs.scheduleCourses();
    }
}
