/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package course_scheduler;
import java.io.*;
import java.util.*;
/**
 *
 * @author Myk
 */
public class Course_Scheduler {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        List courses, preferences, assignments = new ArrayList();
        String fileName1 = "src/course_scheduler/Dept1ClassData.csv";
        String fileName2 = "src/course_scheduler/Dept2ClassData.csv";
        List LinesOfFile = new ArrayList();
        
        LinesOfFile = readFile(fileName1);
        courses     = findCourses(LinesOfFile);
        preferences = findClassroomPreferences(LinesOfFile);
        assignments = findFacultyAssignments(LinesOfFile);
    }
    
    // finds and stores all faculty assignments along with time preferences in a list
    public static List findFacultyAssignments(List list) {
        List assignments = new ArrayList();
        int index;
        
        // finds location of assignments in the file
        index = isObjectInList(list, "Faculty Assignments:") + 1;
        
        // store all assignments
        for(; index < list.size(); index++) {
            String[] tokens = list.get(index).toString().split("\n");
            assignments.addAll(Arrays.asList(tokens));
        }
        
        //printList(assignments);
        //System.out.println("number of assignments = " + assignments.size());
        
        return assignments;
    }
    
    // finds and stores all of the courses with classroom restrictions and their room/building preferences in a list
    public static List findClassroomPreferences(List list) {
        List preferences = new ArrayList();
        int index;
        
        // finds location of preferences in the file
        index = isObjectInList(list, "Classroom Preferences:") + 1;
        
        // store all preferences
        for(; !list.get(index).equals("Faculty Assignments:"); index++) {
            String[] tokens = list.get(index).toString().split("\n");
            preferences.addAll(Arrays.asList(tokens));
        }
        
        //printList(preferences);
        //System.out.println("number of preferences = " + preferences.size());
        
        return preferences;
    }
    
    // finds and stores all of the courses found in the file then returns a list containing all of the courses
    public static List findCourses(List list) {
        List courses = new ArrayList();
        int index;
        
        // finds the location of the courses in the file
        index = isObjectInList(list, "Courses Offered") + 1;
        
        // starting from the location, loop until it reaches the end point (classroom preferences)
        for(; !list.get(index).equals("Classroom Preferences:"); index++) {
            String[] tokens = list.get(index).toString().split(", ");
            courses.addAll(Arrays.asList(tokens));
        }
        
        //printList(courses);
        //System.out.println("number of courses = " + courses.size());
        
        return courses;
    }
    
    // Takes file path as an argument then tries to read the file. Returns the file as list with each line as an individual element
    public static List readFile(String fileName) {
        String line;                        // temporary storage for each line when read in (file is read line by line)
        List lines = new ArrayList();       // store file (will store all lines of the file in one variable)
        
        // file reader
        try {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            
            // read a line, if it is not null add to list element.
            while((line = bufferedReader.readLine()) != null) {
                if(line.isEmpty() || line.trim().equals("\n") || line.trim().equals("")) {
                    // skip empty line
                }
                else {
                    lines.add(line.trim());
                }
            }
            
            bufferedReader.close();
        }
        // exception handling
        catch(FileNotFoundException ex) {
            System.out.println("Unable to open file '" + fileName + "'");
        }
        catch(IOException ex) {
            ex.printStackTrace();
        }
        
        // return the file (the list of lines of the file)
        return lines;
    }
    
    // Prints out all elements of a List
    public static void printList(List list){
        for(Object ele : list) {
            System.out.println(ele);
        }
    }
    
    // Searches a list for an element, returns index if found, -1 if not
    public static int isObjectInList(List list, Object target){
        /*for(Object ele : list) {
            if(ele.equals(target)) {
                System.out.println("ding");
                return true;
            }
        }*/
        for(int i = 0; i < list.size(); i++) {
            if (list.get(i).equals(target)) {
                //System.out.println(list.get(i));
                return i;
            }
        }
        return -1;
    }
}
