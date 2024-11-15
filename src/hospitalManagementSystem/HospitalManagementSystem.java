package hospitalManagementSystem;

import java.sql.*;
import java.util.Scanner;

public class HospitalManagementSystem {
    private static final String  url="jdbc:mysql://localhost:3306/hospital";
    private static final String  userName="root";
    private static final String  password="root";


    public static void main(String[] args) {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }
        Scanner sc=new Scanner(System.in);
        try{
            Connection conn= DriverManager.getConnection(url,userName,password);
            Patient patient=new Patient(conn,sc);
            Doctor doctor=new Doctor(conn);
            while (true){
                System.out.println("Hospital Management System");
                System.out.println("1. Add Patient");
                System.out.println("2. View Patient");
                System.out.println("3. View Doctor");
                System.out.println("4. Book Appointment");
                System.out.println("5. Delete Patient");
                System.out.println("6. Edit appointment");
                System.out.println("7. Exit");
                System.out.println("Enter your choice");
                int choice=sc.nextInt();
                switch(choice){
                    case 1:
                        //add patient
                        patient.addPatient();
                        System.out.println();
                        return;
                    case 2:
                        //View patient
                        patient.viewPatient();
                        System.out.println();
                        return;
                    case 3:
                        //View Doctors
                        doctor.viewDoctors();
                        return;
                    case 4:
                        //Book Appointment
                        bookAppointment(patient,doctor,conn,sc);
                        System.out.println();
                        return;
                    case 5:
                        System.out.println("Enter Patient ID");
                        int patientID=sc.nextInt();
                        patient.deletePatient(patientID);
                        return;
                    case 6:
                        updateAppointment(patient,doctor,conn,sc);
                    case 7:
                        return;

                    default:
                        System.out.println("Enter valid choice");
                }
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    public static void bookAppointment(Patient patient,Doctor doctor,Connection conn,Scanner sc){
        System.out.println("Enter Patient ID: ");
        int patientID=sc.nextInt();
        System.out.println("Enter Doctor ID: ");
        int doctorID=sc.nextInt();
        System.out.println("Enter appointment date (YYYY-MM-DD): ");
        String appointmentDate=sc.next();
        if(patient.getPatientById(patientID) && doctor.getDoctorById(doctorID)){
            if(checkDoctorAvailability(doctorID,appointmentDate,conn)){
                String appointmentQuery="insert into appointments (patient_id,doctor_id,appointment_date) values(?,?,?)";
                try {
                    PreparedStatement pstmt = conn.prepareStatement(appointmentQuery);
                    pstmt.setInt(1, patientID);
                    pstmt.setInt(2, doctorID);
                    pstmt.setString(3, appointmentDate);
                    int affectedRows=pstmt.executeUpdate();
                    if(affectedRows>0){
                        System.out.println("Appointment Booked Successfully");
                    }else{
                        System.out.println("Appointment Booking Failed");
                    }
                }
                catch(SQLException e){
                    e.printStackTrace();
                }

            }else{
                System.out.println("Doctor is not available");
            }
        }
        else{
            System.out.println("either doctor or patient does not exist");
        }
    }
    public static boolean checkDoctorAvailability(int doctorID,String appointmentDate,Connection conn){
        String query="select count(*) from appointments where doctor_id=? and appointment_date=?";
        try{
            PreparedStatement ps= conn.prepareStatement(query);
            ps.setInt(1, doctorID);
            ps.setString(2, appointmentDate);
            ResultSet rs=ps.executeQuery();
            if(rs.next()){
                int count=rs.getInt(1);
                if(count==0){
                    return true;
                }
                else{
                    return false;
                }
            }

        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }
    public static void updateAppointment(Patient patient,Doctor doctor,Connection conn,Scanner sc){
        System.out.println("Enter Patient ID: ");
        int patientID=sc.nextInt();
        System.out.println("Enter Doctor ID: ");
        int doctorID=sc.nextInt();
        System.out.println("Enter new appointment date (YYYY-MM-DD): ");
        String appointmentDate=sc.next();
        if(patient.getPatientById(patientID) && doctor.getDoctorById(doctorID)){
            String query="update appointments set appointment_date=? where patient_id=? and doctor_id=?";
            try{
                PreparedStatement ps= conn.prepareStatement(query);
                ps.setString(1, appointmentDate);
                ps.setInt(2, patientID);
                ps.setInt(3, doctorID);
                int affectedRows=ps.executeUpdate();
                if(affectedRows>0){
                    System.out.println("Appointment edited Successfully");
                }
                else{
                    System.out.println("Appointment Editing Failed");
                }
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
    }

}
