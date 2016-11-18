/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package course_scheduler_beta;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 *
 * @author Myk
 * @author Victoria
 * 
 * This class handles the scheduling process
 */
public class CourseScheduler {
    List timeSlots;
    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
    /*try{
        Date startOfDay = timeFormat.parse("8:00:00");
        Date endOfDay = timeFormat.parse("20:00:00");
    }
    catch (Exception e){
        System.out.println("error doing time stuff");
    }*/
    
    public CourseScheduler(List<Classroom> rooms, int dur, int gap){
        
    }
    
    // create time slots (military time) based on duration of classes (dur), and time between classes (gap)
    private void makeTimeSlots(int dur, int gap){
        int totalClassTime = dur + gap;
        int totalDayTime = 720;
        int numberOfTimeSlots = totalDayTime/totalClassTime;
        for(int i = 0; i < numberOfTimeSlots; i++){
            //int slot = startOfDay + (totalClassTime * i);
            //timeSlots.add(slot);
        }
    }
}
