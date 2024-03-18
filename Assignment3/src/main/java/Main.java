
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class Main {
    public static void main (String[] args){
    	// Connecting to the Database
    	// 
        String url = "jdbc:postgresql://localhost:5432/Assignment3";
        String user = "postgres";
        String password = "0000";

        try{
        	// Connect to the database 
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection(url, user, password);

            
            // Retrieves and displays all records from the students table
            getAllStudents(connection);
            
            // Inserts a new student record into the students table
            addStudent(connection, "Madara", "Uchiha", "madara.uchiha@example.com", "2024-03-16");
            
            // Updates the email address for a student with the specified student_id
            updateStudentEmail(connection, 1, "updated.email@example.com");
            
            // Deletes the record of the student with the specified student_id
            deleteStudent(connection, 2); 
            
            
            // Closing the database connection when done 
            connection.close();
        }
        catch(Exception e){
        	// Exception handling 
            System.out.println(e);
        }

    }
    
 // Retrieves and displays all records from the students table
    public static void getAllStudents(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        //Executing the query to the database
        ResultSet resultSet = statement.executeQuery("SELECT * FROM students");
        
        System.out.println("All Students:");
        while (resultSet.next()) {
        	//printing out the student details
            System.out.print(resultSet.getString("first_name") + "\t");
            System.out.print(resultSet.getString("last_name") + "\t");
            System.out.print(resultSet.getString("email") + "\t");
            System.out.println(resultSet.getString("enrollment_date"));
        }
        resultSet.close();
        statement.close();
    }
    
    
 // Inserts a new student record into the students table
    public static void addStudent(Connection connection, String firstName, String lastName, String email, String enrollmentDate) throws SQLException {
        String insertSQL = "INSERT INTO students (first_name, last_name, email, enrollment_date) VALUES (?, ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(insertSQL);
        statement.setString(1, firstName);
        statement.setString(2, lastName);
        statement.setString(3, email);
        // Need to convert the given date string into a sql.java.Date object 
        java.sql.Date date = java.sql.Date.valueOf(enrollmentDate);
        
        statement.setDate(4, date);
        
        //Execute the insertion query 
        int rowsInserted = statement.executeUpdate();
        if (rowsInserted > 0) {
        	//printing out success statement
            System.out.println("A new student has been added successfully.");
        }
        statement.close();
    }

 // Updates the email address for a student with the specified student_id
    public static void updateStudentEmail(Connection connection, int studentId, String newEmail) throws SQLException {
        String sql = "UPDATE students SET email = ? WHERE student_id = ?";
        
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, newEmail);
        statement.setInt(2, studentId);
        //Execute the update query
        int rowsUpdated = statement.executeUpdate();
        if (rowsUpdated > 0) {
        	// Printing out success statement
            System.out.println("Email of student with ID " + studentId + " has been updated successfully.");
        } else {
        	// Error is the id was not found 
            System.out.println("No such student found with ID " + studentId);
        }
        statement.close();
    }
   
    
 // Deletes the record of the student with the specified student_id
    public static void deleteStudent(Connection connection, int studentId) throws SQLException {
        String sql = "DELETE FROM students WHERE student_id = ?";
        
        PreparedStatement statement = connection.prepareStatement(sql);
        
        statement.setInt(1, studentId);
        
        //Execute the deletion query 
        int rowsDeleted = statement.executeUpdate();
        if (rowsDeleted > 0) {
        	// printing out success message 
            System.out.println("Student with ID " + studentId + " has been deleted successfully.");
        } else {
        	// error if the id was not found 
            System.out.println("No such student found with ID " + studentId);
        }
        statement.close();
    }

}
