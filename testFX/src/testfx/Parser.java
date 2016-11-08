/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testfx;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Myk
 */
public class Parser {
    private List preferences = new ArrayList();
    private List<Course> courses = new ArrayList();
    private List<Teacher> teachers = new ArrayList();
    private List LinesOfFile = new ArrayList();
    
    /*public Parser(){
        String file = "src/testfx/Dept1ClassData.csv";
        
        LinesOfFile = readFile(file);
        courses     = findCourses(LinesOfFile);
        preferences = findClassroomPreferences(LinesOfFile);
        assignPreferences(courses, preferences);
        teachers = findFacultyAssignments(LinesOfFile);
        
        printList(courses);
    }*/
    public Parser(File file){
        LinesOfFile = readFile(file);
        courses     = findCourses(LinesOfFile);
        preferences = findClassroomPreferences(LinesOfFile);
        assignPreferences(courses, preferences);
        teachers = findFacultyAssignments(LinesOfFile);
        
        printList(courses);
    }    
    
    // finds and stores all faculty assignments along with time preferences in a list
    public static List findFacultyAssignments(List list) {
        List<Teacher> teachers = new ArrayList();
        int index;
        
        // finds location of assignments in the file
        index = isObjectInList(list, "Faculty Assignments:") + 1;
        
        // store all assignments
        for(; index < list.size(); index++) {
            String[] tokens = list.get(index).toString().split("\n");
            String[] subtokens = tokens[0].split(", | - ");

            teachers.add(new Teacher(subtokens[0], subtokens[1], subtokens[2], subtokens[3], subtokens[4]));
        }
        
        return teachers;
    }
    
    // assigns building and class restrictions to courses
    public static void assignPreferences(List<Course> courses, List<String> prefs) {
        // loop through preferences
        for(int i = 0; i < prefs.size(); i++) {
            // find the course to apply restrictions to.
            for(Course ele: courses) {
                if (prefs.get(i).equals(ele.getName())) {
                    ele.setBuilding(prefs.get(++i));
                    ele.setClassroom(prefs.get(++i));
                }
            }
        }       
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
            String[] subtokens = tokens[0].split(", room | must be taught in ");

            preferences.addAll(Arrays.asList(subtokens));
        }

        return preferences;
    }
    
    // finds and stores all of the courses found in the file then returns a list containing all of the courses
    public static List findCourses(List list) {
        List<Course> courses = new ArrayList();
        int index;
        
        // finds the location of the courses in the file
        index = isObjectInList(list, "Courses Offered") + 1;
        System.out.println(index);
        // starting from the location, loop until it reaches the end point (classroom preferences)
        for(; !list.get(index).equals("Classroom Preferences:"); index++) {
            String[] tokens = list.get(index).toString().split(", ");
            
            // creates class object and adds the values
            for (String token : tokens) {
                courses.add(new Course(token));
            }
        }
        
        return courses;
    }
    
    // Takes file path as an argument then tries to read the file. Returns the file as list with each line as an individual element
    public static List readFile(File file) {
        String line;                        // temporary storage for each line when read in (file is read line by line)
        List lines = new ArrayList();       // store file (will store all lines of the file in one variable)
        
        // file reader
        try {
            FileReader fileReader = new FileReader(file);
            // read a line, if it is not null add to list element.
            try (BufferedReader bufferedReader = new BufferedReader(fileReader)) {
                // read a line, if it is not null add to list element.
                while((line = bufferedReader.readLine()) != null) {
                    if(line.isEmpty() || line.trim().equals("\n") || line.trim().equals("")) {
                        // skip empty line
                    }
                    else {
                        lines.add(line.trim());
                    }
                }
            }
        }
        // exception handling
        catch(FileNotFoundException ex) {
            System.out.println("Unable to open file '" + file.getName() + "'");
        }
        catch(IOException ex) {
        }
        
        // return the file (the list of lines of the file)
        return lines;
    }
    
    // Prints out all elements of a List
    public static void printList(List list){
        list.stream().forEach((ele) -> {
            System.out.println(ele);
        });
    }
    
    // Searches a list for an element, returns index if found, -1 if not
    public static int isObjectInList(List list, Object target){
        for(int i = 0; i < list.size(); i++) {
            if (list.get(i).equals(target)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * @return the preferences
     */
    public List getPreferences() {
        return preferences;
    }

    /**
     * @param preferences the preferences to set
     */
    public void setPreferences(List preferences) {
        this.preferences = preferences;
    }

    /**
     * @return the courses
     */
    public List<Course> getCourses() {
        return courses;
    }

    /**
     * @param courses the courses to set
     */
    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    /**
     * @return the teachers
     */
    public List<Teacher> getTeachers() {
        return teachers;
    }

    /**
     * @param teachers the teachers to set
     */
    public void setTeachers(List<Teacher> teachers) {
        this.teachers = teachers;
    }

    /**
     * @return the LinesOfFile
     */
    public List getLinesOfFile() {
        return LinesOfFile;
    }

    /**
     * @param LinesOfFile the LinesOfFile to set
     */
    public void setLinesOfFile(List LinesOfFile) {
        this.LinesOfFile = LinesOfFile;
    }
}
