/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package course_scheduler_beta;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
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
        Scene scene = new Scene(border, 1000, 500);
        stage.setTitle("Course Scheduler");
        stage.setScene(scene);
        stage.show();
    }
    
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
        courseTab.setContent(coursePane());        
        //set content in teacher tab
        teacherTab.setContent(teacherPane());
        //set content in classroom tab
        classroomTab.setContent(classroomPane());
        
        Scene scene = new Scene(tabPane, 600, 450);
        stage.setTitle("Add");
        stage.setScene(scene);
        stage.show();
    }
    
    //used by  Add Window
    private BorderPane coursePane()
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
        Button addBtn = new Button("Add Course");
        bPane.setBottom(addBtn);
        
        addBtn.setOnAction((ActionEvent event) -> {
            //take data from text fields, construct course obj
            //check if fields are valid?
            //Course c = new Course();
            //if((crn_tField.getText() != null && !crn_tField.getText().isEmpty()))
            //c.crn = Integer.parseInt(crn_tField.getText());
            //shove in database
                
        });
        
        return bPane;
    }
    
    //used by Add Window
    private BorderPane teacherPane()
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
        
        Label coursesAvail_label = new Label("Courses Available");
        gPane2.add(coursesAvail_label, 1,0); 
        
        ListView<String> list2 = new ListView();
        ObservableList<String> coursesAvail = FXCollections.observableArrayList("ex1","ex2");
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
        Button addBtn = new Button("Add Professor");
        bPane.setBottom(addBtn);
        
        addBtn.setOnAction((ActionEvent event) -> {
            //add teacher to database
        });
        
        return bPane;
    }
    
    private BorderPane classroomPane()
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
        Button addBtn = new Button("Add Classroom");
        bPane.setBottom(addBtn);
        
        addBtn.setOnAction((ActionEvent event) -> {
            //add classroom to database
        });
        
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
        StackPane root = new StackPane();
        Scene scene = new Scene(root, 500, 300);
        stage.setTitle("Edit");
        stage.setScene(scene);
        stage.show();
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
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
    
}
