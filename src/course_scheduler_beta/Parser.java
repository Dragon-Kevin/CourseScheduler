/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package course_scheduler_beta;

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
    private List<Classroom> availableRooms = new ArrayList();
    private String department;
    private String semester;
    private String building;
    private int[] classDuration = new int[2];  // {dur;gap}
    private DatabaseUtility db;
    
    public Parser(DatabaseUtility db, File file){
        this.db = db;
        LinesOfFile = readFile(file);
        initialData(LinesOfFile);
        findCourses(LinesOfFile);
        findClassroomPreferences(LinesOfFile);
        assignPreferences(courses, preferences);
        findFacultyAssignments(LinesOfFile);
        assignDuration(LinesOfFile);
        findAvailableRooms(LinesOfFile);
        assignTeacherToCourse();
        storeData();
        //printList(availableRooms);
    }    
    
    // Takes file path as an argument then tries to read the file. Returns the file as list with each line as an individual element
    private List readFile(File file) {
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
    
    private void initialData(List list) {
        String[] tokens = list.get(0).toString().split(": ");
        if(tokens.length > 0 && tokens[0].equalsIgnoreCase("Department")){
            setDepartment(afterColon(list, 0));
        }
        tokens = list.get(1).toString().split(": ");
        if(tokens.length > 0 && tokens[0].equalsIgnoreCase("Location")) {
            setBuilding(afterColon(list, 1));
        }
        tokens = list.get(2).toString().split(": ");
        if(tokens.length > 0 && tokens[0].equalsIgnoreCase("Semester")){
            setSemester(afterColon(list, 2));
        }
        db.setCurrentSemester(semester);
        //System.out.println(department + " " + semester + " " + building);
    }
    
    private String afterColon(List list, int index){
        return list.get(index).toString().split(": ")[1];
    }
    
    // finds and stores all of the courses found in the file then returns a list containing all of the courses
    private void findCourses(List list) {        
        // finds the location of the courses in the file
        int index = isObjectInList(list, "Courses Offered:") + 1;
        
        // starting from the location, loop until it reaches the end point (classroom preferences)
        for(; !list.get(index).equals("Classroom Preferences:"); index++) {
            String[] tokens = list.get(index).toString().split(", ");
            courses.add(new Course(Integer.parseInt(tokens[0]), tokens[1], getBuilding(), getDepartment()));
        }
    }
    
     // finds and stores all of the courses with classroom restrictions and their room/building preferences in a list
    private void findClassroomPreferences(List list) {       
        // finds location of preferences in the file
        int index = isObjectInList(list, "Classroom Preferences:") + 1;
        
        //System.out.println("*** " + list.get(index).toString());
        // store all preferences
        for(; !list.get(index).equals("Faculty Assignments:"); index++) {
            String[] tokens = list.get(index).toString().split("\n");
            //System.out.println("*** " + tokens[0]);
            String[] subtokens = tokens[0].split(", room | must be taught in ");

            preferences.addAll(Arrays.asList(subtokens));
        }
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
    
    // finds and stores all faculty assignments along with time preferences in a list
    private void findFacultyAssignments(List list) {        
        // finds location of assignments in the file
        int index = isObjectInList(list, "Faculty Assignments:") + 1;
        
        // store all assignments
        for(; !list.get(index).equals("Class Length:"); index++) {
            String[] tokens = list.get(index).toString().split("\n");
            String[] subtokens = tokens[0].split(", | - ");
            teachers.add(new Teacher(subtokens[0], subtokens[1], subtokens[2], subtokens[3], subtokens[4], subtokens[5]));
        }
    }
    
    private void assignDuration(List list){
        int index = isObjectInList(list, "Class Length:") + 1;
        
        getClassDuration()[0] = Integer.parseInt(list.get(index).toString().split(" ")[1]);
        getClassDuration()[1] = Integer.parseInt(list.get(++index).toString().split(" ")[1]);
    }
    
    private void findAvailableRooms(List list){
        int index = isObjectInList(list, "Classrooms Available:") + 1;
        
        String[] tokens = list.get(index).toString().split(", ");
        for(String subtok: tokens){
            availableRooms.add(new Classroom(subtok));
        }
        //getAvailableRooms().addAll(Arrays.asList(tokens));
    }
    
    private void assignTeacherToCourse(){
        for(Teacher t:teachers){
            for(Course c:courses){
                //String[] temp = t.getTeachableCourses();
                for(String x:t.getTeachableCourses()){
                    if(c.getName().equalsIgnoreCase(x)){
                        c.setProf_(t);
                    }
                }
            }
        }
    }
    
    private void storeData(){
        db.clearDatabase(semester);
        db.setDuration(classDuration);
        //System.out.println(db.getDuration()[0]);
        teachers.stream().forEach((t) -> {
            //System.out.println(t);
            db.addNewProfessor(t);
        });
        courses.stream().forEach((c) -> {
            db.addNewCourse(c);
           //System.out.println(c);
        });
        availableRooms.stream().forEach((r) -> {
            db.addClassroom(r);
        });
    }
    
    // Prints out all elements of a List
    public static void printList(List list){
        System.out.println("Printing List: ");
        list.stream().forEach((ele) -> {
            System.out.println(ele);
        });
    }
    
    public void storeList(List list, int x){
        if(x == 0){
            for(Teacher ele: (List<Teacher>)list) {
                db.addNewProfessor(ele);
            }
        }else if (x == 1){
            for(Course ele: (List<Course>)list) {
                db.addNewCourse(ele);
            }
        }else if (x == 2){
            for( Classroom ele: (List<Classroom>)list) {
                db.addClassroom(ele);
            }
        }else 
            System.out.println("Error Storing List");
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

    /**
     * @return the availableRooms
     */
    public List getAvailableRooms() {
        return availableRooms;
    }

    /**
     * @param availableRooms the availableRooms to set
     */
    public void setAvailableRooms(List availableRooms) {
        this.availableRooms = availableRooms;
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
     * @return the classDuration
     */
    public int[] getClassDuration() {
        return classDuration;
    }

    /**
     * @param classDuration the classDuration to set
     */
    public void setClassDuration(int[] classDuration) {
        this.classDuration = classDuration;
    }
}
