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
        // TODO code application logic here
        Course c = new Course();
        Teacher t = new Teacher();
        List courses = new ArrayList();
        String fileName1 = "src/course_scheduler/Dept1ClassData.csv";
        String fileName2 = "src/course_scheduler/Dept2ClassData.csv";
        List LinesOfFile = new ArrayList();
        
        LinesOfFile = readFile(fileName1);
        printList(LinesOfFile);
        //System.out.println(LinesOfFile);
    }
    
    // reads in the file
    public static List readFile(String fileName) {
        String line;
        List lines = new ArrayList();
        
        try {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            
            while((line = bufferedReader.readLine()) != null) {
                //String[] tokens = line.split(delimiter);
                lines.add(line);
                
                //System.out.println(tokens.length);
                //System.out.println(line);
            }
            
            bufferedReader.close();
        }
        catch(FileNotFoundException ex) {
            System.out.println("Unable to open file '" + fileName + "'");
        }
        catch(IOException ex) {
            ex.printStackTrace();
        }
        
        return lines;
    }
    
    public static void printList(List list){
        System.out.println("here");
        for(Object ele : list) {
            System.out.println(ele);
        }
    }
}
