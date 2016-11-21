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

    
    public CourseScheduler(int dur, int gap){
        this.duration = dur;
        this.gap = gap;
    }
    
    public CourseScheduler(){
        this.duration = 60;
        this.gap = 15;
    }
    
    public void scheduleCourses(){
        
//        Parser p = new Parser(new File(""));      
//        List preferences = p.getPreferences();
        
        DatabaseUtility db = new DatabaseUtility();
      
        /* Get all courses, teachers and classrooms from database */
        List<Course> courses = db.getCourses(null, null);
        List<Teacher> teachers = db.getProfessors(null, null);
        List<Classroom> classrooms = db.getClassrooms(null, null);
        
        // Make the time slots for assigning classes
        makeTimeSlots(classrooms);
        teacherPrefs(db, classrooms);
        /* Handle the classroom preferences first */
//        assignClassroomPreferences(courses, preferences, teachers, courseMeetingTimes, classrooms);
        
        /* Handle faculty preferences second */
        //assignFacultyPreferences();
        
        /* Handle remaining by course size / room size / course time */
        for (Course iCourse : courses) {
            
            if (!iCourse.assigned){

                /* Set the course professor */
                /*
                for (Teacher iTeacher : teachers){
                    for (String teacherCourse : iTeacher.teachableCourses){
                        if (iCourse.name.equalsIgnoreCase(teacherCourse)) {
                            iCourse.setProf(iTeacher.name);
                        }                
                    }
                }*/

                /* Set the building for the course */
               // if (iCourse.department.equalsIgnoreCase("CS")){
               //     iCourse.building = "Technology Hall";
                //}

                /* Set the room for the course */
                /*for (Classroom iRoom : classrooms){
                    if (iRoom.numberOfSeats >= iCourse.getEnroll()){
                        for (int iTimeSlot=0; iTimeSlot<16; iTimeSlot++){                        
                            if (!iCourse.assigned && iRoom.timeSlot[iTimeSlot]){
                                iCourse.setClassroom(iRoom.name);
                                iCourse.time = courseMeetingTimes[iTimeSlot];
                                iRoom.timeSlot[iTimeSlot] = false;
                                iCourse.assigned = true;
                            }
                        }
                    }
                }*/
            }
        }
        
        
        /* TEST:  Display the results of the assignments */
        
        
        /* Write the assignments to a file */
        String fileName = "CourseAssignments.csv";
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(fileName));
        } catch (IOException ex) {
            Logger.getLogger(CourseScheduler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        for (Course iCourse : courses) {
            
            /* Write to screen for viewing */
            //System.out.println(iCourse.toString());
            
            try {
                writer.write(iCourse.toString() + "\n");
            }
            catch (IOException e){
                System.out.println(e);
            }
            
        }
        
        try{
           if (writer != null){
               writer.close( );
           }
        }
        catch ( IOException e){
            System.out.println(e);
        }
    }
    
    private void teacherPrefs(DatabaseUtility db, List<Classroom> rooms) {
        List<Teacher> t = new ArrayList();
        t.addAll(db.getProfessors("TIME_PREF","Evening"));
        t.addAll(db.getProfessors("TIME_PREF","Morning"));
        t.addAll(db.getProfessors("TIME_PREF","Afternoon"));
        t.addAll(db.getProfessors("TIME_PREF","MW"));
        t.addAll(db.getProfessors("TIME_PREF","TR"));
        
        Parser.printList(t);
        
        for(Teacher i: t){
            List<Course> c = db.getCourses("PROFESSOR", i.getName());
            //if(checkTime(i.getTimePreference()))
            List<String> possibleTimes = matchPref(i.getTimePreference());
            Parser.printList(possibleTimes);
            for(Course y:c){
                //if(y.getClassroom().length() > 0){
                    /*for(String p: possibleTimes){
                        if(y.getClassroom_().isSlotEmpty(p)){
                            System.out.println();
                        }
                    }*/
                    
                //}
               // else {
                    int done = 0;
                    //while(done == 0){
                        for(int j = 0; done == 0 && j < rooms.size(); j++){
                            //System.out.println(rooms.get(j));
                            for(int jj = 0; done == 0 && jj < possibleTimes.size(); jj++){
                                //System.out.println(possibleTimes.get(jj));
                                if(rooms.get(j).isSlotEmpty(possibleTimes.get(jj))){
                                   // System.out.println("got a match");
                                    done++;
                                    rooms.get(j).useSlot(possibleTimes.get(jj));
                                    y.setClassroom(rooms.get(j).getRoomNum());
                                    y.setTime(possibleTimes.get(jj));
                                }    
                            }
                        }
                        //done++;
                   // }
               // }
            } 
            Parser.printList(c);
        }
        
        //Parser.printList(t);
    }
    
    private List matchPref(String time){
        List<String> s = new ArrayList();
        System.out.println(time);
        for(String i:timeSlots){
            String[] tmp = i.split(":| ");
            //for(String x:tmp)
                //System.out.print(x + "; ");
            if(tmp[6].equalsIgnoreCase("am") && time.equalsIgnoreCase("Morning")){
                s.add(i);
            }
            else if(tmp[6].equalsIgnoreCase("pm") && time.equalsIgnoreCase("Afternoon") && Integer.parseInt(tmp[4])<6){
                s.add(i);
            }
            else if(tmp[6].equalsIgnoreCase("pm") && time.equalsIgnoreCase("Evening") && Integer.parseInt(tmp[4])>6){
                s.add(i);
            }
            else if(tmp[7].equalsIgnoreCase("MW") && time.equalsIgnoreCase("MW")){
                s.add(i);
            }
            else if(tmp[7].equalsIgnoreCase("TR") && time.equalsIgnoreCase("TR")){
                s.add(i);
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
            if(slot1 >= 1200) { amORpm1 = "pm"; amORpm2 = "pm"; slot1-=1200;}
            
            
            int slot2 = slot1 + ((int)((totalClassTime)/60))*(100) + ((totalClassTime)%60);
            if(slot2%100 >= 60) slot2+=40;
            if(slot2 >= 1200) {amORpm2 = " pm"; slot2-=1200; }
            
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
            //System.out.println("*********************");
            c.setTimeSlots(courseMeetingTimes);
        }
        System.out.println("Available Times");
        Parser.printList(timeSlots);
        //return rooms;
    }
    
    /* Modified by Victoria Mitchell */
    public static void assignClassroomPreferences(List<Course> courses, List<String> prefs, List<Teacher> teachers, String[] courseMeetingTimes, 
            List<Classroom> classrooms) {
        for(int i = 0; i < prefs.size(); i++) {
            //System.out.println(prefs.get(i));
            for(Course ele: courses) {
                if (prefs.get(i).equals(ele.name)) {
                    ele.setBuilding(prefs.get(++i));
                    ele.setClassroom(prefs.get(++i));
                    ele.assigned = true;
                    
                    /* Set the course professor */
                    /*
                    for (Teacher iTeacher : teachers){
                        for (String teacherCourse : iTeacher.teachableCourses){
                            if (ele.name.equalsIgnoreCase(teacherCourse)) {
                                ele.setProf(iTeacher.name);
                            }                
                        }
                    }*/
                                        
                    for (int iTimeSlot=0; iTimeSlot<courseMeetingTimes.length; iTimeSlot++){    
                            ele.time = courseMeetingTimes[iTimeSlot];
                            for (Classroom thisRoom : classrooms){
                                if (thisRoom.name.equals(ele.getClassroom())){
                                   // thisRoom.timeSlot[iTimeSlot] = false;
                                }
                            }                            
                            ele.assigned = true;
                        }
                    
                }
            }
        }       
    }
    
    // Using main as a unit test for the scheduler
    public static void main(String[] argv){
        
        CourseScheduler cs = new CourseScheduler();
        cs.scheduleCourses();
    }
}
