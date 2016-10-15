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
    String name, time, department, building, classroom;
    int enrollment;
    
    int crn;//aj additions
    int courseNum;
    int length;
    String prof;
    
    //empty 
    public Course(){
        name = "";
        time = "";
        department = "";
        building = "";
        classroom = "";
        enrollment = -1;
        crn = -1;
        courseNum = -1;
        prof = "";
        length = 0; 
    }
    
    public Course(String name) {
        this.name = name;
        this.department = name.split(" ")[0];
        
        //aj additions
        //name = "";
        time = "";
        //department = "";
        building = "";
        classroom = "";
        enrollment = -1;
        crn = -1;
        courseNum = -1;
        prof = "";
        length = 0;
    }
    
    public void test() {
        System.out.println("test");
    }
    
    @Override
    public String toString () {
        return "department = " + department + "; time = " + time + "; building = " + building + "; classroom = " + classroom + "; name = " + name;
    }
}
