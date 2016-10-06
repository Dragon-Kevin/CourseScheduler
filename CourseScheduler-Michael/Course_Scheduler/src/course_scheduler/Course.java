/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package course_scheduler;

/**
 *
 * @author Myk
 */
public class Course {
    private String name;
    private String time;
    private String department;
    private String building;
    private String classroom;
    private int enrollment;
    
    public Course(String name) {
        this.name = name;
        this.department = name.split(" ")[0];
    }
    
    public void test() {
        System.out.println("test");
    }
    
    @Override
    public String toString () {
        return "department = " + getDepartment() + "; time = " + getTime() + "; building = " + getBuilding() + "; classroom = " + getClassroom() + "; name = " + getName();
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
     * @return the time
     */
    public String getTime() {
        return time;
    }

    /**
     * @param time the time to set
     */
    public void setTime(String time) {
        this.time = time;
    }

    /**
     * @return the department
     */
    public String getDepartment() {
        return department;
    }

    /**
     * @param department the department to set
     */
    public void setDepartment(String department) {
        this.department = department;
    }

    /**
     * @return the building
     */
    public String getBuilding() {
        return building;
    }

    /**
     * @param building the building to set
     */
    public void setBuilding(String building) {
        this.building = building;
    }

    /**
     * @return the classroom
     */
    public String getClassroom() {
        return classroom;
    }

    /**
     * @param classroom the classroom to set
     */
    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }

    /**
     * @return the enrollment
     */
    public int getEnrollment() {
        return enrollment;
    }

    /**
     * @param enrollment the enrollment to set
     */
    public void setEnrollment(int enrollment) {
        this.enrollment = enrollment;
    }
}
