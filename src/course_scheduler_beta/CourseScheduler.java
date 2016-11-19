/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package course_scheduler_beta;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;

/**
 *
 * @author Victoria
 * 
 * This class handles the scheduling process
 */
public class CourseScheduler {
    List timeSlots;
    String[] courseMeetingTimes;
    int duration, gap;

    
    public CourseScheduler(int dur, int gap){
        this.duration = dur;
        this.gap = gap;
    }
    
    public CourseScheduler(){
        this.duration = 80;
        this.gap = 15;
    }
    
    public void scheduleCourses(){
        
        Parser p = new Parser(new File(""));      
        List preferences = p.getPreferences();
        
        DatabaseUtility db = new DatabaseUtility();
      
        /* Read from database */
        List<Course> courses = db.getCourses("", "");
        List<Teacher> teachers = db.getProfessors("", "");
        List<Classroom> classrooms = db.getClassrooms("", "");
        
        // Make the time slots for assigning classes
        makeTimeSlots();
                
        /* Handle the classroom preferences first */
        assignClassroomPreferences(courses, preferences, teachers, courseMeetingTimes, classrooms);
        
        /* Handle faculty preferences second */
        //assignFacultyPreferences();
        
        /* Handle remaining by course size / room size / course time */
        for (Course iCourse : courses) {
            
            if (!iCourse.assigned){

                /* Set the course professor */
                for (Teacher iTeacher : teachers){
                    for (String teacherCourse : iTeacher.teachableCourses){
                        if (iCourse.name.equalsIgnoreCase(teacherCourse)) {
                            iCourse.prof = iTeacher.name;
                        }                
                    }
                }

                /* Set the building for the course */
                if (iCourse.department.equalsIgnoreCase("CS")){
                    iCourse.building = "Technology Hall";
                }

                /* Set the room for the course */
                for (Classroom iRoom : classrooms){
                    if (iRoom.numberOfSeats >= iCourse.getEnrollment()){
                        for (int iTimeSlot=0; iTimeSlot<16; iTimeSlot++){                        
                            if (!iCourse.assigned && iRoom.timeSlot[iTimeSlot]){
                                iCourse.classroom = iRoom.name;
                                iCourse.time = courseMeetingTimes[iTimeSlot];
                                iRoom.timeSlot[iTimeSlot] = false;
                                iCourse.assigned = true;
                            }
                        }
                    }
                }
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
            System.out.println(iCourse.toString());
            
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
    
    // create time slots (military time) based on duration of classes (dur), and time between classes (gap)
    private void makeTimeSlots(){
        int startOfDay = 800;
        int totalClassTime = duration + gap;
        int totalDayTime = 720; // Because there are MW and TR classes
        int numberOfTimeSlots = totalDayTime/totalClassTime;
        courseMeetingTimes = new String[2*numberOfTimeSlots];
        
        
        // For MW classes
        for(int i = 0; i < numberOfTimeSlots; i++){
            int slot = startOfDay + (totalClassTime * i);
            courseMeetingTimes[i] = String.valueOf(slot) + " - " + String.valueOf(slot + totalClassTime) + " MW ";
        }
        
        // For TR classes
        for(int i = 0; i < numberOfTimeSlots; i++){
            int slot = startOfDay + (totalClassTime * i);
            courseMeetingTimes[i + numberOfTimeSlots] = String.valueOf(slot) + " - " + String.valueOf(slot + totalClassTime) + " TR ";
        }
    }
    
    /* Modified by Victoria Mitchell */
    public static void assignClassroomPreferences(List<Course> courses, List<String> prefs, List<Teacher> teachers, String[] courseMeetingTimes, 
            List<Classroom> classrooms) {
        for(int i = 0; i < prefs.size(); i++) {
            System.out.println(prefs.get(i));
            for(Course ele: courses) {
                if (prefs.get(i).equals(ele.name)) {
                    ele.building = prefs.get(++i);
                    ele.classroom = prefs.get(++i);
                    ele.assigned = true;
                    
                    /* Set the course professor */
                    for (Teacher iTeacher : teachers){
                        for (String teacherCourse : iTeacher.teachableCourses){
                            if (ele.name.equalsIgnoreCase(teacherCourse)) {
                                ele.prof = iTeacher.name;
                            }                
                        }
                    }
                                        
                    for (int iTimeSlot=0; iTimeSlot<courseMeetingTimes.length; iTimeSlot++){    
                            ele.time = courseMeetingTimes[iTimeSlot];
                            for (Classroom thisRoom : classrooms){
                                if (thisRoom.name.equals(ele.classroom)){
                                    thisRoom.timeSlot[iTimeSlot] = false;
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
