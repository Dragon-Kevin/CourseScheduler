/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package course_scheduler;
import java.util.*;

/**
 *
 * @author Myk
 */
public class Teacher {
    String name;
    Course[] teachableCourses = new Course[3];
    String timePreference;
    static int id;                 //teacher id represents teacher in db
    int courseLoad;         //holds num of course a teacher has, used in db
    
    public void test() {
        System.out.println("test");
    }
}
