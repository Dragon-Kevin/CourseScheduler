/*
 * 
 */
package course_scheduler;

import java.util.*;
import java.sql.*;

/**
 *
 * @author AJ
 */
public class Database {
    
    private String host;        //local host for db
    private String username;    //access un
    private String password;    //access pw
    
    /*
        Basic Constructor
    */
    public Database(){
        //the host is defualt, un and pw are set and I don't
        //know how to change them without starting a new db
        host = "jdbc:derby://localhost:1527/SchoolData";    
        username = "user1";
        password = "123456";
    }        
    
    /*
        done
    */
    public void addNewProfessor(Teacher prof){
        try{
            Connection con = DriverManager.getConnection(host, username, password);
        
            String sqlCheck = "select ID from PROFESSORS where ID = " + prof.id;
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sqlCheck);
            System.out.println("inserting...");
            
            if(rs.next()){
                //found?
                System.out.println("hit");
            }
            else{
                //create an entry in PROFESSORS
                
                String sql = "insert into PROFESSORS values(?,?,?)";
                PreparedStatement ps = con.prepareStatement(sql);           
                ps.setInt   (1, prof.id);
                ps.setString(2, prof.name);
                ps.setString(3, prof.timePreference);
                ps.executeUpdate();

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
            }
            con.close();         
        }
        catch(SQLException err){
            System.out.println( "Error inputing Professor!");
            err.printStackTrace();
        } 
    }
    
    /*
        done
    */
    public void removeProfessor(Teacher prof)
    {
        try{
            Connection con = DriverManager.getConnection(host, username, password);
            
            String sqlCheck = "select ID from PROFESSORS where ID = " + prof.id;
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sqlCheck);
            System.out.printf("deserting...");
            
            if(rs.next())
            {       
                String sql = "delete from PROFESSORS where ID = "+ prof.id;
                PreparedStatement ps = con.prepareStatement(sql);
                ps.executeUpdate();

                String sql2 = "delete from PROF_COURSES where PROF_ID = "+ prof.id;
                PreparedStatement ps2 = con.prepareStatement(sql2);
                ps2.executeUpdate();

                String sql3 = "update COURSES set PROFESSOR = '' where PROFESSOR = '"+ prof.name +"'";
                PreparedStatement ps3 = con.prepareStatement(sql3);
                ps3.executeUpdate();
            }
            con.close();
        }
        catch(SQLException err){
            System.out.println( "Error deleting Professor!");
            err.printStackTrace(); 
        }
    }
    /*
        pass in prof that is assumed to already exist in db and has already
        been updated with correct values
        ID SHOULD NEVER CHANGE ONCE USED, now set to static in class
        done
    */
    public void alterProfessor(Teacher prof)
    {
        //alter with update 
        try{
            Connection con = DriverManager.getConnection(host, username, password);

            String sqlCheck = "select * from PROFESSORS where ID = " + prof.id;
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sqlCheck);
            System.out.println("altering...");
            
            if(rs.next())//if found
            {   
                //alter PROFESSORS
                String sql = "update PROFESSORS set PROFESSORNAME = ?, TIME_PREFERENCE = ?";           //alter
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setString(1, prof.name);
                ps.setString(2  , prof.timePreference);
                ps.executeUpdate();
            }
            
            con.close();
        }catch(SQLException err){
            System.out.println( "Error altering Professor!");
            err.printStackTrace(); 
        }
                
    }
    /*
    done?
    */
    public void addNewCourse(Course course){
        try{
            Connection con = DriverManager.getConnection(host, username, password);
            
            String sql = "insert into COURSES values(?,?,?,?,?,?,?,?,?,?)";
            
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, course.crn);
            ps.setInt(2, course.courseNum);
            ps.setString(3, course.name);    //name
            ps.setString(4, "");    //depart
            ps.setString(5, "");    //class
            ps.setString(6, "");    //building
            ps.setInt(7, 0);        //enroll
            ps.setString(8, "");    //time
            ps.setInt(9, 0);        //length
            ps.setString(10, "");   //prof
            ps.executeUpdate();
            
            con.close();
            
        }
        catch(SQLException err){
            System.out.println( "Error inputing Course!");
            err.printStackTrace();           
        }
    }
    
    /*
    
    */
    public void removeCourse(Course course)
    {
        
    }
    
    /*
    
    */
    public void alterCourse(Course course)
    {
        
    }
    
    public void addClassroom()
    {
        //do we need this?
    }
        
    /*
    //requiring there to be a course and professor to exist already
    //before they are connected   
    done?
    */
    public void assignCoursetoProf(Teacher prof, Course course){
        prof.courseLoad++;

        if(prof.courseLoad > 5){    //db blows up
            Connection con = null;
            try{  
                con = DriverManager.getConnection(host, username, password);

                String sqlCheck = "select PROF_ID from PROF_COURSES where PROF_ID="+prof.id;
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(sqlCheck);

                while(rs.next()){
                    if(rs.getInt("PROF_ID") == prof.id){
                        //found    
                        String sql = "update PROF_COURSES set COURSE_"+prof.courseLoad+    //the next blank course column
                                     " = '"+course.name+"' where PROF_ID = "    //store name there
                                     +prof.id;                                  //where the this is the prof 

                         PreparedStatement ps = con.prepareStatement(sql);
                         ps.executeUpdate();
                    }
                    else{
                        //notfound
                        System.out.print("prof not registered");
                    }
                }
                con.close();
            }catch(SQLException err){
                System.out.println("Error");
                err.printStackTrace();  
            }
        }
        else
            System.out.println("Professor at max courses");
            
    }

    /*
    
    */
    public void removeCoursefromProf(Teacher prof, Course course)
    {
        //dec prof.courseLoad
        try{
            Connection con = DriverManager.getConnection(host, username, password);
            
            //find prof
            String sqlCheck = "select PROF_ID from PROF_COURSES where PROF_ID=" + prof.id;
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sqlCheck);
            
            while(rs.next()){
                if(rs.getInt("PROF_ID") == prof.id){
                    //found
                    String sqlCheck2 = "select * from PROF_COURSES";
                    Statement st2 = con.createStatement();
                    ResultSet rs2 = st.executeQuery(sqlCheck2);
                    
                    //find
                    for(int i = 1; i<6; i++){
                       // rs.getString
                    }
                }
                else
                    System.out.println("Prof not found");
            }
             
        }catch(SQLException err){
            System.out.println("Error Removing Course from Professor1");
            err.printStackTrace();              
        }
    }
}
