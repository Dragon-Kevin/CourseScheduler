/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package course_scheduler_beta;

import java.io.File;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *
 * @author mv0003
 */
public class Course_Scheduler_Beta extends Application {
    
    @Override
    public void start(Stage stage) {
        stage.setResizable(false);
        FileChooser fileChooser = new FileChooser();
        Button btnOpenCSV = new Button();
        
        btnOpenCSV.setText("Open");
        
        
        StackPane root = new StackPane();
        root.getChildren().add(btnOpenCSV);
        
        Scene scene = new Scene(root, 300, 250);
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
        stage.setTitle("Welcome!");
        stage.setScene(scene);
        stage.show();
    }
    
    public void mainWindow(Parser data){
        Stage stage = new Stage();
        stage.setResizable(false);
        BorderPane border = new BorderPane();
        border.setPadding(new Insets(20, 20, 20, 20));
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
        BorderPane border = new BorderPane();
        Button btnAdd = new Button();
        Button btn1 = new Button();
        Button btn2 = new Button();
        
        btnAdd.setMinWidth(100.0);
        btn1.setMinWidth(100.0);
        btn2.setMinWidth(100.0);
        
        HBox vbox = new HBox();
        vbox.setSpacing(10);
        vbox.setPadding(new Insets(20, 20, 20, 20)); 
        vbox.getChildren().addAll(btnAdd, btn1, btn2);
        
        border.setPadding(new Insets(20,20,20,20));
        border.setBottom(vbox);
        Scene scene = new Scene(border, 500, 300);
        stage.setTitle("Add");
        stage.setScene(scene);
        stage.show();
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
        ObservableList<Teacher> data = FXCollections.observableArrayList(parser.getTeachers());
        TableColumn crnCol = new TableColumn("CRN");
        TableColumn yearCol = new TableColumn("Year");
        TableColumn semesterCol = new TableColumn("Semester");
        TableColumn courseCol = new TableColumn("Course");
        TableColumn teacherCol = new TableColumn("Teacher");
        teacherCol.setCellValueFactory(
                new PropertyValueFactory<>("name"));
        TableColumn buildingCol = new TableColumn("Building");
        TableColumn roomCol = new TableColumn("Room #");
        TableColumn timeCol = new TableColumn("Time");
        
        table.setItems(data);
        table.getColumns().addAll(crnCol, yearCol, semesterCol, courseCol, teacherCol, buildingCol, roomCol, timeCol);
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
