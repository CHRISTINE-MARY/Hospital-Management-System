package hospitalManagementSystem;

import javax.sound.midi.SysexMessage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Patient {
    private Connection conn;
    private Scanner sc;

    public Patient(Connection conn,Scanner sc) {
        this.conn = conn;
        this.sc = sc;
    }
    public void addPatient(){
        System.out.println("Enter the name of the patient:");
        String name = sc.next();
        System.out.println("Enter the age of the patient:");
        int age = sc.nextInt();
        sc.nextLine();
        System.out.println("Enter the gender of the patient:");
        String gender = sc.next();


        try{
            String query="INSERT into patients(name,age,gender) values(?,?,?)";
            PreparedStatement ps=conn.prepareStatement(query);
            ps.setString(1,name);
            ps.setInt(2,age);
            ps.setString(3,gender);
            int affectedRows=ps.executeUpdate();
            if(affectedRows>0){
                System.out.println("Patient added successfully");
            }
            else{
                System.out.println("Patient not added");
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    public void viewPatient(){
        String query = "select * from patients";
        try{
            PreparedStatement ps=conn.prepareStatement(query);
            ResultSet rs=ps.executeQuery();
            System.out.println("Patients: ");
            System.out.println("+------------+--------------+-----------+----------------+");
            System.out.println("| Patient Id | Name         | Age       | Gender         |");
            System.out.println("+------------+--------------+-----------+----------------+");
            while(rs.next()){
                int id=rs.getInt("id");
                String name=rs.getString("name");
                int age=rs.getInt("age");
                String gender=rs.getString("gender");
                System.out.printf("|%-12s|%-20s|%-10s|%-12s|\n",id,name,age,gender);
                System.out.println("+------------+--------------+-----------+----------------+");
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }

    }
    public boolean getPatientById(int id){
        String query = "select * from patients where id=?";
        try{
            PreparedStatement ps=conn.prepareStatement(query);
            ps.setInt(1,id);
            ResultSet rs=ps.executeQuery();
            if(rs.next()){
                return true;
            }
            else{
                return false;
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}

