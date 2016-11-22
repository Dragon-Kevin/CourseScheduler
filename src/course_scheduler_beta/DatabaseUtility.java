/**This is the database utility class. It works with the database to store and
 * retrieve data.
 * @author AJ
 */
package course_scheduler_beta;

import java.util.*;
import java.sql.*;

/**
 * There are four tables we use in this database. The courses, professors, and
 * classrooms tables each have a column to define semester. Each semester is 
 * a schedule.
 * 
 * COURSES          -Contains all courses, with all their relevant data. This is 
 *                  what we build the schedules with.
 * PROFESSORS       -Contains all professors, with what courses they are teaching
 * CLASSROOMS       -Contains all classrooms
 * USER_SCHEDULES   -the names of all stored schedules. This is basically every
 *                  unique entry in the semester columns in the other tables.
 * 
 * @author AJ
 */
public class DatabaseUtility {
    
    private String host;            //local host for db
    private String username;        //access un
    private String password;        //access pw
    private String currentSemester; //current semester is the current schedule being worked on
    private int[] duration;         //added so that our program has access to durations from main
    
    /**Constructor. Sets the private members to the the correct login info.
     * 
     */
    public DatabaseUtility(){

        host = "jdbc:derby://localhost:1527/SchoolData";    
        username = "user1";
        password = "123456";
        currentSemester = "";   //a new instance of Database Utility will need to
                                //have the currently selected semester set right after it is created
    }        
    
    /**
     * @return the currentSemester
     */
    public String getCurrentSemester() {
        return currentSemester;
    }

    /**Changes the currently selected semester to a new one.
     * @param currentSemester the currentSemester to set
     */
    public void setCurrentSemester(String currentSemester) {
        this.currentSemester = currentSemester;
    }

    /** Adds a new semester name to semester table
     * @param newSemester - the new semester to set as currently selected
     */
    public void addSemester(String newSemester)
    {
        try{
            Connection con = DriverManager.getConnection(host, username, password);
            
            System.out.println("HITTING "+newSemester);
            
            String sql = "insert into USER_SCHEDULES values(?)";
            PreparedStatement ps = con.prepareStatement(sql); 
            ps.setString(1, newSemester);
            ps.executeUpdate();
            
            con.close();
        }catch(SQLException err){
            System.out.println( "Error Storing Semester!");
            //err.printStackTrace();
        }
    }
    
    /** Returns a list of string of the names of all stored schedules. Use this 
     * to populate the list structure on the New/Load menu.
     * 
     * @return a list of strings of each semester stored in the database 
     */
    public List getSemesters()
    {
        List<String> semesters = new ArrayList();
        try{
            Connection con = DriverManager.getConnection(host, username, password);
            
            String sql = "select * from USER_SCHEDULES";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            
            while(rs.next()){
                String s = new String();
                s = rs.getString("NAME");
                semesters.add(s);
            }
            
            con.close();
        }catch(SQLException err){
            System.out.println( "Error Getting Semesters!");
            //err.printStackTrace();
        }
        return semesters;
    }
    
    /**Returns a list of Teacher objects constructed from the Database.
     * 
     * @param constraint - name of the column being restricted to
     * @param value - name of the value we are searching for in the constraint 
     *                column
     * @return - an ArrayList of Teacher objects constructed from the selected 
     *           entries in the database
     */
    public List getProfessors(String constraint, String value)
    {   
        List<Teacher> profs = new ArrayList();  //list to store professors from db in
        
        try{
            Connection con = DriverManager.getConnection(host, username, password);
            ResultSet rs;
            
            if(value == null && constraint != null){
                String sql = "Select * from PROFESSORS where SEMESTER = '"+ getCurrentSemester() +"' and "
                        + constraint +" is null";
                //System.out.println(sql);
                Statement st = con.createStatement();
                rs = st.executeQuery(sql);                   
            }
            else if(constraint != null){
                //select professors based on current semester and constraint 
                String sql = "Select * from PROFESSORS where SEMESTER = '"+ getCurrentSemester() +"' and "
                        + constraint +" = '"+ value +"'";
                Statement st = con.createStatement();
                rs = st.executeQuery(sql);   
            }
            else{
                //select professors based on current semester
                String sql = "Select * from PROFESSORS where SEMESTER = '"+ getCurrentSemester() +"'";
                Statement st = con.createStatement();
                rs = st.executeQuery(sql);
            }
            
            //retrieve and store professors into list
            while(rs.next()){
                Teacher prof = new Teacher();
                prof.setAnum          (rs.getString("PROF_ID"));
                prof.setName          (rs.getString("PROF_NAME"));
                prof.setTimePreference(rs.getString("TIME_PREF"));
                profs.add(prof);
            }
            
            con.close();
        }catch(SQLException err){
            System.out.println( "Error retrieving Professors!");
            //err.printStackTrace();
        }
        return profs;
    }
    
    /** Adds the passed in Teacher object to the PROFESSORS table. Throws error
     * if that professor already exists in database
     * 
     * @param prof - teacher object
     */
    public void addNewProfessor(Teacher prof)
    {
        try{
            Connection con = DriverManager.getConnection(host, username, password);
            
            //manual dup checking
            String sqlCheck = "select * from PROFESSORS where SEMESTER = '"+ getCurrentSemester() +"'";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sqlCheck);
            
            while(rs.next()){
                if(prof.getAnum().equals(rs.getString("PROF_ID"))){
                    SQLException err = new SQLException();
                    throw err;
                }
            }
            
            //temp store prof's course listing
            List<Course> c = prof.getCourses();
            
            //create an entry in PROFESSORS
            String sql = "insert into PROFESSORS values(?,?,?,?)";
            PreparedStatement ps = con.prepareStatement(sql);           
            ps.setString(1, getCurrentSemester());
            ps.setString(2, prof.getName());
            ps.setString(3, prof.getTimePreference());
            ps.setString(4, prof.getAnum());
            ps.executeUpdate();

            //update course professor column to reflect name change
            for(int i = 0; i < c.size(); i++){ 
                String sql3 = "update COURSES set PROFESSOR = '"+ prof.getName() +"' where CRN = " + c.get(i).getCrn();
                PreparedStatement ps3 = con.prepareStatement(sql3);
                ps3.executeUpdate();
            }           
            con.close();         
        }
        catch(SQLException err){
            System.out.println( "Error inputing Professor! Professor may already exist!");
            //err.printStackTrace();
        } 
    }
    
    /** Removes teacher entry from database. This handles all references to this entry
     *  in other tables as well. The getProfessors() method should be called first to obtain
     *  front-end representation of database. If a teacher is deleted from the front-end, 
     *  this function should be used to update the database correspondingly.
     * 
     * @param prof - the teacher object corresponding to the database entry to be removed
     */
    public void removeProfessor(Teacher prof)
    {
        try{
            Connection con = DriverManager.getConnection(host, username, password);
           
            //remove from PROFESSORS
            String sql = "delete from PROFESSORS where PROF_ID = '"+ prof.getAnum()+"' and SEMESTER = '"+getCurrentSemester()+"'";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.executeUpdate();

            //remove from COURSES
            String sql3 = "update COURSES set PROFESSOR = "+ null +" "
                    + "where PROFESSOR = '"+ prof.getName() +"' and SEMESTER = '"+getCurrentSemester()+"'";
            PreparedStatement ps3 = con.prepareStatement(sql3);
            ps3.executeUpdate();
           
            con.close();
        }
        catch(SQLException err){
            System.out.println( "Error deleting Professor!");
            err.printStackTrace(); 
        }
    }
    
    /**Updates teacher entry in database to new values of teacher passed in.
     * The getProfessors() method should be called first to obtain front-end representation
     * of database. As a professor is updated, this function should be called to 
     * change the database entry to new values
     * 
     * @param prof - the Teacher object, with new values, to overwrite old existing entry
     */
    public void alterProfessor(Teacher prof, String oldName)
    {
        //alter with update 
        try{
            Connection con = DriverManager.getConnection(host, username, password);

            //alter PROFESSORS
            String sql = "update PROFESSORS set "
                    + "PROF_NAME = '"+prof.getName()+"',"
                    + "TIME_PREF = '"+prof.getTimePreference()+"'"
                    + "where PROF_ID = '"+prof.getAnum()+"' and SEMESTER = '"+getCurrentSemester()+"'";      
            PreparedStatement ps = con.prepareStatement(sql);
            ps.executeUpdate();
            
            List<Course> c = getCourses("PROFESSOR",oldName);
            
            for(int i = 0; i < c.size(); i++){ 
                String sql3 = "update COURSES set PROFESSOR = '"+ prof.getName() +"' where CRN = " + c.get(i).getCrn();
                PreparedStatement ps3 = con.prepareStatement(sql3);
                ps3.executeUpdate(); 
            }    
            con.close();
        }catch(SQLException err){
            System.out.println( "Error altering Professor!");
           err.printStackTrace(); 
        }
                
    }
    
    /**Returns a list of Course objects constructed from the Database.
     * 
     * @param constraint - name of the column being restricted to
     * @param value - name of the value we are searching for in the constraint 
     *                column
     * @return - an ArrayList of Course objects constructed from the selected 
     *           entries in the database
     */
    public List getCourses(String constraint, String value)
    {
        List<Course> courses = new ArrayList();
        //System.out.println(currentSemester);
        try{
            Connection con = DriverManager.getConnection(host, username, password);
            ResultSet rs;
            
            if(value == null && constraint != null){
                String sql = "Select * from COURSES where SEMESTER = '"+ getCurrentSemester() +"' and "
                        + constraint +" is null";
                Statement st = con.createStatement();
                rs = st.executeQuery(sql);                   
            }
            else if(constraint != null){
                //select courses based on current semester and constraint
                if(constraint.equals("CRN")){
                    int ivalue = Integer.parseInt(value);
                    String sql = "Select * from COURSES where SEMESTER = '"+ getCurrentSemester() +"' and "
                        + constraint +" = "+ ivalue;
                    Statement st = con.createStatement();
                    rs = st.executeQuery(sql); 
                }
                else{
                String sql = "Select * from COURSES where SEMESTER = '"+ getCurrentSemester() +"' and "
                        + constraint +" = '"+ value +"'";
                Statement st = con.createStatement();
                rs = st.executeQuery(sql);
                }
            }
            else{
                //select courses based on current semester
                String sql = "Select * from COURSES where SEMESTER = '"+ getCurrentSemester() +"'";
                Statement st = con.createStatement();
                rs = st.executeQuery(sql);
            }
            
            //capture data and store into array list
            while(rs.next()){
                Course course = new Course();
                course.setSemester(currentSemester);
                course.setCrn(rs.getInt          ("CRN"));
                course.setDepartment(rs.getString("DEPARTMENT"));
                course.setCourseNum(rs.getString ("COURSE_NUM"));
                course.setName(rs.getString      ("COURSE_NAME"));
                course.setM_enroll(rs.getInt     ("M_ENROLL"));
                course.setEnroll(rs.getInt       ("ENROLL"));
                course.setAvail(rs.getInt        ("AVAIL"));
                course.setWaitList(rs.getInt     ("WAIT_LIST"));
                course.setDays(rs.getString      ("DAYS"));
                course.setsTime(rs.getString     ("START_TIME"));
                course.seteTime(rs.getString     ("END_TIME"));
                course.setBuilding(rs.getString  ("BUILDING"));
                course.setClassroom(rs.getString ("CLASSROOM"));
                course.setProf(rs.getString      ("PROFESSOR"));
                //System.out.println(course);
                courses.add(course);
            }
            
            con.close();
        }catch(SQLException err){
            System.out.println( "Error retrieving Courses!");
            //err.printStackTrace();
        }

        return courses;
    }
    
    /** Utility function for getProfessor(). Retrieves a single course
     * 
     * @param crn - unique crn to look for 
     * @return the found course
     */
    public Course getSingleCourse(int crn){
        Course course = new Course();
        try{
            Connection con = DriverManager.getConnection(host, username, password);
            
            String sql = "select * from COURSES where SEMESTER = '"+getCurrentSemester()+"' and CRN = "+crn;
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            if(rs.next()){
            course.setCrn(rs.getInt   ("CRN"));
            course.setDepartment(rs.getString("DEPARTMENT"));
            course.setCourseNum(rs.getString("COURSE_NUM"));
            course.setName(rs.getString("COURSE_NAME"));
            course.setM_enroll(rs.getInt   ("M_ENROLL"));
            course.setEnroll(rs.getInt   ("ENROLL"));
            course.setAvail(rs.getInt   ("AVAIL"));
            course.setWaitList(rs.getInt   ("WAIT_LIST"));
            course.setDays(rs.getString("DAYS"));
            course.setsTime(rs.getString("START_TIME"));
            course.seteTime(rs.getString("END_TIME"));
            course.setBuilding(rs.getString("BUILDING"));
            course.setClassroom(rs.getString("CLASSROOM"));
            course.setProf(rs.getString("PROFESSOR"));
            }
            
        }catch(SQLException err){
            System.out.println( "Error");
            //err.printStackTrace();           
        }
        return course;
    }
    
    /** Adds the passed in Course object to the COURSES table. Throws error
     *  if that course already exists in database
     * 
     * @param course - new classroom object to be recorded into database
     */
    public void addNewCourse(Course course){
        try{
            Connection con = DriverManager.getConnection(host, username, password);
           
            //manual dup checking
            String sqlCheck = "select * from COURSES where SEMESTER = '"+getCurrentSemester()+"'";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sqlCheck);
            
            while(rs.next()){
                if(rs.getInt("CRN") == course.getCrn()){
                    SQLException err = new SQLException();
                    throw err;
                }
            }
            
            String sql = "insert into COURSES values(?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?)";             
            PreparedStatement ps = con.prepareStatement(sql);   
            ps.setString(1, getCurrentSemester());   
            ps.setInt   (2, course.getCrn());
            ps.setString(3, course.getDepartment());
            ps.setString(4, course.getCourseNum());
            ps.setString(5, course.getName());   
            ps.setInt   (6, course.getM_enroll());   
            ps.setInt   (7, course.getEnroll());   
            ps.setInt   (8, course.getAvail());   
            ps.setInt   (9, course.getWaitList());
            ps.setString(10, course.getDays());             
            ps.setString(11, course.getSTime());             
            ps.setString(12, course.getETime());
            ps.setString(13, course.getBuilding());      
            ps.setString(14, course.getClassroom());         
            ps.setString(15, course.getProf());     
            ps.executeUpdate();
                
            con.close();
        }
        catch(SQLException err){
            System.out.println( "Error inputing Course! Course may already exist!");
            //err.printStackTrace();           
        }
    }
    
    /** Removes course entry from database. This handles all references to this entry
     *  in other tables as well. The getCourses() method should be called first to obtain
     *  front-end representation of database. If a course is deleted from the front-end, 
     *  this function should be used to update the database correspondingly.
     * 
     * @param course - the course object corresponding to the database entry to be removed
     */
    public void removeCourse(Course course)
    {
        try{
            Connection con = DriverManager.getConnection(host, username, password);
            
            //remove course from COURSES
            String sql = "delete from COURSES where CRN = " + course.getCrn() +" and SEMESTER = '"+getCurrentSemester()+"'";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.executeUpdate();
            
            con.close(); 
        }
        catch(SQLException err){
            System.out.println( "Error removing Course!");
            //err.printStackTrace();           
        }
    }
    
    /**Updates course entry in database to new values of course passed in.
     * The getCourses() method should be called first to obtain front-end representation
     * of database. As a course is updated, this function should be called to 
     * change the database entry to new values
     * 
     * @param course - the course object, with new values, to overwrite old existing entry
     */
    public void alterCourse(Course course)
    {
        try{
            Connection con = DriverManager.getConnection(host, username, password);
            
            Course c = getSingleCourse(course.getCrn());
            String t = c.getProf();//old teacher for course
            
            String sql =  "update COURSES set "
                    + "DEPARTMENT = '"   +course.getDepartment() +"',"
                    + "COURSE_NUM = '"   +course.getCourseNum()  +"',"
                    + "COURSE_NAME = '" +course.getName()       +"',"
                    + "M_ENROLL = "     +course.getM_enroll()   +","
                    + "ENROLL = "       +course.getEnroll()     +","
                    + "AVAIL = "        +course.getAvail()      +","
                    + "WAIT_LIST = "    +course.getWaitList()   +","
                    + "DAYS = '"        +course.getDays()       +"',"
                    + "START_TIME = '"  +course.getSTime()      +"',"
                    + "END_TIME = '"    +course.getETime()      +"',"
                    + "BUILDING = '"    +course.getBuilding()   +"',"
                    + "CLASSROOM = '"   +course.getClassroom()  +"',"
                    + "PROFESSOR = '"   +course.getProf()       +"'"
                    + "where CRN = " + course.getCrn() + " and SEMESTER = '"+getCurrentSemester()+"'";
            PreparedStatement ps3 = con.prepareStatement(sql);
            ps3.executeUpdate();
 
            
            con.close(); 
        }
        catch(SQLException err){
            System.out.println( "Error altering Course!");
            err.printStackTrace();           
        }    
    }
    
    /**Returns a list of Classroom objects constructed from the Database.
     * 
     * @param constraint - name of the column being restricted to
     * @param value - name of the value we are searching for in the constraint 
     *                column
     * @return - an ArrayList of Classroom objects constructed from the selected 
     *           entries in the database
     */
    public List getClassrooms(String constraint, String value)
    {
        List<Classroom> classrooms = new ArrayList();
        
        try{
            Connection con = DriverManager.getConnection(host, username, password);
            ResultSet rs;
            
            if(value == null && constraint != null){
                String sql = "Select * from PROFESSORS where SEMESTER = '"+ getCurrentSemester() +"' and "
                        + constraint +" is null";
                Statement st = con.createStatement();
                rs = st.executeQuery(sql);                   
            }
            else if(constraint != null){
                //select classrooms based on current semester and constraint 
                String sql = "Select * from CLASSROOMS where SEMESTER = '"+ getCurrentSemester() +"' and "
                        + constraint +" = '"+ value +"'";
                Statement st = con.createStatement();
                rs = st.executeQuery(sql);   
            }
            else{
                //select classrooms based on current semester
                String sql = "Select * from CLASSROOMS where SEMESTER = '"+ getCurrentSemester() +"'";
                Statement st = con.createStatement();
                rs = st.executeQuery(sql);
            }
            
            while(rs.next()){
                Classroom classroom = new Classroom();
                classroom.setRoomNum(rs.getString("ROOM_NUM"));
                classroom.setmEnroll(rs.getInt("M_ENROLL"));
                classroom.setBuildingName(rs.getString("BUILDING"));
                classrooms.add(classroom);
            }
            
            con.close();           
        }catch(SQLException err){
            System.out.println( "Error retrieving Classrooms!");
            //err.printStackTrace();           
        }
        return classrooms;
    }
    
    /** Adds the passed in Classroom object to the CLASSROOMS table. Throws error
     *  if that classroom already exists in database
     * 
     * @param classroom - new classroom object to be recorded into database
     */
    public void addClassroom(Classroom classroom)
    {
        try{
            Connection con = DriverManager.getConnection(host, username, password);
        
            //manual dup checking
            String sqlCheck = "select * from CLASSROOMS where SEMESTER = '"+ getCurrentSemester() +"'";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sqlCheck);
            
            while(rs.next()){
                if((rs.getString("ROOM_NUM").equals(classroom.getRoomNum())) && (rs.getString("BUILDING").equals(classroom.getBuildingName()))){
                    SQLException err = new SQLException();
                    throw err;
                }
            }        
        
            String sql = "insert into CLASSROOMS values(?,?,?,?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, getCurrentSemester());
            ps.setString(2, classroom.getRoomNum());
            ps.setString(3, classroom.getBuildingName());
            ps.setInt   (4, classroom.getmEnroll());
            ps.executeUpdate();

            con.close();
        }catch(SQLException err){
            System.out.println( "Error inputing Classroom! Classroom may already exist!");
            //err.printStackTrace();           
        }
    }
    
    /** Removes classroom entry from database. This handles all references to this entry
     *  in other tables as well. The getClassrooms() method should be called first to obtain
     *  front-end representation of database. If a classroom is deleted from the front-end, 
     *  this function should be used to update the database correspondingly.
     * 
     * @param classroom - the classroom object corresponding to the database entry to be removed
     */
    public void removeClassroom(Classroom classroom)
    {
        try{
            Connection con = DriverManager.getConnection(host, username, password);

            //remove from CLASSROOMS
            String sql = "delete from CLASSROOMS where ROOM_NUM = '"+ classroom.getRoomNum() +"' and SEMESTER = '"+ getCurrentSemester() +"'";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.executeUpdate();

            //remove from COURSES
            String sql2 = "update COURSES set CLASSROOM = "+ null +" where CLASSROOM = '"+ classroom.getRoomNum() +"' and SEMESTER = '"+ getCurrentSemester() +"'";
            PreparedStatement ps2 = con.prepareStatement(sql2);
            ps2.executeUpdate();
            
            con.close();
        }catch(SQLException err){
            System.out.println( "Error deleting Classroom!");
            //err.printStackTrace();           
        }    
    }
    
    /**Updates classroom entry in database to new values of classroom passed in.
     * The getClassrooms() method should be called first to obtain front-end representation
     * of database. As a classroom is updated, this function should be called to 
     * change the database entry to new values
     * 
     * @param classroom - the classroom object, with new values, to overwrite old existing entry
     */
    public void alterClassroom(Classroom classroom)
    {
        try{
            Connection con = DriverManager.getConnection(host, username, password);

            System.out.println(classroom);
            
            String sql = "update CLASSROOMS set "
                    + "BUILDING = '"+ classroom.getBuildingName() +"', "
                    + "M_ENROLL = "+ classroom.getmEnroll()
                    + " where ROOM_NUM = '" + classroom.getRoomNum() + "' and SEMESTER = '"+getCurrentSemester()+"'";
            System.out.println(sql);
            PreparedStatement ps = con.prepareStatement(sql);
            ps.executeUpdate();

            con.close();

            }catch(SQLException err){
                System.out.println( "Error altering Classroom!");
                err.printStackTrace();           
        }          
    }
    
    /** Clears out a specified semester from the database. If no semester is 
     *  specified, the entire database is cleared.
     * 
     * @param semester - the semester to be cleared. If null, entire database is
     *                   cleared
     */
    public void clearDatabase(String semester)
    {
        //clear classrooms, courses, and professors
        try{
            Connection con = DriverManager.getConnection(host, username, password);
            
            if(semester != null){
                String sql = "delete from CLASSROOMS where SEMESTER ='"+ semester +"'";
                PreparedStatement ps = con.prepareStatement(sql);
                ps.executeUpdate();

                String sql2 = "delete from COURSES where SEMESTER ='"+ semester +"'";
                ps = con.prepareStatement(sql2);
                ps.executeUpdate();

                String sql3 = "delete from PROFESSORS where SEMESTER ='"+ semester +"'";
                ps = con.prepareStatement(sql3);
                ps.executeUpdate();            

                String sql4 = "delete from USER_SCHEDULES where NAME ='"+ semester +"'";
                ps = con.prepareStatement(sql4);
                ps.executeUpdate();
            }
            else{
                String sql = "delete from CLASSROOMS";
                PreparedStatement ps = con.prepareStatement(sql);
                ps.executeUpdate();

                String sql2 = "delete from COURSES";
                ps = con.prepareStatement(sql2);
                ps.executeUpdate();

                String sql3 = "delete from PROFESSORS";
                ps = con.prepareStatement(sql3);
                ps.executeUpdate();            

                String sql4 = "delete from USER_SCHEDULES";
                ps = con.prepareStatement(sql4);
                ps.executeUpdate();  
            }

            
            con.close();
        }catch(SQLException err){
            System.out.println("Error clearing Database");
            //err.printStackTrace();              
        }
    }

    /**
     * @return the duration
     */
    public int[] getDuration() {
        return duration;
    }

    /**
     * @param duration the duration to set
     */
    public void setDuration(int[] duration) {
        this.duration = duration;
    }
}
