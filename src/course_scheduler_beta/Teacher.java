/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package course_scheduler_beta;
import java.util.*;
/**
 *
 * @author Myk
 */
public class Teacher {
    private String name;
    private List<Course> courses = new ArrayList();
    String[] teachableCourses = new String[3];
    private String[] timeSlots;
    private boolean[] emptySlots;
    private String timePreference;
    private String Anum;
    
    public Teacher(){
        this.Anum = "";
        this.name = "";
        this.timePreference = "";
    }
    
    public Teacher(String Anum, String name, String course1, String course2, String course3, String timePreference) {
        this.Anum = Anum;
        this.name = name;
        this.teachableCourses[0] = course1;
        this.teachableCourses[1] = course2;
        this.teachableCourses[2] = course3;
        setTimePreference(timePreference.split(": ")[1]);
        shortenTimePref();
    }
    
    public void shortenTimePref(){
        if(timePreference.equalsIgnoreCase("Morning classes only"))
                this.timePreference = "Morning";
            else if(timePreference.equalsIgnoreCase("Evening classes only"))
                this.timePreference = "Evening";
            else if(timePreference.equalsIgnoreCase("Afternoon classes only"))
                this.timePreference = "Afternoon";
            else if(timePreference.equalsIgnoreCase("All Mon-Wed classes"))
                this.timePreference = "MW";
            else if(timePreference.equalsIgnoreCase("All Tue-Thu classes"))
                this.timePreference = "TR";
    }
    
    public void test() {
        System.out.println("test");
    }
    
    public boolean isSlotEmpty(String slot){
        for(int i = 0; i < timeSlots.length; i++){
            if(slot.equalsIgnoreCase(timeSlots[i])){
                //System.out.println("empty slot " + slot);
                //System.out.println(emptySlots[i]);
                return emptySlots[i];
            }
        }
        //System.out.println("nop");
        return false;
    }
    
    public void useSlot(String slot){
        for(int i=0;i<timeSlots.length; i++){
            if(timeSlots[i].equalsIgnoreCase(slot))
                emptySlots[i] = false;
        }
        
    }
    
    public void setTimeSlots(String[] timeSlots) {
        this.timeSlots = timeSlots;
        emptySlots = new boolean[timeSlots.length];
        for(int i = 0; i<timeSlots.length; i++){
            emptySlots[i] = true;
            //System.out.println(b);
        }
        //System.out.println(emptySlots[1]);
    }  
    
    public boolean[] getEmptySlots(){
        return emptySlots;
    }
    
    @Override
    public String toString () {
        return "A# = " + Anum + "Name = " + getName() + "; Course 1 = " + getTeachableCourses()[0] + "; Course 2 = " + getTeachableCourses()[1] + "; Course 3 = " + getTeachableCourses()[2] + "; Preferred Time = " + getTimePreference();
    }
    
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the teachableCourses
     */
    public String[] getTeachableCourses() {
        return teachableCourses;
    }

    /**
     * @param teachableCourses the teachableCourses to set
     */
    public void setTeachableCourses(String[] teachableCourses) {
        this.teachableCourses = teachableCourses;
    }

    /**
     * @return the timePreference
     */
    public String getTimePreference() {
        return timePreference;
    }

    /**
     * @param timePreference the timePreference to set
     */
    public void setTimePreference(String timePreference) {
        this.timePreference = timePreference;
    }

    /**
     * @return the Anum
     */
    public String getAnum() {
        return Anum;
    }

    /**
     * @param Anum the Anum to set
     */
    public void setAnum(String Anum) {
        this.Anum = Anum;
    }
    
    public void addCourse(Course course){
        courses.add(course);
    }
    
    public List getCourses(){
        return courses;
    }
}
