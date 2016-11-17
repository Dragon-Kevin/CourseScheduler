/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package course_scheduler_beta;

/**
 *
 * @author Myk
 */
public class Course {
    int crn;                                //unique identifier
    String department, courseNum, name;     //Name Data
    int m_enroll, enroll, avail, waitList;  //Enroll Data    
    String days, sTime, eTime;              //Time Data
    String building, classroom;             //Location Data
    String prof;                            //Professor Name
    Teacher prof_;                          //Actual Professor
    
    String time;
 
    //empty constructor made for testing DB
    public Course(){
        crn = -1;
        department = "";
        courseNum = "";
        name = "";
        m_enroll = -1;
        enroll = -1;
        avail = -1;
        waitList = -1;
        days = "";
        sTime = "";
        eTime = "";       
        building = "";
        classroom = "";        
        prof = "";
    }
    
    public Course(String name) {
        this.name = name;
        this.department = name.split(" ")[0];
    }
    
    public Course(int crn, String name, String building) {
        this.name = name;
        this.crn = crn;
        this.building = building;
    }
    
    public void test() {
        System.out.println("test");
    }
    
    @Override
    public String toString () {
        return "department = " + department + "; time = " + time + "; building = " + building + "; classroom = " + classroom + "; name = " + name;
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
        return enroll;
    }

    /**
     * @param enrollment the enrollment to set
     */
    public void setEnrollment(int enrollment) {
        this.enroll = enrollment;
    }
}
