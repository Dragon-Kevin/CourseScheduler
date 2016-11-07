/**
 * 
 * 
 * @author AJ
 */
package course_scheduler;

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
    
    private String host;        //local host for db
    private String username;    //access un
    private String password;    //access pw
    private String currentSemester; //current semester is the current schedule being worked on
    
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
    
    /** DONE
     * Changes the currently selected semester to a new one. The new semester 
     * string will come from the db table maintaining all currently created 
     * semesters. These will have to be sent to a drop down list in the view 
     * for the user to select.
     * @param newCurrentSemeter - the new semester to set as currently selected
     */
    public void setSemester(String newCurrentSemeter){
        this.currentSemester = newCurrentSemeter;
    }
    
    /**DONE
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
            System.out.println( "Error Storing Semester!");
            err.printStackTrace();
        }
        return semesters;
    }
    
    /** Deprecated code -- do not use
     * @param tableName 
     */
    public void createSchedule(String tableName)
    {
        List<Course> courses = new ArrayList();
        List<Teacher> teachers = new ArrayList();
        List<Classroom> classrooms = new ArrayList();
        
        try{
            Connection con = DriverManager.getConnection(host, username, password);
            
            //pull and construct three lists from db, course list,
            //classroom list, teacher list 
            
            //pull courses
            String sql = "select * from COURSES where SEMESTER = '"+ currentSemester +"'";
            //String sql = "select * from COURSES";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            
            //consruct course list loop
            while(rs.next()){
                Course course = new Course();
                //store data
                courses.add(course);
            }
            
            //pull teachers
            String sql2 = "select * from PROFESSORS where SEMESTER = '"+ currentSemester +"'";
            Statement st2 = con.createStatement();
            ResultSet rs2 = st2.executeQuery(sql2);
            
            //construct teacher list loop
            while(rs2.next()){
                Teacher teacher = new Teacher();
                //store data
                teachers.add(teacher);
            }
            
            //pull classrooms
            String sql3 = "select * from CLASSROOMS where SEMESTER = '"+ currentSemester +"'";
            Statement st3 = con.createStatement();
            ResultSet rs3 = st2.executeQuery(sql3);
                        
            //construct classroom list loop
            while(rs3.next()){
                Classroom classroom = new Classroom();
                //store data
                classrooms.add(classroom);
            } 
            
            //got lists
            //store into schedule table with current semester 
            Iterator courseItr = courses.iterator(); 
            Iterator teacherItr = teachers.iterator();
            Iterator classroomItr = classrooms.iterator();
           
            Course c;
            //loop structure
            while(courseItr.hasNext()){
                c = (Course)courseItr.next();
                //insert into SCHEDULE 
                //a new enrty
                //semester, crn, course num, course_name, m_enroll, enroll
                //avaiable, wait listed, days, start_time, end_time, building 
                //room_num, instructor
                String sqlConstruct  = "insert into SCHEDULE values(?,?,?,?,?, ?,?,?,?,?, ?,?,?,?)";
                PreparedStatement ps = con.prepareCall(sqlConstruct);
                ps.setString(1, currentSemester);   //semester -- string
                ps.setInt   (2, c.crn);             //crn -- int
                //ps.setInt   (3, c.courseNum);       //course_num -- string
                ps.setString(4, c.name);            //course_name -- string
                //ps.setInt   (5, c.mEnroll);         //m_enroll -- int
                ps.setInt   (6, c.enrollment);      //enroll -- int
                //ps.setInt   (7, c.available);       //available -- int
                //ps.setInt   (8, c.waitList);        //wait_list -- int
                //ps.setString(9, c.days);            //days -- string
                //ps.setString(10, c.startTime);      //start time -- string
                //ps.setString(11, c.endTime);        //end time -- string
                ps.setString(12, c.building);       //building -- string
                //ps.setString(13, c.roomNum);        //room_num -- string
                //ps.setString()//instructor -- sring
  
            }
            
            con.close();
        }catch(SQLException err){
            System.out.println( "Error Storing Semester!");
            err.printStackTrace();
        }
    }
    
    /**Deprecated - do not use
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
    public List getProfessors(String constraint, String value)
    {
        //constraint - selection limiter - by department, etc. NOT USED YET
        
        List<Teacher> profs = new ArrayList();
        
        try{
            Connection con = DriverManager.getConnection(host, username, password);
            ResultSet rs;
            
            /*if(constraint == null){
                //select professors based on current semester
                String sql = "Select * from PROFESSORS where SEMESTER = '"+ currentSemester +"' and "
                        + constraint+" = ";
                Statement st = con.createStatement();
                rs = st.executeQuery(sql);   
            }
            else{*/
                //select professors based on current semester
                String sql = "Select * from PROFESSORS where SEMESTER = '"+ currentSemester +"'";
                Statement st = con.createStatement();
                rs = st.executeQuery(sql);
            //}
            //retrieve and store professors into list
            while(rs.next()){
                Teacher prof = new Teacher();
                prof.id = rs.getInt("ID");
                prof.name = rs.getString("PROFESSORNAME");
                prof.timePreference = rs.getString("TIME_PREFERENCE");
                //soft max 8 courses so far
                prof.pcourses.add(rs.getString("COURSE_1"));
                prof.pcourses.add(rs.getString("COURSE_2"));
                prof.pcourses.add(rs.getString("COURSE_3"));
                prof.pcourses.add(rs.getString("COURSE_4"));
                prof.pcourses.add(rs.getString("COURSE_5"));
                prof.pcourses.add(rs.getString("COURSE_6"));
                prof.pcourses.add(rs.getString("COURSE_7"));
                prof.pcourses.add(rs.getString("COURSE_8"));
                profs.add(prof);
            }
            
            con.close();
        }catch(SQLException err){
            System.out.println( "Error retrieving Professors!");
            err.printStackTrace();
        }
        return profs;
    }
    
    /** DONE - Might need to refactor to allow for more than 8 courses per prof 
     * 
     * @param prof - teacher object
     */
    public void addNewProfessor(Teacher prof)
    {
        try{
            Connection con = DriverManager.getConnection(host, username, password);
            
            //manual dup checking
            String sqlCheck = "select * from PROFESSORS where SEMESTER = '"+currentSemester+"'";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sqlCheck);
            
            while(rs.next()){
                if(rs.getInt("PROF_ID") == prof.id){
                    SQLException err = new SQLException();
                    throw err;
                }
            }
            
            //create an entry in PROFESSORS
            String sql = "insert into PROFESSORS values(?,?,?,?, ?,?,?,?,?,?,?,?)";
            PreparedStatement ps = con.prepareStatement(sql);           
            ps.setString(1, currentSemester);
            ps.setInt   (2, prof.id);
            ps.setString(3, prof.name);
            ps.setString(4, prof.timePreference);
            ps.setString(5, "");
            ps.setString(6, "");
            ps.setString(7, "");
            ps.setString(8, "");
            ps.setString(9, "");
            ps.setString(10, "");
            ps.setString(11, "");
            ps.setString(12, "");
            ps.executeUpdate();
            
            //insert all courses being taught by that professor
            for(int i = 0; i < prof.pcourses.size(); i++){
                String sql2 = "update PROFESSORS set COURSE_"+(i+1)+" = '"+prof.pcourses.get(i)+"' where PROF_ID = "+prof.id;
                PreparedStatement ps2 = con.prepareStatement(sql2);
                ps2.executeUpdate();
            }
            
            con.close();         
        }
        catch(SQLException err){
            System.out.println( "Error inputing Professor! Professor may already exist!");
            err.printStackTrace();
        } 
    }
    
    /**DONE
     * 
     * @param prof 
     */
    public void removeProfessor(Teacher prof)
    {
        try{
            Connection con = DriverManager.getConnection(host, username, password);
           
            //remove from PROFESSORS
            String sql = "delete from PROFESSORS where PROF_ID = "+ prof.id+" and SEMESTER = '"+currentSemester+"'";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.executeUpdate();

            //remove from COURSES
            String sql3 = "update COURSES set PROFESSOR = "+ null +" "
                    + "where PROFESSOR = '"+ prof.name +"' and SEMESTER = '"+currentSemester+"'";
            PreparedStatement ps3 = con.prepareStatement(sql3);
            ps3.executeUpdate();
           
            con.close();
        }
        catch(SQLException err){
            System.out.println( "Error deleting Professor!");
            err.printStackTrace(); 
        }
    }
    
    /**DONE
     * @param prof 
     */
    public void alterProfessor(Teacher prof)
    {
        //alter with update 
        try{
            Connection con = DriverManager.getConnection(host, username, password);

            //alter PROFESSORS
            String sql = "update PROFESSORS set values(?,?,?,?, ?,?,?,?, ?,?,?,?) "
                    + "where PROF_ID = "+prof.id+" and SEMESTER = '"+currentSemester+"'";      
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1,  currentSemester);
            ps.setInt   (2,  prof.id);
            ps.setString(3,  prof.name);
            ps.setString(4,  prof.timePreference);
            ps.setString(5,  prof.pcourses.get(0));
            ps.setString(6,  prof.pcourses.get(1));
            ps.setString(7,  prof.pcourses.get(2));
            ps.setString(8,  prof.pcourses.get(3));
            ps.setString(9,  prof.pcourses.get(4));
            ps.setString(10, prof.pcourses.get(5));
            ps.setString(11, prof.pcourses.get(6));
            ps.setString(12, prof.pcourses.get(7));
            ps.executeUpdate();
            
            con.close();
        }catch(SQLException err){
            System.out.println( "Error altering Professor!");
           //err.printStackTrace(); 
        }
                
    }
    
    /**DONE - need functionality to select a constraint restricted set 
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
            
            //select by current semester
            String sql = "Select * from COURSES where SEMESTER = '"+ currentSemester +"'";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            
            //capture data and store into array list
            while(rs.next()){
                Course course = new Course();
                course.crn =        rs.getInt   ("CRN");
                course.department = rs.getString("DEPARTMENT");
                course.courseNum =  rs.getString("COURSE_NUM");
                course.name =       rs.getString("COURSE_NAME");
                course.crn =        rs.getInt   ("M_ENROLL");
                course.crn =        rs.getInt   ("ENROLL");
                course.crn =        rs.getInt   ("AVAIL");
                course.crn =        rs.getInt   ("WAIT_LIST");
                course.department = rs.getString("DAYS");
                course.enrollment = rs.getInt   ("START_TIME");
                course.time =       rs.getString("END_TIME");
                course.building =   rs.getString("BUILDING");
                course.classroom =  rs.getString("CLASSROOM");
                course.prof =       rs.getString("PROFESSOR");
                courses.add(course);
            }
            con.close();
        }catch(SQLException err){
            System.out.println( "Error retrieving Courses!");
            err.printStackTrace();
        }
        return courses;
    }
    
    /**DONE
     * 
     * @param course 
     */
    public void addNewCourse(Course course){
        try{
            Connection con = DriverManager.getConnection(host, username, password);
           
            //manual dup checking
            String sqlCheck = "select * from COURSES where SEMESTER = '"+currentSemester+"'";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sqlCheck);
            
            while(rs.next()){
                if(rs.getInt("CRN") == course.crn){
                    SQLException err = new SQLException();
                    throw err;
                }
            }
            
            String sql = "insert into COURSES values(?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?)";             
            PreparedStatement ps = con.prepareStatement(sql);   
            ps.setString(1, currentSemester);   
            ps.setInt   (2, course.crn);
            ps.setString(3, course.department);
            ps.setString(4, course.courseNum);
            ps.setString(5, course.name);   
            ps.setInt   (6, course.m_enroll);   
            ps.setInt   (7, course.enroll);   
            ps.setInt   (8, course.avail);   
            ps.setInt   (9, course.waitList);
            ps.setString(10, course.days);             
            ps.setString(11, course.sTime);             
            ps.setString(12, course.eTime);
            ps.setString(13, course.building);      
            ps.setString(14, course.classroom);         
            ps.setString(15, course.prof);     
            ps.executeUpdate();
                
            con.close();
        }
        catch(SQLException err){
            System.out.println( "Error inputing Course! Course may already exist!");
            err.printStackTrace();           
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
            
            //remove course from COURSES
            String sql = "delete from COURSES where CRN = " + course.crn +" and SEMESTER = '"+currentSemester+"'";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.executeUpdate();
            
            //remove course from professors teaching it
            for (int i = 1; i < 9; i++){
                String sql2 = "update PROFESSORS set COURSE_"+ i +" = "+null+" "
                        + "where COURSE_"+ i +" = '"+ course.name +"' and  SEMESTER = '"+currentSemester+"'";
                PreparedStatement ps2 = con.prepareStatement(sql2);
                ps2.executeUpdate();
            }
            con.close(); 
        }
        catch(SQLException err){
            System.out.println( "Error removing Course!");
            err.printStackTrace();           
        }
    }
    
    /**DONE
     * @param course 
     */
    public void alterCourse(Course course)
    {
        try{
            Connection con = DriverManager.getConnection(host, username, password);
            
            String sql =  "update COURSES set values(?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?)"
                        + "where CRN = " + course.crn +" and SEMESTER = '"+currentSemester+"'";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, currentSemester);
            ps.setInt   (2, course.crn);
            ps.setString(5, course.department);
            ps.setString(3, course.courseNum);
            ps.setString(4, course.name);
            ps.setInt   (6, course.m_enroll);
            ps.setInt   (7, course.enroll);
            ps.setInt   (8, course.avail);
            ps.setInt   (9, course.waitList);
            ps.setString(10, course.days);
            ps.setString(11, course.sTime);
            ps.setString(12, course.eTime);
            ps.setString(13, course.building);
            ps.setString(14, course.classroom);
            ps.setString(15, course.prof);
            ps.executeUpdate();
 
            con.close(); 
        }
        catch(SQLException err){
            System.out.println( "Error altering Course!");
            err.printStackTrace();           
        }    
    }
    
    /**NOT DONE YET - need functionality to select a constraint restricted set
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
            
            String sql = "Select * from CLASSROOM where SEMESTER ='"+ currentSemester +"'";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            
            while(rs.next()){
                Classroom classroom = new Classroom();
                classroom.setRoomNum(rs.getString("ROOM_NUM"));
                classroom.setmEnroll(rs.getInt("M_ENROLL"));
                classroom.setBuildingName(rs.getString("BUILDING"));
                classrooms.add(classroom);
            }
            
            con.close();           
        }catch(SQLException err){
            System.out.println( "Error retireving Classrooms!");
            err.printStackTrace();           
        }
        
        return classrooms;
    }
    
    /**DONE
     * 
     * @param classroom 
     */
    public void addClassroom(Classroom classroom)
    {
        //do we need this?
        try{
            Connection con = DriverManager.getConnection(host, username, password);
        
            //manual dup checking
            String sqlCheck = "select * from CLASSROOMS where SEMESTER = '"+currentSemester+"'";
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
            ps.setString(1, currentSemester);
            ps.setString(2, classroom.getRoomNum());
            ps.setString(3, classroom.getBuildingName());
            ps.setInt   (4, classroom.getmEnroll());
            ps.executeUpdate();

            con.close();
        }catch(SQLException err){
            System.out.println( "Error inputing Classroom! Classroom may already exist!");
            err.printStackTrace();           
        }
    }
    
    /**DONE
     * 
     * @param classroom 
     */
    public void removeClassroom(Classroom classroom)
    {
        try{
            Connection con = DriverManager.getConnection(host, username, password);

            //remove from CLASSROOMS
            String sql = "delete from CLASSROOMS where ROOM_NUM = '" + classroom.getRoomNum() +"' and SEMESTER = '"+currentSemester+"'";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.executeUpdate();

            //remove from COURSES
            String sql2 = "update COURSES set CLASSROOM = "+null+" where CLASSROOM = '"+ classroom.getRoomNum() +"' and SEMESTER = '"+currentSemester+"'";
            PreparedStatement ps2 = con.prepareStatement(sql2);
            ps2.executeUpdate();
            
            con.close();
        }catch(SQLException err){
            System.out.println( "Error deleting Classroom!");
            err.printStackTrace();           
        }    
    }
    
    /**DONE
     * 
     * @param classroom 
     */
    public void alterClassroom(Classroom classroom)
    {
        try{
            Connection con = DriverManager.getConnection(host, username, password);

            String sql = "update CLASSROOMS values(?,?,?,?) where ROOM_NUM = " 
                    + classroom.getRoomNum() + " and SEMESTER = '"+currentSemester+"'";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, currentSemester);
            ps.setString(2, classroom.getRoomNum());
            ps.setString(3, classroom.getBuildingName());
            ps.setInt   (4, classroom.getmEnroll());
            ps.executeUpdate();

            con.close();

            }catch(SQLException err){
                System.out.println( "Error altering Classroom!");
                err.printStackTrace();           
        }          
    }
    
    /**DEPRECATED DO NOT USE - handle adding courses via alterProfessor
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

    /**DEPRECATED DO NOT USE - handle adding courses via alterProfessor
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
    
    /** DONE 
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
            
            String sql4 = "delete from USER_SCHEDULES";
            ps = con.prepareStatement(sql4);
            ps.executeUpdate();
            
            con.close();
        }catch(SQLException err){
            System.out.println("Error clearing Database");
            //err.printStackTrace();              
        }
    }
}
