/**
 * 
 * 
 * @author AJ
 */
package course_scheduler;

import java.util.*;
import java.sql.*;

/**
 * There are at least *five* tables so far.
 * @author AJ
 */
public class DatabaseUtility {
    
    private String host;        //local host for db
    private String username;    //access un
    private String password;    //access pw
    private String currentSemester;
    
    /** DONE
     *  The host is default, username and password are set and I don't 
     * know how to change them without starting a new database. 
     */
    public DatabaseUtility(){

        host = "jdbc:derby://localhost:1527/SchoolData";    
        username = "user1";
        password = "123456";
        currentSemester = "";                               //currently selected semester
    }        
    
    /** Changes the currently selected semester to a new one. The new semester 
     * string will come from the db table maintaining all currently created 
     * semesters. These will have to be sent to a drop down list in the view 
     * for the user to select.
     * @param newCurrentSemeter - the new semester to set as currently selected
     */
    public void setSemester(String newCurrentSemeter){
        this.currentSemester = newCurrentSemeter;
    }
    
    /**DONE
     * Creates a new table that stores a schedules data. Also stores the users
     * table name into a table of the user's schedules.
     * @param tableName 
     */
    public void createScheduleTable(String tableName)
    {
        try{
            Connection con = DriverManager.getConnection(host, username, password);
            
            //create new schedule table
            String sql = "CREATE TABLE "+tableName+"("
                    + "CRN INTEGER NOT NULL,"
                    + "COURSE_NUM INTEGER,"
                    + "NAME VARCHAR(32),"
                    + "DEPARTMENT VARCHAR(32),"
                    + "CLASSROOM VARCHAR(32),"
                    + "BUILDING VARCHAR(32),"
                    + "ENROLLMENT INTEGER,"
                    + "TIME VARCHAR(32),"
                    + "PROFESSOR VARCHAR(32),"
                    + "PRIMARY KEY (CRN))";
           
            PreparedStatement ps = con.prepareStatement(sql);
            ps.executeUpdate();
            
            //store name into table of existing tables
            String sql2 = "insert into USER_SCHEDULES values("+tableName+")";
            PreparedStatement ps2 = con.prepareStatement(sql2);
            ps2.executeUpdate();
            
            con.close();
        }catch(SQLException err){
            System.out.println( "Error Creating Table!");
            err.printStackTrace();
        }
    }
    
    /**DONE
     * 
     * @param tableName 
     */
    public void deleteScheduleTable(String tableName)
    {
        try{
            Connection con = DriverManager.getConnection(host, username, password);
            
            //remove table from database
            String sql = "drop table "+tableName;
            PreparedStatement ps = con.prepareStatement(sql);
            ps.executeUpdate();
            
            //remove from tables of schedules
            String sql2 = "delete from USER_SCHEDULES where NAME = '"+tableName+"'";
            PreparedStatement ps2 = con.prepareStatement(sql2);
            ps2.executeUpdate();           
            
            con.close();
        }catch(SQLException err){
            System.out.println( "Error Creating Table!");
            err.printStackTrace();
        }        
    }
    
    /**NOT DONE YET - need functionality to select a constraint restricted set 
     * of data
     * Returns a list of Teacher objects constructed from the Database.
     * @param constraint - not use yet
     * @return 
     */
    public List getProfessors(String constraint)
    {
        //constraint - selection limiter - by department, etc. NOT USED YET
        
        List<Teacher> profs = new ArrayList();
        
        try{
            Connection con = DriverManager.getConnection(host, username, password);
            
            System.out.println("Preparing SQL");
            String sql = "Select * from PROFESSORS";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            
            //get courses for professors
            
            System.out.println("Retrieving...");
            while(rs.next()){
                Teacher prof = new Teacher();
                prof.id = rs.getInt("ID");
                prof.name = rs.getString("PROFESSORNAME");
                prof.timePreference = rs.getString("TIME_PREFERENCE");
                profs.add(prof);
            }
            
            System.out.println("Closing...");
            con.close();
        }catch(SQLException err){
            System.out.println( "Error retrieving Professors!");
            err.printStackTrace();
        }
        
            System.out.println("Ending...");
        return profs;
    }
    
    /**
     * DONE
     * @param prof - teacher object
     */
    public void addNewProfessor(Teacher prof)
    {
        try{
            Connection con = DriverManager.getConnection(host, username, password);
            
            System.out.println(prof.id);
            System.out.println(prof.name);
            System.out.println(prof.timePreference);
            
            System.out.println("insert into PROFESSORS");
            //create an entry in PROFESSORS
            String sql = "insert into PROFESSORS values(?,?,?)";
            PreparedStatement ps = con.prepareStatement(sql);           
            ps.setInt   (1, prof.id);
            ps.setString(2, prof.name);
            ps.setString(3, prof.timePreference);
            ps.executeUpdate();

            System.out.println("insert into PROF_COURSES");
            //create an entry in PROF_COURSES for this prof
            String sql2 = "insert into PROF_COURSES values(?,?,?,?,?,?)";
            PreparedStatement ps2 = con.prepareStatement(sql2);
            ps2.setInt(1, prof.id);
            ps2.setString(2, null);
            ps2.setString(3, null);
            ps2.setString(4, null);
            ps2.setString(5, null);
            ps2.setString(6, null);
            ps2.executeUpdate();
           
            //System.out.println("maybe it worked");
            con.close();         
        }
        catch(SQLException err){
            System.out.println( "Error inputing Professor! Professor may already exist!");
            //err.printStackTrace();
        } 
    }
    
    /**
     * DONE
     * @param prof 
     */
    public void removeProfessor(Teacher prof)
    {
        try{
            Connection con = DriverManager.getConnection(host, username, password);
           
            //found
            //remove from PROFESSORS
            String sql = "delete from PROFESSORS where ID = "+ prof.id;
            PreparedStatement ps = con.prepareStatement(sql);
            ps.executeUpdate();

            //remove from PROF_COURSES
            String sql2 = "delete from PROF_COURSES where PROF_ID = "+ prof.id;
            PreparedStatement ps2 = con.prepareStatement(sql2);
            ps2.executeUpdate();

            //remove from COURSES
            String sql3 = "update COURSES set PROFESSOR = "+null+" where PROFESSOR = '"+ prof.name +"'";
            PreparedStatement ps3 = con.prepareStatement(sql3);
            ps3.executeUpdate();
           
            con.close();
        }
        catch(SQLException err){
            System.out.println( "Error deleting Professor!");
            //err.printStackTrace(); 
        }
    }
    
    /**DONE
     * pass in prof that is assumed to already exist in db and teacher obj has 
     * already been updated with correct values ID SHOULD NEVER CHANGE ONCE 
     * USED, now set to static in class
     * @param prof 
     */
    public void alterProfessor(Teacher prof)
    {
        //alter with update 
        try{
            Connection con = DriverManager.getConnection(host, username, password);

            //alter PROFESSORS
            String sql = "update PROFESSORS set PROFESSORNAME = ?, TIME_PREFERENCE = ?";           //alter
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, prof.name);
            ps.setString(2  , prof.timePreference);
            ps.executeUpdate();
            
            con.close();
        }catch(SQLException err){
            System.out.println( "Error altering Professor!");
           //err.printStackTrace(); 
        }
                
    }
    
    /**NOT DONE YET - need functionality to select a constraint restricted set 
     * of data
     * 
     * @param constraint
     * @return 
     */
    public List getCourses(String constraint)
    {
        //constraint - selection limiter - by department, etc. NOT USED YET
        
        List<Course> courses = new ArrayList();
        
        try{
            Connection con = DriverManager.getConnection(host, username, password);
            
            System.out.println("Preparing SQL");
            String sql = "Select * from COURSES";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            
            System.out.println("Retrieving...");
            while(rs.next()){
                Course course = new Course();
                course.crn = rs.getInt("CRN");
                course.courseNum = rs.getInt("COURSE_NUM");
                course.name = rs.getString("NAME");
                course.department = rs.getString("DEPARTMENT");
                course.classroom = rs.getString("CLASSROOM");
                course.building = rs.getString("BUILDING");
                course.enrollment = rs.getInt("ENROLLMENT");
                course.time = rs.getString("TIME");
                course.prof = rs.getString("PROFESSOR");
                courses.add(course);
            }
            
            System.out.println("Closing...");
            con.close();
        }catch(SQLException err){
            System.out.println( "Error retrieving Courses!");
            //err.printStackTrace();
        }
        
            System.out.println("Edning...");
        return courses;
    }
    
    /**
     * DONE
     * @param course 
     */
    public void addNewCourse(Course course){
        try{
            Connection con = DriverManager.getConnection(host, username, password);
            

            
            String sql = "insert into COURSES values(?,?,?,?,?,?,?,?,?)";             
            PreparedStatement ps = con.prepareStatement(sql);              
            ps.setInt   (1, course.crn);
            ps.setInt   (2, course.courseNum);
            ps.setString(3, course.name);   
            ps.setString(4, course.department);          
            ps.setString(5, course.building);        
            ps.setInt   (6, course.enrollment);        
            ps.setString(7, course.time);              
            ps.setString(8, course.prof);          
            ps.setString(9,course.classroom);
            ps.executeUpdate();
            //System.out.println("done?");
                
            con.close();
        }
        catch(SQLException err){
            System.out.println( "Error inputing Course! Course may already exist!");
            //err.printStackTrace();           
        }
    }
    
    /** DONE
     * FUN FACT: If the SQL query finds no row matching the search requirements
     * no row is deleted AND no error is thrown! Of course, this doesn't let you
     * know if what your trying to delete isn't actually there.
     * 
     * @param course the course to be deleted, specifically accesses course.name
     *      to find the course in database tables
     */
    public void removeCourse(Course course)
    {
        try{
            Connection con = DriverManager.getConnection(host, username, password);
            
            String sql = "delete from COURSES where CRN = " + course.crn;
            PreparedStatement ps = con.prepareStatement(sql);
            System.out.println("removing course...");
            ps.executeUpdate();
            
            //this tries to remove all instances of the course from a column
            //if nothing can be removed, nothing bad happens
            //iterates through all 5 columns
            for (int i = 1; i < 6; i++){
                String sql2 = "update PROF_COURSES set COURSE_"+ i +" = "+null+" where COURSE_"+ i +" = '"+ course.name +"'";
                PreparedStatement ps2 = con.prepareStatement(sql2);
                ps2.executeUpdate();
            }
            con.close(); 
        }
        catch(SQLException err){
            System.out.println( "Error removing Course!");
            //err.printStackTrace();           
        }
    }
    
    /**DONE
     * CRN is marked as a primary value in COURSES table, so it's value cannot 
     * be updated, only deleted along with the entire record.
     * @param course 
     */
    public void alterCourse(Course course)
    {
        try{
            Connection con = DriverManager.getConnection(host, username, password);
            
            String sql =  "update COURSES set "
                        + "COURSE_NUM = ?,"
                        + "NAME = ?,"
                        + "DEPARTMENT = ?,"
                        + "CLASSROOM = ?,"
                        + "BUILDING = ?,"
                        + "ENROLLMENT = ?,"
                        + "TIME = ?,"
                        + "PROFESSOR = ?"
                        + "where CRN = " + course.crn;
            PreparedStatement ps = con.prepareStatement(sql);
            System.out.println("screaming");
            ps.setInt   (1, course.courseNum);
            ps.setString(2, course.name);
            ps.setString(3, course.department);
            ps.setString(4, course.classroom);
            ps.setString(5, course.building);
            ps.setInt   (6, course.enrollment);
            ps.setString(7, course.time);
            ps.setString(8, course.prof);
            ps.executeUpdate();
 
            con.close(); 
        }
        catch(SQLException err){
            System.out.println( "Error altering Course!");
            //err.printStackTrace();           
        }    
    }
    
    /**
     * 
     * @param constraint
     * @return 
     */
    public List getClassrooms(String constraint)
    {
        List<Classroom> classrooms = new ArrayList();
        
        try{
            //constraint - selection limiter - by department, etc. NOT USED YET
            
            Connection con = DriverManager.getConnection(host, username, password);
            
            System.out.println("Preparing SQL");
            String sql = "Select * from CLASSROOM";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            
            System.out.println("Retrieving...");
            
            while(rs.next()){
                Classroom classroom = new Classroom();
                classroom.setRoomNum(rs.getString("ROOM_NUM"));
                classroom.setmEnroll(rs.getInt("MAX_ENROLLMENT"));
                classroom.setBuildingName(rs.getString("BUILDING"));
                classrooms.add(classroom);
            }
            
            System.out.println("Closing...");
            con.close();           
        }catch(SQLException err){
            System.out.println( "Error retireving Classrooms!");
            //err.printStackTrace();           
        }
        
        return classrooms;
    }
    
    /**
     * 
     * @param classroom 
     */
    public void addClassroom(Classroom classroom)
    {
        //do we need this?
        try{
        Connection con = DriverManager.getConnection(host, username, password);
        
        String sql = "insert into CLASSROOMS values(?,?,?)";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, classroom.getRoomNum());
        ps.setInt   (2, classroom.getmEnroll());
        ps.setString(3, classroom.getBuildingName());
        ps.executeUpdate();
        
        con.close();
        
        }catch(SQLException err){
            System.out.println( "Error inputing Classroom! Classroom may already exist!");
            //err.printStackTrace();           
        }
    }
    
    /**
     * 
     * @param classroom 
     */
    public void removeClassroom(Classroom classroom)
    {
        try{
            Connection con = DriverManager.getConnection(host, username, password);

            //remove from CLASSROOMS
            String sql = "delete from CLASSROOMS where ROOM_NUM = '" + classroom.getRoomNum() +"'";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.executeUpdate();

            //remove from COURSES
            String sql2 = "update COURSES set CLASSROOM = "+null+" where CLASSROOM = '"+ classroom.getRoomNum() +"'";
            PreparedStatement ps2 = con.prepareStatement(sql2);
            ps2.executeUpdate();
            
            con.close();
        }catch(SQLException err){
            System.out.println( "Error deleting Classroom!");
            //err.printStackTrace();           
        }    
    }
    
    /**
     * 
     * @param classroom 
     */
    public void alterClassroom(Classroom classroom)
    {
        try{
            Connection con = DriverManager.getConnection(host, username, password);

            String sql = "update CLASSROOMS "
                    + "MAX_ENROLLMENT = ?, "
                    + "BUILDING = ?"
                    + "where ROOM_NUM = " + classroom.getRoomNum();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt   (1, classroom.getmEnroll());
            ps.setString(2, classroom.getBuildingName());
            ps.executeUpdate();

            con.close();

            }catch(SQLException err){
                System.out.println( "Error altering Classroom!");
                //err.printStackTrace();           
        }          
    }
    
    /**NOT DONE - NEED TO REFACTOR TO NOT USE COURSE LOAD VARIABLE. 
     * Requires there to be a course and professor to exist already 
     * before they are connected.  
     * @param prof
     * @param course 
     */
    public void assignCoursetoProf(Teacher prof, Course course){
        
        prof.courseLoad++;
        
        //System.out.println(prof.courseLoad);
        if(prof.courseLoad < 6){    //unfortunately, we are limited to a max number of courses a professor can teach
            try{  
                Connection con = DriverManager.getConnection(host, username, password);

                String sqlCheck = "select * from PROF_COURSES where PROF_ID="+prof.id;
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(sqlCheck);
                rs.next();                  //bump rs to first entry of result set
                for (int i = 1; i < 6; i++){//for each course
                    //System.out.println(rs.getString("COURSE_1"));
                    System.out.println(i);
                    System.out.println(rs.getString("COURSE_"+i));
                    System.out.println(course.name);
                    if(rs.getString("COURSE_"+i) != null && rs.getString("COURSE_"+i).equals(course.name)){ //else if that course is the same as we are trying to input
                        //already teaching
                        System.out.println("Professor already teaching course!");
                        break;  //bail out
                    }
                    else if (rs.getString("COURSE_"+i) != null){ //if that course is not empty
                        //do nothing, go to next
                        System.out.println("Course Slot Filled, Skipping");
                    }
                    else{
                        //teacher can teach course
                        //so add it
                        System.out.println("add to professor's course list");
                        String sql = "update PROF_COURSES set COURSE_"+ i +    //the next blank course column
                                 " = '"+course.name+"' where PROF_ID = "    //store name there
                                 +prof.id;                                  //where the this is the prof 

                        PreparedStatement ps = con.prepareStatement(sql);
                        ps.executeUpdate();
                        break; //inserted, bail out
                    }
                }
                
                //for COURSES
                String sql2 = "update COURSES set PROFESSOR = '"+prof.name+"'where CRN = "+course.crn;
                PreparedStatement ps2 = con.prepareStatement(sql2);
                ps2.executeUpdate();
                
                con.close();
            }catch(SQLException err){
                System.out.println("Error");
                //err.printStackTrace();  
            }
        }
        else
            System.out.println("Professor at max courses");
            
    }

    /**NOT DONE - NEED TO REFACTOR TO NOT USE COURSE LOAD VARIABLE.
     * Current logic assumes that there will *NEVER* be duplicate courses in a 
     * professor's courses because there can't be a duplicate added via calling
     * only this function.
     * @param prof
     * @param course 
     */
    public void removeCoursefromProf(Teacher prof, Course course)
    {
        prof.courseLoad--;
        
        try{
            Connection con = DriverManager.getConnection(host, username, password);
   
            //update PROF_COURSES
            for (int i = 1; i < 6; i++){
                String sql = "update PROF_COURSES set COURSE_"+ i +" = "+null+" where COURSE_"+ i +" = '"+ course.name +"'";
                PreparedStatement ps = con.prepareStatement(sql);
                ps.executeUpdate();
            }
            
            //update COURSES
            String sql2 = "update COURSES set PROFESSOR = '' where CRN = " + course.crn;
            PreparedStatement ps2 = con.prepareStatement(sql2);
            ps2.executeUpdate();
            
            con.close();
        }catch(SQLException err){
            System.out.println("Error Removing Course from Professor!");
            //err.printStackTrace();              
        }
    }
    
    /** NOT DONE YET - need to know if to clear generated schedules in addition 
     * obj dbs
     * 
     */
    public void clearDatabase()
    {
        //clear classrooms, courses, and professors
        try{
            Connection con = DriverManager.getConnection(host, username, password);
            
            String sql = "delete from CLASSROOMS";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.executeUpdate();
            
            String sql2 = "delete from COURSES";
            ps = con.prepareStatement(sql2);
            ps.executeUpdate();
            
            String sql3 = "delete from PROFESSORS";
            ps = con.prepareStatement(sql3);
            ps.executeUpdate();            
            
            String sql4 = "delete from PROF_COURSES";
            ps = con.prepareStatement(sql4);
            ps.executeUpdate();
            
            //clear all stored schedules?
            //String sql5 = "delete from ";
            con.close();
        }catch(SQLException err){
            System.out.println("Error clearing Database");
            //err.printStackTrace();              
        }
    }
}
