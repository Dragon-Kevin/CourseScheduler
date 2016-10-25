/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package course_scheduler;
import java.io.*;
import java.util.*;
import View.*;
/**
 *
 * @author Myk
 */
public class Course_Scheduler {

    private static DatabaseUtility db = new DatabaseUtility();
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        /** This makes the MainMenu open on running the program. 
         *  EditMenu is just me messing around with the edit menu for
         *  editing teacher.
        
        MainMenu my = new MainMenu();
        //EditMenu my2 = new EditMenu();
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                my.setVisible(true);
                //my2.setVisible(true);
            }
        }); */
                
        //!!! - FOR ALL DATABASE STUFF,
        //make sure you go to libraries
        //and right-click ADD JAR/folder
        //and add derbyclient.jar
        //otherwise the database will not appear to work
       
        //AJ DB STUFF - FOR TESTING  
        //Course testCourse = new Course("Senior Design");
        //Course testCourse2 = new Course("Data Structures");
        //Course testCourse3 = new Course("OO Design in C++");
        //Course testCourse4 = new Course("Assembly");
        //Course testCourse5 = new Course("Programming Languages");
        //
        //Teacher prof = new Teacher("","","","","");       
        //prof.name = "Dr. Coleman";
        //prof.id = 12345;
        //prof.timePreference = "None";
        //prof.courseLoad = 0;
        //
        //Teacher prof2 = new Teacher("","","","","");
        //prof2.id = 54321;
        //prof2.name = "Dr. Newman";
        ///prof2.timePreference = "None";
        //
        //Teacher prof3 = new Teacher("","","","","");
        //prof3.id = 99999;
        //prof3.name = "Dr. Rowaboat";
        //prof3.timePreference = "Sometimes";
        //
        //Course fullCourse = new Course("Senior Design");
        //fullCourse.crn = 22352;
        //fullCourse.classroom = "326";
        //fullCourse.name = ;
        //fullCourse.courseNum = 499;
        //fullCourse.building = "Tech Hall";
        //fullCourse.department = "Computer Science";
        //fullCourse.enrollment = 20;
        //fullCourse.length = 80;
        //fullCourse.time = "5:30 PM - 6:50 PM";
        //fullCourse.prof = "Dr. Coleman";
        
        //db.addNewProfessor(prof);
        //db.addNewProfessor(prof2);
        //db.addNewProfessor(prof3);
        //db.removeProfessor(prof);
        //prof.name = "Dr. Newman";
        //prof.timePreference= "Afternoon";
        //db.alterProfessor(prof);
        
        //db.addNewCourse(fullCourse); 
        //db.removeCourse(fullCourse);
        //fullCourse.courseNum = 500;
        //db.alterCourse(fullCourse);
        
        //db.assignCoursetoProf(prof, testCourse);
        //db.assignCoursetoProf(prof, testCourse2);
        //db.assignCoursetoProf(prof, testCourse3);
        //db.assignCoursetoProf(prof, testCourse4);
        //db.assignCoursetoProf(prof, testCourse5);
        //db.removeCoursefromProf(prof, fullCourse);
        //db.removeCoursefromProf(prof, testCourse2);
        //db.removeCoursefromProf(prof, testCourse3);

        //String roomNum = "326";
        //int enroll = 20;
        //int m_enroll = 40;
        //String building = "Tech Hall";
        //db.addClassroom(roomNum, enroll, m_enroll, building);
        //db.removeClassroom(roomNum);
        //db.alterClassroom(201, 22, 30, "Tech Hall");
        
        //String name = "";
        //db.createScheduleTable(name);
        //db.deleteScheduleTable("SCHEDULE_1");
        db.clearDatabase();
        //END AJ DB STUFF
        
        
        List preferences, assignments = new ArrayList();
        List<Course> courses = new ArrayList();
        List<Teacher> teachers = new ArrayList();
        String fileName1 = "src/course_scheduler/Dept1ClassData.csv";
        String fileName2 = "src/course_scheduler/Dept2ClassData.csv";
        List LinesOfFile = new ArrayList();
        
        LinesOfFile = readFile(fileName1);
        courses     = findCourses(LinesOfFile);
        preferences = findClassroomPreferences(LinesOfFile);
        assignPreferences(courses, preferences);
        teachers = findFacultyAssignments(LinesOfFile);
        
        
        //AJ RETRIEVAL TESTING
        //storeCourses(courses);        //store into DB
        //storeProfessors(teachers);    //store into DB
    /*    
        List<Course> dbCourses = db.getCourses("");
        Iterator itr = dbCourses.iterator();
        Course c;
        while(itr.hasNext())
        {
            c = (Course)itr.next();
            System.out.println(c.name);
        }
      
        List<Teacher> dbTeachers = db.getProfessors("");
        Iterator itr2 = dbTeachers.iterator();
        Teacher t;
        while(itr2.hasNext()){
            t = (Teacher)itr2.next();
            System.out.println(t.id);
            System.out.println(t.name);
            System.out.println(t.timePreference);
        }
    */  //END RETRIEVAL TESTING
        
        //printList(courses);
    }
    
    /**
     * @author AJ
     * @param teachers 
     */
    public static void storeProfessors(List<Teacher> teachers)
    {
        Teacher teacher;
        int id = 1;//tempCode
        Iterator itr = teachers.iterator();
        while(itr.hasNext())
        {
            teacher = (Teacher)itr.next();
            teacher.id = id;//tempCode
            db.addNewProfessor(teacher);
            id++;//tempCode
        }
    }
    
    /**
     * @author AJ
     * @param courses 
     */
    public static void storeCourses(List<Course> courses)
    {
        Course course;
        int crn = 1;//tempCode
        Iterator itr = courses.iterator();
        while(itr.hasNext())
        {
            course = (Course)itr.next();
            course.crn = crn;//tempCode 
            db.addNewCourse(course);
            crn++;//tempCode
        }
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
    
    public static void assignPreferences(List<Course> courses, List<String> prefs) {
        for(int i = 0; i < prefs.size(); i++) {
            System.out.println(prefs.get(i));
            for(Course ele: courses) {
                if (prefs.get(i).equals(ele.name)) {
                    ele.building = prefs.get(++i);
                    ele.classroom = prefs.get(++i);
                    //System.out.println(ele);
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
        
        // starting from the location, loop until it reaches the end point (classroom preferences)
        for(; !list.get(index).equals("Classroom Preferences:"); index++) {
            String[] tokens = list.get(index).toString().split(", ");
            
            // creates class object and adds the values
            for (int i = 0; i < tokens.length; i++) {
                courses.add(new Course(tokens[i]));
            }
        }
        
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
        for(int i = 0; i < list.size(); i++) {
            if (list.get(i).equals(target)) {
                return i;
            }
        }
        return -1;
    }
}
