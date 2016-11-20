/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package course_scheduler_beta;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
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
import javafx.scene.layout.StackPane;
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
    ObservableList<Course> data = FXCollections.observableArrayList(db.getCourses(null, null));
    
    @Override
    public void start(Stage stage) {
        //set semseter to default
        db.setCurrentSemester("Fall 2016");
        
        stage.setResizable(false);
        FileChooser fileChooser = new FileChooser();
        
        //base pane for start window
        BorderPane bPane = new BorderPane();
        bPane.setPadding(new Insets(10));
        
        //new and load buttons
        HBox hbox = new HBox();
        hbox.setSpacing(50);
        Button btnOpenCSV = new Button("New");
        btnOpenCSV.setMinWidth(100);
        Button btnLoadCSV = new Button("Load");
        btnLoadCSV.setMinWidth(100);
        hbox.getChildren().addAll(btnOpenCSV, btnLoadCSV);
        hbox.setAlignment(Pos.CENTER);
        bPane.setTop(hbox);
        
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
                Parser parser = new Parser(file);
                mainWindow(parser);
                stage.close();
            }
            //Parser parser = new Parser();
            //mainWindow();
            //stage.close();
        });
        
        Scene scene = new Scene(bPane, 300, 250);
        stage.setTitle("Welcome!");
        stage.setScene(scene);
        stage.show();
    }
    
    public void mainWindow(Parser data){
        Stage stage = new Stage();
        //stage.setResizable(false);
        BorderPane border = new BorderPane();
        border.setPadding(new Insets(20, 200, 100, 20));    // top, right, bottom, left
        TableView table = new TableView();
        
        border.setLeft(configureButtons());
        border.setRight(configureTable(table, data));
        Scene scene = new Scene(border, 1400, 500);
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
        courseTab.setContent(addCoursePane());        
        //set content in teacher tab
        teacherTab.setContent(addTeacherPane());
        //set content in classroom tab
        classroomTab.setContent(addClassroomPane());
        
        Scene scene = new Scene(tabPane, 600, 450);
        stage.setTitle("Add");
        stage.setScene(scene);
        stage.show();
    }
    
    //used by  Add Window
    private BorderPane addCoursePane()
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
        Label numLabel = new Label("Course Number");
        gPane.add(numLabel, 0,  2);
        Label nameLabel = new Label("Course Name");
        gPane.add(nameLabel, 0, 3);
        Label m_eLabel = new Label("Max Enrollment");
        gPane.add(m_eLabel, 0,  4);
        Label eLabel = new Label("Enrollment");
        gPane.add(eLabel, 0,    5);
        Label aLabel = new Label("Available");
        gPane.add(aLabel, 0,    6);
        Label w_lLabel = new Label("Wait List");
        gPane.add(w_lLabel, 0,  7);

        TextField crn_tField = new TextField();
        gPane.add(crn_tField, 1,    0);
        TextField dep_tField = new TextField();
        gPane.add(dep_tField, 1,    1);
        TextField num_tField = new TextField();
        gPane.add(num_tField, 1,    2);
        TextField name_tField = new TextField();
        gPane.add(name_tField, 1,   3);
        TextField me_tField = new TextField();
        gPane.add(me_tField, 1,     4);
        TextField e_tField = new TextField();
        gPane.add(e_tField, 1,      5);
        TextField a_tField = new TextField();
        gPane.add(a_tField, 1,      6);
        TextField wl_tField = new TextField();
        gPane.add(wl_tField, 1,     7);
        
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
            if((crn_tField.getText() != null && !crn_tField.getText().isEmpty()))   //crn
                c.setCrn(Integer.parseInt(crn_tField.getText()));
            if((dep_tField.getText() != null && !dep_tField.getText().isEmpty()))   //department
                c.setDepartment(dep_tField.getText());
            if((num_tField.getText() != null && !num_tField.getText().isEmpty()))   //Course Number
                c.setCourseNum(num_tField.getText());
            if((name_tField.getText() != null && !name_tField.getText().isEmpty()))   //course name
                c.setName(name_tField.getText());
            if((me_tField.getText() != null && !me_tField.getText().isEmpty()))   //max enrollment
                c.setM_enroll(Integer.parseInt(me_tField.getText()));
            if((e_tField.getText() != null && !e_tField.getText().isEmpty()))   //enrollment
                c.setEnroll(Integer.parseInt(e_tField.getText()));
            if((a_tField.getText() != null && !a_tField.getText().isEmpty()))   //available
                c.setAvail(Integer.parseInt(a_tField.getText()));
            if((wl_tField.getText() != null && !wl_tField.getText().isEmpty()))   //wait list
                c.setWaitList(Integer.parseInt(wl_tField.getText()));
            
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
    private BorderPane addTeacherPane()
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
        final ToggleGroup timePref = new ToggleGroup();
        
        RadioButton none_rb = new RadioButton("None");
        none_rb.setToggleGroup(timePref);
        none_rb.setSelected(true);
        RadioButton morning_rb = new RadioButton("Morning");
        morning_rb.setToggleGroup(timePref);
        RadioButton afternoon_rb = new RadioButton("Afternoon");
        afternoon_rb.setToggleGroup(timePref);
        
        Label timePrefLabel = new Label("Time Preference:");
        gPane.add(timePrefLabel, 0,3);
        
        gPane.add(none_rb, 0,4);
        gPane.add(morning_rb, 0,5);
        gPane.add(afternoon_rb, 0,6);

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
        
        List<Course> c = db.getCourses("PROFESSOR", null);
        List<String> c_crns = new ArrayList();
        for(Course ele: c){
            System.out.println("1: "+ele);
            c_crns.add(Integer.toString(ele.getCrn()));
        }
        
        ObservableList<String> coursesAvail = FXCollections.observableArrayList(c_crns);
        list2.setItems(coursesAvail);
        list2.setPrefWidth(150);
        list2.getSelectionModel().select(null);
        gPane2.add(list2, 1,1);
        
        //remove course button and action
        Button removeBtn = new Button("Remove ->");
        removeBtn.setMinWidth(150);
        gPane2.add(removeBtn, 0, 5);
        
        removeBtn.setOnAction((ActionEvent event) -> {
            String removed = list.getSelectionModel().getSelectedItem();
            System.out.println(removed);
            if((!removed.equals(null) && !removed.isEmpty())){
                coursesToTeach.remove(removed);
                coursesAvail.add(removed);
                list.getSelectionModel().select(null);
            }
        });
        
        
        Button addCourseBtn = new Button("<- Add");
        gPane2.add(addCourseBtn, 1, 5);
        addCourseBtn.setMinWidth(150);
        
        addCourseBtn.setOnAction((ActionEvent event) -> {
            String removed = list2.getSelectionModel().getSelectedItem();
            System.out.println(removed);
            if((!removed.equals(null) && !removed.isEmpty())){
                coursesAvail.remove(removed);
                coursesToTeach.add(removed);
                list2.getSelectionModel().select(null);
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
            if(none_rb.isSelected()){
                t.setTimePreference("None");
            }
            else if(morning_rb.isSelected()){
                t.setTimePreference("Morning");
            }
            else{
                t.setTimePreference("Afternoon");
            }
            
            //retrieve course objs
            for(String ele: coursesToTeach){
                Course toAdd = new Course();
                toAdd = (Course)db.getCourses("CRN", ele).get(0);
                t.addCourse(toAdd);
            }
            
            //shove in database
            if(!(t.getAnum().isEmpty())){    
                db.addNewProfessor(t);     
                Label success = new Label("Teacher Added"); 
                FadeTransition fader = createFader(success);
                SequentialTransition fade = new SequentialTransition(success,fader);
                gPane3.add(success,1,0);
                fade.play();
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
    private BorderPane addClassroomPane()
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
        Label me_Label = new Label("Max Enrollment");
        gPane.add(me_Label, 0,  2);

        TextField num_tField = new TextField();
        gPane.add(num_tField, 1,    0);
        TextField build_tField = new TextField();
        gPane.add(build_tField, 1,    1);
        TextField me_tField = new TextField();
        gPane.add(me_tField, 1,    2);
        
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
            if((me_tField.getText() != null && !me_tField.getText().isEmpty()))         //room size
                c.setmEnroll(Integer.parseInt(me_tField.getText()));
            
            
            //shove in database
            if(!(c.getRoomNum().isEmpty())){    
                db.addClassroom(c);     
                Label success = new Label("Classroom Added"); 
                FadeTransition fader = createFader(success);
                SequentialTransition fade = new SequentialTransition(success,fader);
                gPane3.add(success,1,0);
                fade.play();
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
        StackPane root = new StackPane();
        Scene scene = new Scene(root, 500, 300);
        stage.setTitle("Delete");
        stage.setScene(scene);
        stage.show();
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
        courseTab.setContent(editCoursePane());        
        //set content in teacher tab
        teacherTab.setContent(editTeacherPane());
        //set content in classroom tab
        classroomTab.setContent(editClassroomPane());
        
        Scene scene = new Scene(tabPane, 600, 450);
        stage.setTitle("Edit");
        stage.setScene(scene);
        stage.show();
    }
    
    private BorderPane editCoursePane()
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
        
        Label crnLabel = new Label("CRN");
        gPane.add(crnLabel, 0,  0);
        Label depLabel = new Label("Department");
        gPane.add(depLabel, 0,  1);
        Label numLabel = new Label("Course Number");
        gPane.add(numLabel, 0,  2);
        Label nameLabel = new Label("Course Name");
        gPane.add(nameLabel, 0, 3);
        Label m_eLabel = new Label("Max Enrollment");
        gPane.add(m_eLabel, 0,  4);
        Label eLabel = new Label("Enrollment");
        gPane.add(eLabel, 0,    5);
        Label aLabel = new Label("Available");
        gPane.add(aLabel, 0,    6);
        Label w_lLabel = new Label("Wait List");
        gPane.add(w_lLabel, 0,  7);

        TextField crn_tField = new TextField("sample");
        gPane.add(crn_tField, 1,    0);
        TextField dep_tField = new TextField("sample");
        gPane.add(dep_tField, 1,    1);
        TextField num_tField = new TextField("sample");
        gPane.add(num_tField, 1,    2);
        TextField name_tField = new TextField("sample");
        gPane.add(name_tField, 1,   3);
        TextField me_tField = new TextField("sample");
        gPane.add(me_tField, 1,     4);
        TextField e_tField = new TextField("sample");
        gPane.add(e_tField, 1,      5);
        TextField a_tField = new TextField("sample");
        gPane.add(a_tField, 1,      6);
        TextField wl_tField = new TextField("sample");
        gPane.add(wl_tField, 1,     7);
        
        //frop drown lost
        Label teach_CBox = new Label("Teacher");
        gPane.add(teach_CBox, 0, 8);
        
        ObservableList<String> options = FXCollections.observableArrayList(getList("teacher"));
        ComboBox comboBox = new ComboBox(options);        
        gPane.add(comboBox, 1, 8);
        bPane.setRight(gPane); 
        
        //LEFT
        ListView<String> list = new ListView<String>();
        List<Course> c = db.getCourses(null, null);
        List<String> courseNames = getList("course");
        ObservableList<String> courses = FXCollections.observableArrayList(courseNames);
        list.setItems(courses);
        
        list.getSelectionModel().selectedItemProperty().addListener((observable) -> {
        
            Course update = new Course();
            for(Course ele: c){
                if(ele.getName().equals(list.getSelectionModel().getSelectedItem()))
                update = ele;    
            }
            
            crn_tField.setText(Integer.toString(update.getCrn()));
            dep_tField.setText(update.getDepartment());
            num_tField.setText(update.getCourseNum());
            name_tField.setText(update.getName());
            me_tField.setText(Integer.toString(update.getM_enroll()));
            e_tField.setText(Integer.toString(update.getEnroll()));
            a_tField.setText(Integer.toString(update.getAvail()));
            wl_tField.setText(Integer.toString(update.getWaitList()));
            
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
            
            Course alter = new Course();
            alter.setCrn(Integer.parseInt(crn_tField.getText()));
            alter.setDepartment(crn_tField.getText());
            alter.setCourseNum(crn_tField.getText());
            alter.setName(crn_tField.getText());
            alter.setM_enroll(Integer.parseInt(crn_tField.getText()));
            alter.setEnroll(Integer.parseInt(crn_tField.getText()));
            alter.setAvail(Integer.parseInt(crn_tField.getText()));
            alter.setWaitList(Integer.parseInt(crn_tField.getText()));
            
            Teacher t = new Teacher();
            //remove all instances of course from all entries in teacher table
            //add reference 
            //db.alterProfessor(t);
            
            //shove in database
            if(alter.getCrn() == 0){    
                db.alterCourse(alter);
                Label success = new Label("Classroom Added"); 
                FadeTransition fader = createFader(success);
                SequentialTransition fade = new SequentialTransition(success,fader);
                gPane3.add(success,1,0);
                fade.play();
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
    
    private BorderPane editTeacherPane()
    {
        BorderPane bPane = new BorderPane();
        bPane.setPadding(new Insets(10));
        
        Label header = new Label("Select a Teacher to Edit");
        header.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        bPane.setTop(header);
        
        ListView<String> list = new ListView<String>();
        List<String> teacherNames = getList("teacher");
        ObservableList<String> teachers = FXCollections.observableArrayList(teacherNames);
        list.setItems(teachers);
        
        bPane.setLeft(list);       
        
        return bPane;
    }
        
    private BorderPane editClassroomPane()
    {
        BorderPane bPane = new BorderPane();
        bPane.setPadding(new Insets(10));
        
        Label header = new Label("Select a Classroom to Edit");
        header.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        bPane.setTop(header);
        
        ListView<String> list = new ListView<String>();
        List<String> roomNums = getList("classroom");
        ObservableList<String> rooms = FXCollections.observableArrayList(roomNums);
        list.setItems(rooms);
        
        bPane.setLeft(list);       
        
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
                    list.add(ele.getName());
                }   break;
            case "teacher":
                List<Teacher> t = db.getProfessors(null, null);
                for(Teacher ele: t){
                    list.add(ele.getName());
                }   break;
            case "classroom":
                List<Classroom> cl = db.getClassrooms(null, null);
                for(Classroom ele: cl){
                    list.add(ele.getBuildingName() +" "+ ele.getRoomNum());
            }   break;              
        }

        return list;
    }   
//AJ CODE///////////////////////////////////////////////

    private void updateTable(TableView table) {
        
    }
    
    private VBox configureTable(TableView table, Parser parser) {
        Label label = new Label("Schedule");
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        //ObservableList<Course> data = FXCollections.observableArrayList(db.getCourses(null, null));
        TableColumn semesterCol = new TableColumn("Semester");
        TableColumn crnCol =      new TableColumn("CRN");
        TableColumn depCol =      new TableColumn("Department");
        TableColumn numCol =      new TableColumn("#");
        TableColumn nameCol =     new TableColumn("Name");
        TableColumn m_EnrollCol = new TableColumn("Seats");
        TableColumn enrollCol =   new TableColumn("Enrolled");
        TableColumn availCol =    new TableColumn("Available");
        TableColumn waitCol =     new TableColumn("Wait List");
        TableColumn daysCol =     new TableColumn("Days");
        TableColumn startCol =    new TableColumn("Start");
        TableColumn endCol =      new TableColumn("End");
        TableColumn buildingCol = new TableColumn("Building");
        TableColumn roomCol =     new TableColumn("Room #");
        TableColumn teacherCol =  new TableColumn("Teacher");
        
        semesterCol.setCellValueFactory(new PropertyValueFactory<>("semester")); 
        crnCol.setCellValueFactory(     new PropertyValueFactory<>("crn"));
        depCol.setCellValueFactory(     new PropertyValueFactory<>("department"));
        numCol.setCellValueFactory(     new PropertyValueFactory<>("courseNum"));
        nameCol.setCellValueFactory(    new PropertyValueFactory<>("name"));
        m_EnrollCol.setCellValueFactory(new PropertyValueFactory<>("m_enroll"));
        enrollCol.setCellValueFactory(  new PropertyValueFactory<>("enroll"));
        availCol.setCellValueFactory(   new PropertyValueFactory<>("avail"));
        waitCol.setCellValueFactory(    new PropertyValueFactory<>("waitList"));
        daysCol.setCellValueFactory(    new PropertyValueFactory<>("days"));
        startCol.setCellValueFactory(   new PropertyValueFactory<>("sTime"));
        endCol.setCellValueFactory(     new PropertyValueFactory<>("eTime"));
        buildingCol.setCellValueFactory(new PropertyValueFactory<>("building"));
        roomCol.setCellValueFactory(    new PropertyValueFactory<>("classroom"));
        teacherCol.setCellValueFactory( new PropertyValueFactory<>("prof"));
        table.setItems(data);
        
        //teacherCol.widthProperty().set(125);
        table.getColumns().addAll(semesterCol, crnCol, depCol, 
                numCol, nameCol, m_EnrollCol, enrollCol, waitCol, 
                availCol, daysCol, startCol, endCol, buildingCol, 
                roomCol, teacherCol);
        
        System.out.println(table.getWidth());
        VBox vbox = new VBox();
        vbox.setPadding(new Insets(0, 0, 0, 10));
        vbox.getChildren().addAll(label, table);
        
        return vbox;
    }
    
    private VBox configureButtons() {
        Button btnAdd = new Button("Add");
        Button btnDelete = new Button("Delete");
        Button btnEdit = new Button("Edit");

        btnAdd.setMinWidth(100.0);
        btnDelete.setMaxWidth(100.0);
        btnEdit.setMaxWidth(100.0);
        
        VBox vbox = new VBox();
        vbox.setSpacing(10);
        vbox.setPadding(new Insets(20, 20, 10, 20)); 
        vbox.getChildren().addAll(btnAdd, btnDelete, btnEdit);
        
        btnAdd.setOnAction((ActionEvent event) -> {
            addWindow();
        });
        btnDelete.setOnAction((ActionEvent event) -> {
            deleteWindow();
        });
        btnEdit.setOnAction((ActionEvent event) -> {
            editWindow();
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
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
