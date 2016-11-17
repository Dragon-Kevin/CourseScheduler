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
    private String[] teachableCourses = new String[3];
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
        this.timePreference = timePreference.split(": ")[1];
    }
    
    public void test() {
        System.out.println("test");
    }
    
    @Override
    public String toString () {
        return "Name = " + getName() + "; Course 1 = " + getTeachableCourses()[0] + "; Course 2 = " + getTeachableCourses()[1] + "; Course 3 = " + getTeachableCourses()[2] + "; Preferred Time = " + getTimePreference();
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
