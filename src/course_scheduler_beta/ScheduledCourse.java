/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package course_scheduler_beta;

/**
 *
 * @author AJ
 */
public class ScheduledCourse {
    //Semester
    private String semester;
    //Name Data
    private int crn;
    private String department;
    private String course_num;
    private String course_name;
    //Enrollment Data
    private int m_enroll;
    private int enroll;
    private int avail;
    private int wait_list;
    //Time Data
    private String days;
    private String sTime;
    private String eTime;
    //Location Data
    private String building;
    private String classroom;
    //Professor
    private String prof;

    /**
     * @return the semester
     */
    public String getSemester() {
        return semester;
    }

    /**
     * @param semester the semester to set
     */
    public void setSemester(String semester) {
        this.semester = semester;
    }

    /**
     * @return the crn
     */
    public int getCrn() {
        return crn;
    }

    /**
     * @param crn the crn to set
     */
    public void setCrn(int crn) {
        this.crn = crn;
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
     * @return the course_num
     */
    public String getCourse_num() {
        return course_num;
    }

    /**
     * @param course_num the course_num to set
     */
    public void setCourse_num(String course_num) {
        this.course_num = course_num;
    }

    /**
     * @return the course_name
     */
    public String getCourse_name() {
        return course_name;
    }

    /**
     * @param course_name the course_name to set
     */
    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    /**
     * @return the m_enroll
     */
    public int getM_enroll() {
        return m_enroll;
    }

    /**
     * @param m_enroll the m_enroll to set
     */
    public void setM_enroll(int m_enroll) {
        this.m_enroll = m_enroll;
    }

    /**
     * @return the enroll
     */
    public int getEnroll() {
        return enroll;
    }

    /**
     * @param enroll the enroll to set
     */
    public void setEnroll(int enroll) {
        this.enroll = enroll;
    }

    /**
     * @return the avail
     */
    public int getAvail() {
        return avail;
    }

    /**
     * @param avail the avail to set
     */
    public void setAvail(int avail) {
        this.avail = avail;
    }

    /**
     * @return the wait_list
     */
    public int getWait_list() {
        return wait_list;
    }

    /**
     * @param wait_list the wait_list to set
     */
    public void setWait_list(int wait_list) {
        this.wait_list = wait_list;
    }

    /**
     * @return the days
     */
    public String getDays() {
        return days;
    }

    /**
     * @param days the days to set
     */
    public void setDays(String days) {
        this.days = days;
    }

    /**
     * @return the sTime
     */
    public String getsTime() {
        return sTime;
    }

    /**
     * @param sTime the sTime to set
     */
    public void setsTime(String sTime) {
        this.sTime = sTime;
    }

    /**
     * @return the eTime
     */
    public String geteTime() {
        return eTime;
    }

    /**
     * @param eTime the eTime to set
     */
    public void seteTime(String eTime) {
        this.eTime = eTime;
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
     * @return the prof
     */
    public String getProf() {
        return prof;
    }

    /**
     * @param prof the prof to set
     */
    public void setProf(String prof) {
        this.prof = prof;
    }
}
