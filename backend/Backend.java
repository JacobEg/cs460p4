/**
 * Authors: Jacob Egestad & Cade Marks
 * Instructor: Dr. Lester McCann
 * TAs: Haris Riaz & Aayush Pinto
 * Project: Program #4: Database Design & Implementation
 * Description:
 * 
 * Known Bugs: N/A
 * Java Version: 16.0.2
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
		int newPatientId;
		if(answer.next()){ // make sure this works
			newPatientId = answer.getInt("pid") + 1;
		} else{
			newPatientId = 1;
		}
		int rowsAffected = stmt.executeUpdate(
			String.format("INSERT INTO Patient VALUES (%d, '%s', '%s', %s, '%s', '%s')",
				newPatientId, fName, lName, bursarNum, insurance, birthday));
		stmt.close();
		return rowsAffected != 0;
	}

	public static boolean addEmployee(int patientID, String acctNum, String routingNum){
		Statement stmt = dbConnect.createStatement();
		ResultSet answer = stmt.executeQuery("SELECT MAX(EmployeeID) AS eid FROM Employee");
		int newEmpID;
		if(answer.next()){ // make sure this works
			newEmpID = answer.getInt("eid") + 1;
		} else{
			newEmpID = 1;
		}
		int rowsAffected = stmt.executeUpdate(
			String.format("INSERT INTO Employee VALUES (%d, %d, %d, %d)",
			newEmpID, patientID, acctNum, routingNum));
		stmt.close();
		return rowsAffected != 0;
	}

	public static boolean addAppointment(String checkinTime, String inPerson, String service, String empId, String patientID){
		Statement stmt = dbConnect.createStatement();
		ResultSet answer = stmt.executeQuery("SELECT MAX(ApptNo) AS ano FROM Appointment");
		if(answer.next()){ // make sure this works
			newApptNo = answer.getInt("ano") + 1;
		} else{
			newApptNo = 1;
		}
		int rowsAffected = stmt.executeUpdate(
			String.format("INSERT INTO Appointment VALUES (%s, '%s', '%s', '%s', %s, %s)",
			"" + newApptNo, checkinTime, inPerson, service, empId, patientID));
		stmt.close();
		return rowsAffected != 0;
	}

	public static boolean updatePatient(){
		return false;
	}

	public static boolean updateEmployee(){
		return false;
	}
	
	public static boolean updateAppointment(){
		return false;
	}

	public static boolean deletePatient(int patientId){
		Statement stmt = dbConnect.createStatement();
		ResultSet answer = stmt.executeQuery(
			String.format("SELECT COUNT(EmployeeID) FROM " + 
			"Patient JOIN Employee USING (PatientID) WHERE PatientID=%d", patientId))
		if(answer.next()){
			System.err.println("Patient exists in Employeee table, remove employee first.");
			return false;
		}
		int rowsAffected = stmt.executeUpdate(String.format("DELETE FROM Patient WHERE PatientId=%d", patientId));
		stmt.close();
		return rowsAffected != 0;
	}

	public static boolean deleteEmployee(int empId){
		Statement stmt = dbConnect.createStatement();
		int rowsAffected = stmt.executeUpdate(String.format("DELETE FROM Employee WHERE EmployeeId=%d", empId));
		stmt.close();
		return rowsAffected != 0;
	}
	
	public static boolean deleteAppointment(int apptNo){
		Statement stmt = dbConnect.createStatement();
		int rowsAffected = stmt.executeUpdate(String.format("DELETE FROM Patient Appointment ApptNo=%d", apptNo));
		stmt.close();
		return rowsAffected != 0;
	}

	public static boolean patientExists(int patientID){
		Statement stmt = dbConnect.createStatement();
		ResultSet answer = stmt.executeQuery(
			String.format("SELECT COUNT(PatientID) AS numPatient FROM Patient WHERE PatientId=%d", patientID)
		);
		answer.next(); // make sure this works
		int count = answer.getInt("numPatient");
		stmt.close();
		return count > 0;
	}

	public static ResultSet getPatientsScheduledForCOVIDImmunization(String date){ //q1
		Statement stmt = dbConnect.createStatement();
		ResultSet answer = stmt.executeQuery(
			String.format("SELECT FName,LName FROM " +
			"Patient JOIN Appointment USING (PatientID) JOIN Immunization USING (ApptNo) " + 
			"JOIN Covid USING (INo) JOIN Scheduled USING (ApptNo)" +
			"WHERE CONVERT(date, BookTime) <= '%s' AND DoseNo > 1 AND DoseNo < 5", date));
		stmt.close();
		return answer;
	}

	public static ResultSet getPatientsWithScheduledAppts(String date){ //q2
		Statement stmt = dbConnect.createStatement();
		ResultSet answer = stmt.executeQuery(
			String.format("SELECT FName,LName FROM " +
			"Patient JOIN Appointment USING (PatientID) JOIN Scheduled USING (ApptNo) " +
			"WHERE CONVERT(date, BookTime) = '%s' " +
			"GROUP BY Service ORDER BY BookTime", date)
		);
		stmt.close();
		return answer;
	}

	public static ResultSet getStaffSchedule(){ //q3
		Statement stmt = dbConnect.createStatement();
		ResultSet answer = stmt.executeQuery(
			"SELECT FName,LName,StartTime,EndTime FROM " +
			"Patient JOIN Employee USING (PatientID) JOIN Shift USING (EmployeeID)"
		);
		stmt.close();
		return answer;
	}

	public static ResultSet getCOVIDImmunizationStats(){ //q4
		Statement stmt = dbConnect.createStatement();
		ResultSet answer = stmt.executeQuery(
			"SELECT COUNT(LName),MAX(DoseNo) AS DoseNo FROM " +
			"Patient JOIN Appointment USING (PatientId) " +
			"JOIN Immunization USING (ApptNo) JOIN Covid USING (INo) " +
			"WHERE CheckinTime <= GETDATE()"
		);
		stmt.close();
		return answer;
	}

	/**
	 * getVaccinatedForIllness: returns the ResultSet of the names of people who have been vaccinated
	 * for a given illness sorted by name
	 * Pre-conditions: init has been called
	 * Post-conditions: N/A
	 * @param illness name of illness to check who has been vaccinated for it
	 * @return ResultSet of list of people vaccinated for the illness
	 */
	public static ResultSet getVaccinatedForIllness(String illness){ //q5
		Statement stmt = dbConnect.createStatement();
		ResultSet answer = stmt.executeQuery(
			String.format("SELECT FName,LName FROM " +
			"Patient JOIN Appointment USING (PatientID) JOIN Immunization USING (ApptNo) " +
			"WHERE IType='%s' AND CheckinTime <= GETDATE()" +
			"ORDER BY Lname,FName", illness)
		);
		stmt.close();
		return answer;
	}
}
