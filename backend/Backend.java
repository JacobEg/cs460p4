/**
 * Authors: Jacob Egestad & Cade Marks
 * Instructor: Dr. Lester McCann
 * TAs: Haris Riaz & Aayush Pinto
 * Project: Program #4: Database Design & Implementation
 * Description:
 * 
 * Known Bugs: N/A
 * Version: 17.0.1
 */

package backend;

import java.sql.*;

public class Backend {
	private static Connection dbConnect = null;

	/**
	 * init: Initialize this class to establish connection to Oracle
	 * @return void
	 * Pre-conditions: N/A
	 * Post-conditions: dbConnect will be connected to the Oracle server
	 */
	public static void init(){
		try{
			Class.forName("oracle.jdbc.OracleDriver");
			dbConnect = DriverManager.getConnection("jdbc:oracle:thin:@aloe.cs.arizona.edu:1521:oracle", "egestadj", "a3670");
		} catch(Exception exception){
			exception.printStackTrace();
			System.exit(1);
		}
	}

	/**
	 * close: Close connection to Oracle db
	 * @return void
	 * Pre-conditions: dbConnect has been set to an open connection
	 * Post-conditions the connection represented by dbConnect is now closed
	 */
	public static void close(){
		try{
			dbConnect.close();
		} catch(SQLException exception){
			exception.printStackTrace();
			System.exit(1);
		}
	}

	public static boolean addPatient(String fName, String lName, String bursarNum, String insurance, String birthday){
		Statement stmt = dbConnect.createStatement();
		ResultSet answer = stmt.executeQuery("SELECT MAX(PatientID) AS pid FROM Patient");
		int newPatientId = answer.getInt("pid") + 1;
		int rowsAffected = stmt.executeUpdate(
			String.format("INSERT INTO Patient VALUES (%d, '%s', '%s', %s, '%s', '%s')",
				newPatientId, fName, lName, bursarNum, insurance, birthday));
		stmt.close();
		return rowsAffected != 0;
	}

	public static boolean addEmployee(int patientID, String acctNum, String routingNum){
		Statement stmt = dbConnect.createStatement();
		ResultSet answer = stmt.executeQuery("SELECT MAX(EmployeeID) AS eid FROM Employee");
		int newEmpID = answer.getInt("eid") + 1;
		int rowsAffected = stmt.executeUpdate(
			String.format("INSERT INTO Employee VALUES (%d, %d, %d, %d)",
			newEmpID, patientID, acctNum, routingNum));
		stmt.close();
		return rowsAffected != 0;
	}

	public static boolean addAppointment(String checkinTime, String inPerson, String service, String empId, String patientID){
		Statement stmt = dbConnect.createStatement();
		ResultSet answer = stmt.executeQuery("SELECT MAX(ApptNo) AS ano FROM Appointment");
		int newEmpID = answer.getInt("ano") + 1;
		int rowsAffected = stmt.executeUpdate(
			String.format("INSERT INTO Appointment VALUES (%s, '%s', '%s', '%s', %s, %s)",
			"" + newEmpID, checkinTime, inPerson, service, empId, patientID));
		stmt.close();
		return rowsAffected != 0;
	}

	public static boolean updatePatient(){

	}

	public static boolean updateEmployee(){

	}
	
	public static boolean updateAppointment(){

	}

	public static boolean deletePatient(int patientId){
		Statement stmt = dbConnect.createStatement();
		int rowsAffected = stmt.executeUpdate(String.format("DELETE FROM Patient WHERE PatientId='%s'", patientId));
		stmt.close();
		return rowsAffected != 0;
	}

	public static boolean deleteEmployee(int empId){
		Statement stmt = dbConnect.createStatement();
		int rowsAffected = stmt.executeUpdate(String.format("DELETE FROM Employee WHERE EmployeeId='%s'", empId));
		stmt.close();
		return rowsAffected != 0;
	}
	
	public static boolean deleteAppointment(int apptNo){
		Statement stmt = dbConnect.createStatement();
		int rowsAffected = stmt.executeUpdate(String.format("DELETE FROM Patient Appointment ApptNo='%s'", apptNo));
		stmt.close();
		return rowsAffected != 0;
	}
}
