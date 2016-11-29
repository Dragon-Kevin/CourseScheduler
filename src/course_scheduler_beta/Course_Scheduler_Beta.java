/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package course_scheduler_beta;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.FadeTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.control.*;
//import javafx.scene.control.Button;
//import javafx.scene.control.Label;
//import javafx.scene.control.TableColumn;
//import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
//import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

/**
 *
 * @author mv0003
 */
public class Course_Scheduler_Beta extends Application {
    private static DatabaseUtility db = new DatabaseUtility();
    private static Parser parser;
    private static CourseScheduler scheduler;
    
    TextArea textArea = new TextArea();
    
    ObservableList<Course> data = FXCollections.observableArrayList(db.getCourses(null, null));
    
    @Override
    public void start(Stage stage) {
        stage.setResizable(false);
        FileChooser fileChooser = new FileChooser();
        
        //base pane for start window
        BorderPane bPane = new BorderPane();
        bPane.setPadding(new Insets(10));
        
        GridPane gPane = new GridPane();
        gPane.setHgap(20);
        gPane.setVgap(10);
        gPane.setPadding(new Insets(10));
        
        Button btnLoadCSV = new Button("Load");
        btnLoadCSV.setMinWidth(100);
        gPane.add(btnLoadCSV, 0, 0);
        
        Button btnOpenCSV = new Button("New"); 
        btnOpenCSV.setMinWidth(100);
        gPane.add(btnOpenCSV, 1, 0);
        
        /* Added by Victoria 11/27/16 */
        Button btnHelpCSV = new Button("Help"); 
        btnHelpCSV.setMinWidth(100);
        gPane.add(btnHelpCSV, 2, 0);
        /* Added by Victoria 11/27/16 */
        
        gPane.setAlignment(Pos.CENTER);
        bPane.setTop(gPane);       
        
        //list of semesters, pulled from database
        ListView semesterList = new ListView();
        semesterList.setPrefHeight(200);
        List<String> s = db.getSemesters();
        ObservableList<String> semesters = FXCollections.observableArrayList(s);
        semesterList.setItems(semesters);
        bPane.setBottom(semesterList);        
        
        btnOpenCSV.setOnAction((ActionEvent event) -> {
            configureFileChooser(fileChooser);
            File file = fileChooser.showOpenDialog(stage);
                      
            if(file != null){
                parser = new Parser(db, file);               //the user checks new if they want a new Schedule, so they want to load a new data file, with a new semester entry
                //System.out.println(db.getDuration()[1]);
                db.addSemester(parser.getSemester(), parser.getClassDuration()[0], parser.getClassDuration()[1]);           //take the new semester entry and add it to the database
                db.setCurrentSemester(parser.getSemester());    //set it as the current semester 
                scheduler = new CourseScheduler(db, textArea);        
                mainWindow();
                stage.close();
            }
        });

        btnLoadCSV.setOnAction((ActionEvent event) -> {
                //String theSemester = semesterList.getSelectionModel().getSelectedItem().toString();     //setting for code simplicity
                if(semesterList.getSelectionModel().getSelectedItem() != null && !semesterList.getSelectionModel().getSelectedItem().toString().isEmpty()){                                      //if the user acutally selected something
                    db.setCurrentSemester(semesterList.getSelectionModel().getSelectedItem().toString());                                                 //set the semester from user choice
                    db.setDuration();
                    scheduler = new CourseScheduler(db, textArea);
                    mainWindow();                                                                       //start the stuff
                    stage.close();
                }//else do nothing, probably need a label to appear to tell user they messed up
        });     
        
        /* Added by Victoria 11/27/16 */
        btnHelpCSV.setOnAction((ActionEvent event) -> {       
            mainHelp();
        });
        /* Added by Victoria 11/27/16 */
        
        Scene scene = new Scene(bPane, 400, 250);
        stage.setTitle("Welcome!");
        stage.setScene(scene);
        stage.show();
    }

    public void mainWindow(){
        Stage stage = new Stage();
        //stage.setResizable(false);
        BorderPane border = new BorderPane();
        border.setPadding(new Insets(20, 20, 20, 20));    // top, right, bottom, left
        TableView table = new TableView();
        
        textArea.setEditable(false);
        textArea.setMaxHeight(100);
        textArea.setMaxWidth(1100);
        textArea.setTranslateX(160);
        
        textArea.appendText("");
        
        border.setBottom(textArea);
        border.setLeft(configureButtons());
        border.setRight(configureTable(table));
        Scene scene = new Scene(border, 1300, 600);
        stage.setTitle("Course Scheduler");
        stage.setScene(scene);
        stage.show();
    }

//AJ CODE///////////////////////////////////////////////
    public void addWindow(){
        Stage stage = new Stage();
        stage.setResizable(false);
      
        //create tabbed view
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        Tab courseTab = new Tab();
        Tab teacherTab = new Tab();
        Tab classroomTab = new Tab();
        courseTab.setText("Course");
        teacherTab.setText("Teacher");
        classroomTab.setText("Classroom");
        tabPane.getTabs().addAll(courseTab, teacherTab, classroomTab);
        
        //set content in course tab
        courseTab.setContent(addCoursePane(stage));        
        //set content in teacher tab
        teacherTab.setContent(addTeacherPane(stage));
        //set content in classroom tab
        classroomTab.setContent(addClassroomPane(stage));
        
        Scene scene = new Scene(tabPane, 600, 450);
        stage.setTitle("Add");
        stage.setScene(scene);
        stage.show();
    }
    
    /* Added by Victoria 11/27/16 */
    public void mainHelp(){
        Stage stage = new Stage();
        stage.setResizable(false);
        
        BorderPane bPane = new BorderPane();
        
        Label inst = new Label("  Course Scheduler Help\n\n  To load a previously generated schedule, select one from the box at the bottom of the main screen and click 'LOAD'. \n\n  To create a new schedule from a data file, select 'NEW' and upload the desired file. ");

        bPane.setTop(inst);

        Scene scene = new Scene(bPane, 750, 200);
        stage.setTitle("Help");
        stage.setScene(scene);
        stage.show();
    }
    
    public void editHelp(){
        Stage stage = new Stage();
        stage.setResizable(false);
        
        BorderPane bPane = new BorderPane();
        
        Label inst = new Label("  Course Scheduler Help Screen\t\n\n  Buttons\n  Add: Click to add a professor or course offering to the list of available courses and professors.  \n\n  Delete: Click to remove a course offering or a professor from the list of available courses and professors. \n\n  Edit: Click to edit a course's name, meeting time, or classroom preferences. \n\n  Print: Click to send the scheduled course to a printable file. \n\n  Help: Click to return to this menu. ");
        bPane.setTop(inst);

        Scene scene = new Scene(bPane, 650, 300);
        stage.setTitle("Help");
        stage.setScene(scene);
        stage.show();
    }
    /* Added by Victoria 11/27/16 */
    
    //used by  Add Window
    private BorderPane addCoursePane(Stage stage)
    {//CREATE COURSE PANE CONTENT
        BorderPane bPane = new BorderPane();
        bPane.setPadding(new Insets(10));
        
        //COURSE PANE TOP
        Label inst = new Label("Add a New Course");
        inst.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        bPane.setTop(inst);
        
        //COURSE PANE LEFT
        GridPane gPane = new GridPane();
        gPane.setHgap(10);
        gPane.setVgap(10);
        gPane.setPadding(new Insets(10));
        
        Label crnLabel = new Label("CRN");
        gPane.add(crnLabel, 0,  0);
        Label depLabel = new Label("Department");
        gPane.add(depLabel, 0,  1);
//        Label numLabel = new Label("Course Number");
//        gPane.add(numLabel, 0,  2);
        Label nameLabel = new Label("Course Name");
        gPane.add(nameLabel, 0, 3);
//        Label m_eLabel = new Label("Max Enrollment");
//        gPane.add(m_eLabel, 0,  4);
//        Label eLabel = new Label("Enrollment");
//        gPane.add(eLabel, 0,    5);
//        Label aLabel = new Label("Available");
//        gPane.add(aLabel, 0,    6);
//        Label w_lLabel = new Label("Wait List");
//        gPane.add(w_lLabel, 0,  7);

        TextField crn_tField = new TextField();
        gPane.add(crn_tField, 1,    0);
        TextField dep_tField = new TextField();
        gPane.add(dep_tField, 1,    1);
//        TextField num_tField = new TextField();
//        gPane.add(num_tField, 1,    2);
        TextField name_tField = new TextField();
        gPane.add(name_tField, 1,   3);
//        TextField me_tField = new TextField();
//        gPane.add(me_tField, 1,     4);
//        TextField e_tField = new TextField();
//        gPane.add(e_tField, 1,      5);
//        TextField a_tField = new TextField();
//        gPane.add(a_tField, 1,      6);
//        TextField wl_tField = new TextField();
//        gPane.add(wl_tField, 1,     7);
//        
        bPane.setLeft(gPane);
        
        //COURSE PANE RIGHT
        GridPane gPane2 = new GridPane();
        gPane2.setHgap(10);
        gPane2.setVgap(10);
        gPane2.setPadding(new Insets(10));
        
        Label availTeachers_label = new Label("Available Teachers:");
        gPane2.add(availTeachers_label, 0, 0);
        
        ListView<String> list = new ListView<String>();
        List<Teacher> t = db.getProfessors(null, null);
        List<String> teacherNames = new ArrayList();
        for(Teacher ele: t){
            teacherNames.add(ele.getName());
            
        }
        ObservableList<String> teachers = FXCollections.observableArrayList(teacherNames);
        list.setItems(teachers);
        gPane2.add(list, 0, 1);
        
        bPane.setRight(gPane2);
        
        //COURSE PANE BOTTOM
        GridPane gPane3 = new GridPane();
        gPane3.setHgap(10);
        gPane3.setVgap(10);
        gPane3.setPadding(new Insets(10));
        
        Button addBtn = new Button("Add Course"); 
        gPane3.add(addBtn, 0, 0);
        
        addBtn.setOnAction((ActionEvent event) -> {
            //take data from text fields, construct course obj
            //check if fields are valid
            Course c = new Course();
            c.setDays("");
            c.setsTime("");
            c.seteTime("");
            c.setClassroom("");
            
            if((crn_tField.getText() != null && !crn_tField.getText().isEmpty()))   //crn
                c.setCrn(Integer.parseInt(crn_tField.getText()));
            if((dep_tField.getText() != null && !dep_tField.getText().isEmpty()))   //department
                c.setDepartment(dep_tField.getText());
//            if((num_tField.getText() != null && !num_tField.getText().isEmpty()))   //Course Number
//                c.setCourseNum(num_tField.getText());
            if((name_tField.getText() != null && !name_tField.getText().isEmpty()))   //course name
                c.setName(name_tField.getText());
//            if((me_tField.getText() != null && !me_tField.getText().isEmpty()))   //max enrollment
//                c.setM_enroll(Integer.parseInt(me_tField.getText()));
//            if((e_tField.getText() != null && !e_tField.getText().isEmpty()))   //enrollment
//                c.setEnroll(Integer.parseInt(e_tField.getText()));
//            if((a_tField.getText() != null && !a_tField.getText().isEmpty()))   //available
//                c.setAvail(Integer.parseInt(a_tField.getText()));
//            if((wl_tField.getText() != null && !wl_tField.getText().isEmpty()))   //wait list
//                c.setWaitList(Integer.parseInt(wl_tField.getText()));
            
            if(!list.getSelectionModel().isEmpty()){//if selection exists
                c.setProf(list.getSelectionModel().getSelectedItem());
            }
                
            //shove in database
            if(!(c.getCrn() == -1)){    //response text only checks crn. it needs to check ALL values. Also, this does not account for the store function
                db.addNewCourse(c);     //just boucing off due to a duplicate crn. So, the user does not know if the store is successful, just whether the
                Label success = new Label("Course Added");  //data they entered by valid
                FadeTransition fader = createFader(success);
                SequentialTransition fade = new SequentialTransition(success,fader);
                gPane3.add(success,1,0);
                fade.play();
                // edit by myk
                updateTable();
                //System.out.println(db.getCurrentSemester());
                stage.close();
            }
            else{
                Label success = new Label("Unable to add Course");
                FadeTransition fader = createFader(success);
                SequentialTransition fade = new SequentialTransition(success,fader);
                gPane3.add(success,1,0);
                fade.play(); 
            }
        });
        bPane.setBottom(gPane3);
        
        return bPane;
    }

    //used by Add Window
    private BorderPane addTeacherPane(Stage stage)
    {
        BorderPane bPane = new BorderPane();
        bPane.setPadding(new Insets(10));
        
        //TEACHER PANE TOP
        Label inst = new Label("Add a New Teacher");
        inst.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        bPane.setTop(inst);
        
        //TEACHER PANE LEFT
        GridPane gPane = new GridPane();
        gPane.setHgap(10);
        gPane.setVgap(10);
        gPane.setPadding(new Insets(10));
        
        //text boxes
        Label id_Label = new Label("Professor ID");
        gPane.add(id_Label, 0,  0);
        Label name_Label = new Label("Name");
        gPane.add(name_Label, 0,  1);

        TextField id_tField = new TextField();
        gPane.add(id_tField, 1,    0);
        TextField name_tField = new TextField();
        gPane.add(name_tField, 1,    1);
        
        //radio buttons
        Label timePrefLabel = new Label("Time Preference:");
        gPane.add(timePrefLabel, 0,3);

        ObservableList<String> options = FXCollections.observableArrayList("None", "Morning", "Afternoon", "Evening", "MW", "TR");
        ComboBox comboBox = new ComboBox(options);
        comboBox.getSelectionModel().selectFirst();        
        gPane.add(comboBox, 1, 3);

        bPane.setLeft(gPane);
        
        //TEACHER PANE RIGHT
        GridPane gPane2 = new GridPane();
        gPane2.setHgap(10);
        gPane2.setVgap(10);
        gPane2.setPadding(new Insets(10));
        
        //list of courses to be assigned to teacher
        Label coursesToTeach_label = new Label("Courses Assigned");
        gPane2.add(coursesToTeach_label, 0,0);   
        
        ListView<String> list = new ListView();
        ObservableList<String> coursesToTeach = FXCollections.observableArrayList();
        list.setItems(coursesToTeach);
        list.setPrefWidth(150);
        list.getSelectionModel().select(null);
        gPane2.add(list, 0,1);
        
        //list of courses available to be taught
        Label coursesAvail_label = new Label("Courses Available");
        gPane2.add(coursesAvail_label, 1,0); 
        
        ListView<String> list2 = new ListView();
        
        List<Course> c = db.getCourses("PROFESSOR", "TBA");      //list of courses without teachers
        
        List<String> c_names = new ArrayList();
        for(Course ele: c){
            c_names.add(ele.getName());
        }
        
        ObservableList<String> coursesAvail = FXCollections.observableArrayList(c_names);
        list2.setItems(coursesAvail);
        list2.setPrefWidth(150);
        list2.getSelectionModel().select(null);
        gPane2.add(list2, 1,1);
        
        //remove course button and action
        Button removeBtn = new Button("Remove ->");
        removeBtn.setMinWidth(150);
        gPane2.add(removeBtn, 0, 5);
        
        removeBtn.setOnAction((ActionEvent event) -> {
            if(list.getSelectionModel().getSelectedItem() != null && !list.getSelectionModel().getSelectedItem().isEmpty())
            {
            String removed = list.getSelectionModel().getSelectedItem();
            if((!removed.equals(null) && !removed.isEmpty())){
                coursesToTeach.remove(removed);
                coursesAvail.add(removed);
                list.getSelectionModel().select(null);
            }
            }
        });
        
        
        Button addCourseBtn = new Button("<- Add");
        gPane2.add(addCourseBtn, 1, 5);
        addCourseBtn.setMinWidth(150);
        
        addCourseBtn.setOnAction((ActionEvent event) -> {
            if(list2.getSelectionModel().getSelectedItem() != null && !list2.getSelectionModel().getSelectedItem().isEmpty())
            {
            String removed = list2.getSelectionModel().getSelectedItem();
            if((!removed.equals(null) && !removed.isEmpty())){
                coursesAvail.remove(removed);
                coursesToTeach.add(removed);
                list2.getSelectionModel().select(null);
            }
            }
        });        
        
        bPane.setRight(gPane2);
      
        //TEACHER PANE BOTTOM
        GridPane gPane3 = new GridPane();
        gPane3.setHgap(10);
        gPane3.setVgap(10);
        gPane3.setPadding(new Insets(10));
        
        Button addBtn = new Button("Add Teacher"); 
        gPane3.add(addBtn, 0, 0);
        
        addBtn.setOnAction((ActionEvent event) -> {
            //take data from text fields, construct course obj
            //check if fields are valid
            Teacher t = new Teacher();
            if((id_tField.getText() != null && !id_tField.getText().isEmpty()))   //teacher id
                t.setAnum(id_tField.getText());
            if((name_tField.getText() != null && !name_tField.getText().isEmpty()))   //teacher name
                t.setName(name_tField.getText());
            
            //retrieve data from radio buttons
            t.setTimePreference(comboBox.getSelectionModel().getSelectedItem().toString());
            
            for(Course ele: c){                         //for each course in my list of teacher-less courses
                for(String name: coursesToTeach){       //for each name in coursesToTeach
                    if(ele.getName().equals(name)){     //if this course was selected
                        ele.setProf(name_tField.getText());              //change this course
                        db.alterCourse(ele);            //update database to represent change
                    }
                }                                               
            }
            
            //shove in database
            if(!(t.getAnum().isEmpty())){    
                db.addNewProfessor(t);     
                Label success = new Label("Teacher Added"); 
                FadeTransition fader = createFader(success);
                SequentialTransition fade = new SequentialTransition(success,fader);
                gPane3.add(success,1,0);
                fade.play();
                
                updateTable();
                stage.close();
            }
            else{
                Label success = new Label("Unable to add Teacher");
                FadeTransition fader = createFader(success);
                SequentialTransition fade = new SequentialTransition(success,fader);
                gPane3.add(success,1,0);
                fade.play(); 
            }
        });
        
        bPane.setBottom(gPane3);
        
        return bPane;
    }
    
    //used by Add Window
    private BorderPane addClassroomPane(Stage stage)
    {
        BorderPane bPane = new BorderPane();
        bPane.setPadding(new Insets(10));
        
        //TEACHER PANE TOP
        Label inst = new Label("Add a New Classroom");
        inst.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        bPane.setTop(inst);
        
        //TEACHER PANE LEFT
        GridPane gPane = new GridPane();
        gPane.setHgap(10);
        gPane.setVgap(10);
        gPane.setPadding(new Insets(10));
        
        //text boxes
        Label num_Label = new Label("Room Number");
        gPane.add(num_Label, 0,  0);
        Label build_Label = new Label("Building");
        gPane.add(build_Label, 0,  1);
//        Label me_Label = new Label("Max Enrollment");
//        gPane.add(me_Label, 0,  2);

        TextField num_tField = new TextField();
        gPane.add(num_tField, 1,    0);
        TextField build_tField = new TextField();
        gPane.add(build_tField, 1,    1);
//        TextField me_tField = new TextField();
//        gPane.add(me_tField, 1,    2);
        
        bPane.setLeft(gPane);
        
       //CLASSROOM PANE BOTTOM
        GridPane gPane3 = new GridPane();
        gPane3.setHgap(10);
        gPane3.setVgap(10);
        gPane3.setPadding(new Insets(10));
        
        Button addBtn = new Button("Add Classroom"); 
        gPane3.add(addBtn, 0, 0);
        
        addBtn.setOnAction((ActionEvent event) -> {
            //take data from text fields, construct course obj
            //check if fields are valid
            Classroom c = new Classroom();
            if((num_tField.getText() != null && !num_tField.getText().isEmpty()))       //room num
                c.setRoomNum(num_tField.getText());
            if((build_tField.getText() != null && !build_tField.getText().isEmpty()))   //building
                c.setBuildingName(build_tField.getText());            
//            if((me_tField.getText() != null && !me_tField.getText().isEmpty()))         //room size
//                c.setmEnroll(Integer.parseInt(me_tField.getText()));
            
            
            //shove in database
            if(!(c.getRoomNum().isEmpty())){    
                db.addClassroom(c);     
                Label success = new Label("Classroom Added"); 
                FadeTransition fader = createFader(success);
                SequentialTransition fade = new SequentialTransition(success,fader);
                gPane3.add(success,1,0);
                fade.play();
                
                updateTable();
                stage.close();
            }
            else{
                Label success = new Label("Unable to add Classroom");
                FadeTransition fader = createFader(success);
                SequentialTransition fade = new SequentialTransition(success,fader);
                gPane3.add(success,1,0);
                fade.play(); 
            }
        });
        
        bPane.setBottom(gPane3);        
        
        return bPane;
    }
    
    public void deleteWindow(){
        Stage stage = new Stage();
        stage.setResizable(false);

        //create tabbed view
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        Tab courseTab = new Tab();
        Tab teacherTab = new Tab();
        Tab classroomTab = new Tab();
        Tab semesterTab = new Tab();
        courseTab.setText("Course");
        teacherTab.setText("Teacher");
        classroomTab.setText("Classroom");
        semesterTab.setText("Semester");
        tabPane.getTabs().addAll(courseTab, teacherTab, classroomTab, semesterTab);        
        
        courseTab.setContent(deleteCoursePane(stage));
        
        teacherTab.setContent(deleteTeacherPane(stage));
        
        classroomTab.setContent(deleteClassroomPane(stage));
        
        semesterTab.setContent(deleteSemesterPane(stage));
        
        Scene scene = new Scene(tabPane, 600, 300);
        stage.setTitle("Delete");
        stage.setScene(scene);
        stage.show();
    }
    
    private BorderPane deleteCoursePane(Stage stage)
    {
        BorderPane bPane = new BorderPane();
        bPane.setPadding(new Insets(10));
        
        //TOP
        Label header = new Label("Select a Course to Delete");
        header.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        bPane.setTop(header);
        
        //LEFT
        ListView<String> list = new ListView<String>();     //listview of strings
        List<Course> c = db.getCourses(null, null);         //get list of course objects from database
        ObservableList<String> courses = FXCollections.observableArrayList(getList("course")); //get a list of strings
        list.setItems(courses);     //use courses to populate listview
        
        bPane.setLeft(list);
        
        //BOTTOM
        GridPane gPane3 = new GridPane();
        gPane3.setHgap(10);
        gPane3.setVgap(10);
        gPane3.setPadding(new Insets(10));
        
        Button updateBtn = new Button("Delete"); 
        gPane3.add(updateBtn, 0, 0);
        
        updateBtn.setOnAction((ActionEvent event) -> {
            
            Course delete = new Course();
            for(Course ele: c){
                if(ele.getName().equals(list.getSelectionModel().getSelectedItem())){
                  delete = ele;
                }
            }
            db.removeCourse(delete);
            Label success = new Label("Course Deleted"); 
            FadeTransition fader = createFader(success);
            SequentialTransition fade = new SequentialTransition(success,fader);
            gPane3.add(success,1,0);
            fade.play();

            updateTable();
            courses.remove(list.getSelectionModel().getSelectedItem());
            list.setItems(courses);
            //stage.close();  
        });
        
        bPane.setBottom(gPane3);        
        
        return bPane;        
    }
    
    private BorderPane deleteTeacherPane(Stage stage)
    {
        BorderPane bPane = new BorderPane();
        bPane.setPadding(new Insets(10));
        
        //TOP
        Label header = new Label("Select a Teacher to Delete");
        header.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        bPane.setTop(header);
        
        //LEFT
        ListView<String> list = new ListView<String>();     //listview of strings
        List<Teacher> t = db.getProfessors(null, null);         //get list of course objects from database
        ObservableList<String> teachers = FXCollections.observableArrayList(getList("teacher")); //get a list of strings
        list.setItems(teachers);     //use courses to populate listview
        
        bPane.setLeft(list);
        
        //BOTTOM
        GridPane gPane = new GridPane();
        gPane.setHgap(10);
        gPane.setVgap(10);
        gPane.setPadding(new Insets(10));
        
        Button updateBtn = new Button("Delete"); 
        gPane.add(updateBtn, 0, 0);
        
        updateBtn.setOnAction((ActionEvent event) -> {
            
            Teacher delete = new Teacher();
            for(Teacher ele: t){
                if(ele.getName().equals(list.getSelectionModel().getSelectedItem())){   
                    delete = ele;
                }
            }
            db.removeProfessor(delete);
            Label success = new Label("Teacher Deleted"); 
            FadeTransition fader = createFader(success);
            SequentialTransition fade = new SequentialTransition(success,fader);
            gPane.add(success,1,0);
            fade.play();

            updateTable();
            teachers.remove(list.getSelectionModel().getSelectedItem());
            list.setItems(teachers);
            //stage.close();             
        });
        
        bPane.setBottom(gPane);        
        
        return bPane;      
    }
        
    private BorderPane deleteClassroomPane(Stage stage)
    {
        BorderPane bPane = new BorderPane();
        bPane.setPadding(new Insets(10));
        
        //TOP
        Label header = new Label("Select a Classroom to Delete");
        header.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        bPane.setTop(header);
        
        //LEFT
        ListView<String> list = new ListView<String>();     //listview of strings
        List<Classroom> cl = db.getClassrooms(null, null);         //get list of course objects from database
        ObservableList<String> classrooms = FXCollections.observableArrayList(getList("classroom")); //get a list of strings
        list.setItems(classrooms);     //use courses to populate listview
        
        bPane.setLeft(list);
        
        //BOTTOM
        GridPane gPane = new GridPane();
        gPane.setHgap(10);
        gPane.setVgap(10);
        gPane.setPadding(new Insets(10));
        
        Button updateBtn = new Button("Delete"); 
        gPane.add(updateBtn, 0, 0);
        
        updateBtn.setOnAction((ActionEvent event) -> {
            
            Classroom delete = new Classroom();
            for(Classroom ele: cl){
                if(ele.getRoomNum().equals(list.getSelectionModel().getSelectedItem())){   
                    delete = ele;
                }
            }
            db.removeClassroom(delete);
            Label success = new Label("Classroom Deleted"); 
            FadeTransition fader = createFader(success);
            SequentialTransition fade = new SequentialTransition(success,fader);
            gPane.add(success,1,0);
            fade.play();

            updateTable();
            classrooms.remove(list.getSelectionModel().getSelectedItem());
            list.setItems(classrooms);
            //stage.close();             
        });
        
        bPane.setBottom(gPane);        
        
        return bPane;        
    }
    
    private BorderPane deleteSemesterPane(Stage stage)
    {
        BorderPane bPane = new BorderPane();
        bPane.setPadding(new Insets(10));
        
        //TOP
        Label header = new Label("Select a Semester to Delete");
        header.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        bPane.setTop(header);
        
        //LEFT
        ListView<String> list = new ListView();     //listview of strings
        ObservableList<String> sems = FXCollections.observableArrayList(db.getSemesters()); //get a list of strings
        list.setItems(sems);     //use courses to populate listview
        bPane.setLeft(list);
        
        //BOTTOM
        GridPane gPane = new GridPane();
        gPane.setHgap(10);
        gPane.setVgap(10);
        gPane.setPadding(new Insets(10));
        
        Button updateBtn = new Button("Delete"); 
        gPane.add(updateBtn, 0, 0);
        
        updateBtn.setOnAction((ActionEvent event) -> {
            String delete = list.getSelectionModel().getSelectedItem();
            //System.out.println(delete);
            db.clearDatabase(delete);
            
            Label success = new Label("Semester Deleted"); 
            FadeTransition fader = createFader(success);
            SequentialTransition fade = new SequentialTransition(success,fader);
            gPane.add(success,1,0);
            fade.play();

            updateTable();
            sems.remove(list.getSelectionModel().getSelectedItem());
            list.setItems(sems);
            //stage.close();             
        });
        
        bPane.setBottom(gPane);        
        
        return bPane;        
    }   
    
    public void editWindow(){
        
        Stage stage = new Stage();
        stage.setResizable(false);
      
        //create tabbed view
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        Tab courseTab = new Tab();
        Tab teacherTab = new Tab();
        Tab classroomTab = new Tab();
        courseTab.setText("Course");
        teacherTab.setText("Teacher");
        classroomTab.setText("Classroom");
        tabPane.getTabs().addAll(courseTab, teacherTab, classroomTab);
        
        //set content in course tab
        courseTab.setContent(editCoursePane(stage));        
        //set content in teacher tab
        teacherTab.setContent(editTeacherPane(stage));
        //set content in classroom tab
        classroomTab.setContent(editClassroomPane(stage));
        
        Scene scene = new Scene(tabPane, 600, 450);
        stage.setTitle("Edit");
        stage.setScene(scene);
        stage.show();
    }
    
    private BorderPane editCoursePane(Stage stage)
    {
        BorderPane bPane = new BorderPane();
        bPane.setPadding(new Insets(10));
        
        //TOP
        Label header = new Label("Select a Course to Edit");
        header.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        bPane.setTop(header);
                
        //RIGHT
        GridPane gPane = new GridPane();
        gPane.setHgap(10);
        gPane.setVgap(10);
        gPane.setPadding(new Insets(10));
        
        Label depLabel = new Label("Department");
        gPane.add(depLabel, 0,  0);
//        Label numLabel = new Label("Course Number");
//        gPane.add(numLabel, 0,  2);
        Label nameLabel = new Label("Course Name");
        gPane.add(nameLabel, 0, 1);
//        Label m_eLabel = new Label("Max Enrollment");
//        gPane.add(m_eLabel, 0,  4);
//        Label eLabel = new Label("Enrollment");
//        gPane.add(eLabel, 0,    5);
//        Label aLabel = new Label("Available");
//        gPane.add(aLabel, 0,    6);
//        Label w_lLabel = new Label("Wait List");
//        gPane.add(w_lLabel, 0,  7);

        TextField dep_tField = new TextField();
        gPane.add(dep_tField, 1,    0);
//        TextField num_tField = new TextField("sample");
//        gPane.add(num_tField, 1,    2);
        TextField name_tField = new TextField();
        gPane.add(name_tField, 1,   1);
//        TextField me_tField = new TextField("sample");
//        gPane.add(me_tField, 1,     4);
//        TextField e_tField = new TextField("sample");
//        gPane.add(e_tField, 1,      5);
//        TextField a_tField = new TextField("sample");
//        gPane.add(a_tField, 1,      6);
//        TextField wl_tField = new TextField("sample");
//        gPane.add(wl_tField, 1,     7);
        
        //frop drown lost
        Label teach_CBox = new Label("Teacher");
        gPane.add(teach_CBox, 0, 3);
        
        ObservableList<String> options = FXCollections.observableArrayList(getList("teacher"));
        ComboBox comboBox = new ComboBox(options);        
        gPane.add(comboBox, 1, 3);
        bPane.setRight(gPane); 
        
        //LEFT
        ListView<String> list = new ListView<String>();
        List<Course> c = db.getCourses(null, null);
        ObservableList<String> courses = FXCollections.observableArrayList(getList("course"));
        list.setItems(courses);
        
        list.getSelectionModel().selectedItemProperty().addListener((observable) -> {
        
            Course update = new Course();

            for(Course ele: c){
                if(ele.getName().equals(list.getSelectionModel().getSelectedItem()))
                update = ele;    
            }
            
            update.setDays("");     //the update = ele sets all blank fields to the actual string null
            update.setsTime("");    //so we gotta do this 
            update.seteTime("");
            update.setClassroom("");
            
            dep_tField.setText(update.getDepartment());
//            num_tField.setText(update.getCourseNum());
            name_tField.setText(update.getName());
//            me_tField.setText(Integer.toString(update.getM_enroll()));
//            e_tField.setText(Integer.toString(update.getEnroll()));
//            a_tField.setText(Integer.toString(update.getAvail()));
//            wl_tField.setText(Integer.toString(update.getWaitList()));
            
            comboBox.setValue(update.getProf());
        });
        
        bPane.setLeft(list);
        
        //BOTTOM
        GridPane gPane3 = new GridPane();
        gPane3.setHgap(10);
        gPane3.setVgap(10);
        gPane3.setPadding(new Insets(10));
        
        Button updateBtn = new Button("Update"); 
        gPane3.add(updateBtn, 0, 0);
        
        updateBtn.setOnAction((ActionEvent event) -> {
            
            //course obj alter copies the course we are editing
            //then holds all changes to that course
            //these changes are in the text fields/ combo box until
            //we assign them in the sets down below
            Course alter = new Course();
            for(Course ele: c){
                if(ele.getName().equals(list.getSelectionModel().getSelectedItem())){
                    alter = ele;    
                }
            }
            
            alter.setDepartment     (dep_tField.getText());
//            alter.setCourseNum      (num_tField.getText());
            alter.setName           (name_tField.getText());
//            alter.setM_enroll       (Integer.parseInt(me_tField.getText()));
//            alter.setEnroll         (Integer.parseInt(e_tField.getText()));
//            alter.setAvail          (Integer.parseInt(a_tField.getText()));
//            alter.setWaitList       (Integer.parseInt(wl_tField.getText()));
            if(comboBox.getSelectionModel().getSelectedItem() != null && !comboBox.getSelectionModel().getSelectedItem().toString().isEmpty()){
                alter.setProf(comboBox.getSelectionModel().getSelectedItem().toString());
            
                //shove in database
                if(!(alter.getCrn() == 0)){    
                    db.alterCourse(alter);
                    Label success = new Label("Course Updated"); 
                    FadeTransition fader = createFader(success);
                    SequentialTransition fade = new SequentialTransition(success,fader);
                    gPane3.add(success,1,0);
                    fade.play();

                    updateTable();
                    stage.close();
                }
                else{
                    Label success = new Label("Unable to Update Course");
                    FadeTransition fader = createFader(success);
                    SequentialTransition fade = new SequentialTransition(success,fader);
                    gPane3.add(success,1,0);
                    fade.play(); 
                }
            }
        });
        
        bPane.setBottom(gPane3);        
        
        return bPane;
    }
    
    private BorderPane editTeacherPane(Stage stage)
    {
        BorderPane bPane = new BorderPane();
        bPane.setPadding(new Insets(10));
        
        //TOP
        Label header = new Label("Select a Teacher to Edit");
        header.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        bPane.setTop(header);
        
        //RIGHT
        GridPane gPane = new GridPane();
        gPane.setHgap(10);
        gPane.setVgap(10);
        gPane.setPadding(new Insets(10));
        
        Label name_Label = new Label("Name");
        gPane.add(name_Label, 0,  0);

        TextField name_tField = new TextField();
        gPane.add(name_tField, 1,    0);
        
        Label timePrefLabel = new Label("Time Preference:");
        gPane.add(timePrefLabel, 0,1);

        ObservableList<String> options = FXCollections.observableArrayList("None", "Morning", "Afternoon", "Evening", "MW", "TR");
        ComboBox comboBox = new ComboBox(options);  
        gPane.add(comboBox, 1, 1);

        bPane.setRight(gPane);
        
        //LEFT
        ListView<String> list = new ListView<String>();
        List<Teacher> t = db.getProfessors(null, null);
        List<String> teacherNames = getList("teacher");
        ObservableList<String> teachers = FXCollections.observableArrayList(teacherNames);
        list.setItems(teachers);
        bPane.setLeft(list);    
        
        list.getSelectionModel().selectedItemProperty().addListener((observable) -> {
        
            Teacher update = new Teacher();
            for(Teacher ele: t){
                if(ele.getName().equals(list.getSelectionModel().getSelectedItem()))
                update = ele;    
            }
            
            name_tField.setText(update.getName());
            
            comboBox.setValue(update.getTimePreference());
        });
        
        //BOTTOM
        GridPane gPane3 = new GridPane();
        gPane3.setHgap(10);
        gPane3.setVgap(10);
        gPane3.setPadding(new Insets(10));
        
        Button updateBtn = new Button("Update"); 
        gPane3.add(updateBtn, 0, 0);
        
        updateBtn.setOnAction((ActionEvent event) -> {
            
            Teacher alter = new Teacher();
            for(Teacher ele: t){
                if(ele.getName().equals(list.getSelectionModel().getSelectedItem()))
                alter = ele;    
            }
            
            String oldName = alter.getName();
            alter.setName           (name_tField.getText());
            if(comboBox.getSelectionModel().getSelectedItem() != null && !comboBox.getSelectionModel().getSelectedItem().toString().isEmpty()){
                alter.setTimePreference (comboBox.getSelectionModel().getSelectedItem().toString());

                //shove in database
                if(!(alter.getAnum().isEmpty())){    
                    db.alterProfessor(alter, oldName);
                    Label success = new Label("Teacher Updated"); 
                    FadeTransition fader = createFader(success);
                    SequentialTransition fade = new SequentialTransition(success,fader);
                    gPane3.add(success,1,0);
                    fade.play();

                    //scheduler.scheduleCourses();
                    updateTable();
                    teachers.clear();
                    teachers.setAll(getList("teacher"));
                    list.setItems(teachers);
                    stage.close();
                }
                else{
                    Label success = new Label("Unable to Update Teacher");
                    FadeTransition fader = createFader(success);
                    SequentialTransition fade = new SequentialTransition(success,fader);
                    gPane3.add(success,1,0);
                    fade.play(); 
                }
            }
        });
        
        bPane.setBottom(gPane3); 
        return bPane;
    }
        
    private BorderPane editClassroomPane(Stage stage)
    {
        BorderPane bPane = new BorderPane();
        bPane.setPadding(new Insets(10));
        
        //RIGHT
        GridPane gPane = new GridPane();
        gPane.setHgap(10);
        gPane.setVgap(10);
        gPane.setPadding(new Insets(10));
        
        Label build_Label = new Label("Building");
        gPane.add(build_Label, 0,  0);
//        Label me_Label = new Label("Max Enrollment");
//        gPane.add(me_Label, 0,  1);

        TextField build_tField = new TextField();
        gPane.add(build_tField, 1,    0);
//        TextField me_tField = new TextField();
//        gPane.add(me_tField, 1,    1);
        
        bPane.setRight(gPane);
        
        //LEFT
        Label header = new Label("Select a Classroom to Edit");
        header.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        bPane.setTop(header);
        
        ListView<String> list = new ListView<String>();
        List<Classroom> cl = db.getClassrooms(null, null);
        
        ObservableList<String> rooms = FXCollections.observableArrayList(getList("classroom"));
        list.setItems(rooms);
        
        list.getSelectionModel().selectedItemProperty().addListener((observable) -> {
        
            Classroom update = new Classroom();
            for(Classroom ele: cl){
                if(ele.getRoomNum().equals(list.getSelectionModel().getSelectedItem()))
                {
                    update = ele;
                }
            }
            
            build_tField.setText(update.getBuildingName());
//            me_tField.setText(Integer.toString(update.getmEnroll()));
        });
        
        bPane.setLeft(list); 
        
        //BOTTOM
        GridPane gPane3 = new GridPane();
        gPane3.setHgap(10);
        gPane3.setVgap(10);
        gPane3.setPadding(new Insets(10));
        
        Button updateBtn = new Button("Update"); 
        gPane3.add(updateBtn, 0, 0);
        
        updateBtn.setOnAction((ActionEvent event) -> {
            
            Classroom alter = new Classroom();
            for(Classroom ele: cl){
                if(ele.getRoomNum().equals(list.getSelectionModel().getSelectedItem()))
                    alter = ele;    
            }
             
            alter.setBuildingName(build_tField.getText());
//            alter.setmEnroll(Integer.parseInt(me_tField.getText()));
            
            //shove in database
            if(!(alter.getRoomNum().isEmpty())){    
                db.alterClassroom(alter);
                Label success = new Label("Classroom Updated"); 
                FadeTransition fader = createFader(success);
                SequentialTransition fade = new SequentialTransition(success,fader);
                gPane3.add(success,1,0);
                fade.play();
                
                updateTable();
                //rooms.clear();
                //rooms.setAll(getList("classroom"));
                //list.setItems(rooms);
                stage.close();
            }
            else{
                Label success = new Label("Unable to Update Teacher");
                FadeTransition fader = createFader(success);
                SequentialTransition fade = new SequentialTransition(success,fader);
                gPane3.add(success,1,0);
                fade.play(); 
            }
        });
        
        bPane.setBottom(gPane3);         
        return bPane;
    }    

    //util functions to fade labels and other things
    private FadeTransition createFader(Node node)
    {        
        FadeTransition fade = new FadeTransition(Duration.seconds(2), node);
        fade.setFromValue(1);
        fade.setToValue(0);
        return fade;
    }
    
    /**Util function to return a list of the names of the courses, teachers, or classrooms
     * 
     * @param option - "course", "teacher", "classroom"
     * @return a list of the names
     */
    private List getList(String option)
    {
        List<String> list = new ArrayList();
        
        switch (option) {
            case "course":
                List<Course> c = db.getCourses(null, null);
                for(Course ele: c){
                    if(ele.getName().equals(null) || ele.getName().isEmpty())
                        list.add("<Empty>");
                    else
                        list.add(ele.getName());
                }   break;
            case "teacher":
                List<Teacher> t = db.getProfessors(null, null);
                for(Teacher ele: t){                    
                    if(ele.getName().equals(null) || ele.getName().isEmpty())
                        list.add("<Empty>");
                    else
                        list.add(ele.getName());
                }   break;
            case "classroom":
                List<Classroom> cl = db.getClassrooms(null, null);
                for(Classroom ele: cl){
                    //list.add(ele.getBuildingName() +" "+ ele.getRoomNum());
                    list.add(ele.getRoomNum());
            }   break;              
        }

        return list;
    }   
//AJ CODE///////////////////////////////////////////////

    private void updateTable() {
        //System.out.println("************************************");
        scheduler.scheduleCourses();
        data.clear();
        data.addAll(db.getCourses(null, null));
        //writeToCSV();
    }
    
    private VBox configureTable(TableView table) {
        Label label = new Label("Schedule");
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        //aj edit - sets the table to larger width
        table.setMinWidth(1100);
        
        updateTable();
        
        TableColumn semesterCol = new TableColumn("Semester");
        TableColumn crnCol =      new TableColumn("CRN");
        TableColumn depCol =      new TableColumn("Department");
       //TableColumn numCol =      new TableColumn("#");
        TableColumn nameCol =     new TableColumn("Name");
        //TableColumn m_EnrollCol = new TableColumn("Seats");
        //TableColumn enrollCol =   new TableColumn("Enrolled");
        //TableColumn availCol =    new TableColumn("Available");
        //TableColumn waitCol =     new TableColumn("Wait List");
        TableColumn daysCol =     new TableColumn("Days");
        TableColumn startCol =    new TableColumn("Start");
        TableColumn endCol =      new TableColumn("End");
        TableColumn buildingCol = new TableColumn("Building");
        TableColumn roomCol =     new TableColumn("Room #");
        TableColumn teacherCol =  new TableColumn("Teacher");
        
        semesterCol.setCellValueFactory(new PropertyValueFactory<>("semester")); 
        crnCol.setCellValueFactory(     new PropertyValueFactory<>("crn"));
        depCol.setCellValueFactory(     new PropertyValueFactory<>("department"));
        //numCol.setCellValueFactory(     new PropertyValueFactory<>("name"));
        nameCol.setCellValueFactory(    new PropertyValueFactory<>("name"));
        //m_EnrollCol.setCellValueFactory(new PropertyValueFactory<>("m_enroll"));
        //enrollCol.setCellValueFactory(  new PropertyValueFactory<>("enroll"));
        //availCol.setCellValueFactory(   new PropertyValueFactory<>("avail"));
        //waitCol.setCellValueFactory(    new PropertyValueFactory<>("waitList"));
        daysCol.setCellValueFactory(    new PropertyValueFactory<>("days"));
        startCol.setCellValueFactory(   new PropertyValueFactory<>("sTime"));
        endCol.setCellValueFactory(     new PropertyValueFactory<>("eTime"));
        buildingCol.setCellValueFactory(new PropertyValueFactory<>("building"));
        roomCol.setCellValueFactory(    new PropertyValueFactory<>("classroom"));
        teacherCol.setCellValueFactory( new PropertyValueFactory<>("prof"));
        table.setItems(data);
        
        //teacherCol.widthProperty().set(125);
        table.getColumns().addAll(semesterCol, crnCol, depCol, 
                /*numCol,*/ nameCol, /*m_EnrollCol, enrollCol, waitCol, 
                availCol,*/ daysCol, startCol, endCol, buildingCol, 
                roomCol, teacherCol);
        
        semesterCol.prefWidthProperty().bind(table.widthProperty().multiply(0.1));
        crnCol.prefWidthProperty().bind(table.widthProperty().multiply(0.1));
        depCol.prefWidthProperty().bind(table.widthProperty().multiply(0.1));
        nameCol.prefWidthProperty().bind(table.widthProperty().multiply(0.1));
        daysCol.prefWidthProperty().bind(table.widthProperty().multiply(0.1));
        startCol.prefWidthProperty().bind(table.widthProperty().multiply(0.1));
        endCol.prefWidthProperty().bind(table.widthProperty().multiply(0.1));
        buildingCol.prefWidthProperty().bind(table.widthProperty().multiply(0.1));
        roomCol.prefWidthProperty().bind(table.widthProperty().multiply(0.1));
        teacherCol.prefWidthProperty().bind(table.widthProperty().multiply(0.1));
        
        //System.out.println(table.getWidth());
        VBox vbox = new VBox();
        vbox.setPadding(new Insets(0, 0, 0, 10));
        vbox.getChildren().addAll(label, table);
        
        return vbox;
    }
    
    private VBox configureButtons() {
        Button btnAdd = new Button("Add");
        Button btnDelete = new Button("Delete");
        Button btnEdit = new Button("Edit");
        Button btnPrint = new Button("Print");
        Button btnHelp = new Button("Help");  /* Added by Victoria 11/27/16 */
        Label placeholder = new Label("");
            
        btnAdd.setMinWidth(100.0);
        btnDelete.setMaxWidth(100.0);
        btnEdit.setMaxWidth(100.0);
        btnPrint.setMaxWidth(100.0);
        btnHelp.setMaxWidth(100.0);  /* Added by Victoria 11/27/16 */
        
        VBox vbox = new VBox();
        vbox.setSpacing(10);
        vbox.setPadding(new Insets(20, 20, 10, 20)); 
        vbox.getChildren().addAll(btnAdd, btnDelete, btnEdit, btnPrint, btnHelp);  /* Modified by Victoria 11/27/16 */    
        
        /* Added by Victoria 11/27/16 */
        btnHelp.setOnAction((ActionEvent event) -> { 
            editHelp();
        });
        /* Added by Victoria 11/27/16 */
        
        btnAdd.setOnAction((ActionEvent event) -> {
            addWindow();
        });
        btnDelete.setOnAction((ActionEvent event) -> {
            deleteWindow();
        });
        btnEdit.setOnAction((ActionEvent event) -> {
            editWindow();
        });
        btnPrint.setOnAction((ActionEvent event) -> {
            writeToCSV();
            Label success = new Label("Course Updated"); 
            FadeTransition fader = createFader(success);
            SequentialTransition fade = new SequentialTransition(success,fader);
            vbox.getChildren().remove(4);
            vbox.getChildren().add(success);
            fade.play();           
            
        });
        
        return vbox;
    }
    
    private static void configureFileChooser(FileChooser fileChooser){                           
        fileChooser.setTitle("Open CSV");
        
        fileChooser.setInitialDirectory(
            new File(System.getProperty("user.home"))
        );
        
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("CSV", "*.csv")
        );
    }
    
    private void writeToCSV(){
        /* Write the assignments to a file */
        String fileName = "CourseAssignments.csv";
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(fileName));
        } catch (IOException ex) {
            Logger.getLogger(CourseScheduler.class.getName()).log(Level.SEVERE, null, ex);
        }
        int i = 0;
 
       String head = "Semester,CRN,Department,Name,Days,Start,End,Building,Room#,Teacher\n"; 
       try {
            writer.write(head);
        }
        catch (IOException e){
            System.out.println(e);
        } 
        
        for (Course c : data) {
            if(c != null){
                /* Write to screen for viewing */
                String text = "";
//                if(i == 0)/*{
//                    text = "Semester,CRN,Department,Name,Days,Start,End,Building,Room#,Teacher\n";
//                    i++;
//                }
//                else*/ {
                    text = c.getSemester() + "," + c.getCrn() + "," + c.getDepartment() + "," + c.getName() + "," + c.getDays() + ","
                            + c.getSTime() + "," + c.getETime() + "," + c.getBuilding() + "," + c.getClassroom() + "," + c.getProf() + "\n";
//                }
               try {
                    writer.write(text);
                }
                catch (IOException e){
                    System.out.println(e);
                }
            }
       }
        
        try{
           if (writer != null){
               writer.close( );
           }
        }
        catch ( IOException e){
            System.out.println(e);
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
