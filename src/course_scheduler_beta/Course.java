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
    private int crn;                                //unique identifier
    String department;                            //Professor Name
    private String courseNum;
    String name;                          //Actual Professor
    
    private int m_enroll;
    private int enroll;
    private int avail;
    private int waitList;
    private String days;
    private String sTime;
    private String eTime;
    String building;
    String classroom;
    String prof;
    private String semester;
    private Teacher prof_;
    String time;
    boolean assigned;
 
    //empty constructor made for testing DB
    public Course(){
        crn = -1;
        department = null;
        courseNum = null;
        name = null;
        m_enroll = -1;
        enroll = -1;
        avail = -1;
        waitList = -1;
        days = null;
        sTime = null;
        eTime = null;       
        building = null;
        classroom = null;        
        prof = null;
        assigned = false;
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
        return "semester = " + getSemester() + ";CRN = "+ getCrn() + "department = " + getDepartment() + "; Course Num = " + getCourseNum() + "; name = " + getName() + "; max enrollment = "
                + getM_enroll() + "; Enrolled = " + getEnroll() + "; wait list = " + getWaitList() + "; Available = " + getAvail() + "; Start = " + getSTime() + "; End = " + getETime() +
                "; building = " + getBuilding() + "; classroom = " + getClassroom() + "Professor =" + getProf();
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
     * @return the courseNum
     */
    public String getCourseNum() {
        return courseNum;
    }

    /**
     * @param courseNum the courseNum to set
     */
    public void setCourseNum(String courseNum) {
        this.courseNum = courseNum;
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
     * @return the waitList
     */
    public int getWaitList() {
        return waitList;
    }

    /**
     * @param waitList the waitList to set
     */
    public void setWaitList(int waitList) {
        this.waitList = waitList;
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
    public String getSTime() {
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
    public String getETime() {
        return eTime;
    }

    /**
     * @param eTime the eTime to set
     */
    public void seteTime(String eTime) {
        this.eTime = eTime;
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
     * @return the prof_
     */
    public Teacher getProf_() {
        return prof_;
    }

    /**
     * @param prof_ the prof_ to set
     */
    public void setProf_(Teacher prof_) {
        this.prof_ = prof_;
    }
}
