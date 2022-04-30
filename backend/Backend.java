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

	public static int addPatient(String fName, String lName, String bursarNum, String insurance, String birthday){
		Statement stmt = dbConnect.createStatement();
		ResultSet answer = stmt.executeQuery("SELECT MAX(PatientID) AS pid FROM Patient");
		int newPatientId;
		if(answer.next()){ // make sure this works; there are records in table
			newPatientId = answer.getInt("pid") + 1;
		} else{
			newPatientId = 1;
		}
		int rowsAffected = stmt.executeUpdate(
			String.format("INSERT INTO Patient VALUES (%d, '%s', '%s', %s, '%s', '%s')",
				newPatientId, fName, lName, bursarNum, insurance, birthday));
		stmt.close();
		if(rowsAffected == 0){
			return 0;
		}
		return newPatientId;
	}

	public static int addEmployee(int patientID, String acctNum, String routingNum){
		Statement stmt = dbConnect.createStatement();
		ResultSet answer = stmt.executeQuery("SELECT MAX(EmployeeID) AS eid FROM Employee");
		int newEmpID;
		if(answer.next()){ // make sure this works; there are records in table
			newEmpID = answer.getInt("eid") + 1;
		} else{
			newEmpID = 1;
		}
		int rowsAffected = stmt.executeUpdate(
			String.format("INSERT INTO Employee VALUES (%d, %d, %d, %d)",
			newEmpID, patientID, acctNum, routingNum));
		stmt.close();
		if(rowsAffected == 0){
			return 0;
		}
		return newEmpID;
	}

	public static int addScheduled(String bookTime, String inPerson, String service, String empId, String patientID){
		Statement stmt = dbConnect.createStatement();
		ResultSet answer = stmt.executeQuery("SELECT MAX(ApptNo) AS ano FROM Appointment");
		if(answer.next()){ // make sure this works; there are records in table
			newApptNo = answer.getInt("ano") + 1;
		} else{
			newApptNo = 1;
		}
		int rowsAffected = stmt.executeUpdate(
			String.format("INSERT INTO Appointment VALUES (%s, NULL, '%s', '%s', %s, %s)",
			"" + newApptNo, inPerson, service, empId, patientID)
		);
		stmt.executeUpdate(
			String.format("INSERT INTO Scheduled ('%s', %s)", bookTime, newApptNo)
		);
		stmt.close();
		if(rowsAffected == 0){
			return 0;
		}
		return newApptNo;
	}

	public static int addWalkin(String walkinTime, String inPerson, String service, String empId, String patientID, String isEmergency){
		Statement stmt = dbConnect.createStatement();
		ResultSet answer = stmt.executeQuery("SELECT MAX(ApptNo) AS ano FROM Appointment");
		if(answer.next()){ // make sure this works; there are records in table
			newApptNo = answer.getInt("ano") + 1;
		} else{
			newApptNo = 1;
		}
		int rowsAffected = stmt.executeUpdate(
			String.format("INSERT INTO Appointment VALUES (%s, '%s', '%s', '%s', %s, %s)",
			"" + newApptNo, walkinTime, inPerson, service, empId, patientID)
		);
		stmt.executeUpdate(
			String.format("INSERT INTO Walkin ('%s', %s)", isEmergency, newApptNo)
		);
		stmt.close();
		if(rowsAffected == 0){
			return 0;
		}
		return newApptNo;
	}

	public static boolean updatePatient(int patientId, String attr, String newVal){
		Statement stmt = dbConnect.createStatement();
		String quote = "'";
		if(attr.equalsIgnoreCase("bursaracct")){ // bursaracct is int, shouldn't be quoted
			quote = "";
		}
		int rowsAffected = stmt.executeUpdate(
			String.format("UPDATE Patient SET %s=%s%s%s WHERE PatientID=%d", attr, quote, newVal, quote, patientId)
		);
		stmt.close();
		return rowsAffected != 0;
	}

	public static boolean updateEmployee(int empId, String attr, String newVal){
		Statement stmt = dbConnect.createStatement();
		int rowsAffected = stmt.executeUpdate(
			String.format("UPDATE Employee SET %s=%s WHERE EmployeeID=%d", attr, newVal, empId)
		);
		stmt.close();
		return rowsAffected != 0;
	}
	
	public static boolean updateAppointment(int apptNo, String attr, String newVal){
		String table = "Appointment";
		String quote = "'";
		if(attr.equalsIgnoreCase("booktime")){
			table = "Scheduled";
		} else if(attr.equalsIgnoreCase("isemergency")){
			table = "Walkin";
		}
		if(attr.equalsIgnoreCase("patientid") || attr.equalsIgnoreCase("employeeid")){ // these are int vals; no quotes
			quote = "";
		}
		Statement stmt = dbConnect.createStatement();
		int rowsAffected = stmt.executeUpdate(
			String.format("UPDATE %s SET %s=%s%s%s WHERE ApptNo=%s", table, attr, quote, newVal, quote, apptNo)
		);
		stmt.close();
		return rowsAffected != 0;
	}

	public static boolean deletePatient(int patientId){
		Statement stmt = dbConnect.createStatement();
		ResultSet answer = stmt.executeQuery(
			String.format("SELECT COUNT(EmployeeID) FROM " + 
			"Patient JOIN Employee USING (PatientID) WHERE PatientID=%d", patientId))
		if(answer.next()){ // patient exists in employee table
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
		int rowsAffected = stmt.executeUpdate(String.format("DELETE FROM Appointment WHERE ApptNo=%d", apptNo));
		stmt.executeUpdate(String.format("DELETE FROM Walkin WHERE ApptNo=%d", apptNo);
		stmt.executeUpdate(String.format("DELETE FROM Scheduled WHERE ApptNo=%d", apptNo);
		stmt.close();
		return rowsAffected != 0;
	}

	public static boolean patientExists(int patientID){
		Statement stmt = dbConnect.createStatement();
		ResultSet answer = stmt.executeQuery(
			String.format("SELECT COUNT(PatientID) AS numPatient FROM Patient WHERE PatientId=%d", patientID)
		);
		if(!answer.next()){ // make sure this works; there are no records in table
			return false;
		}
		int count = answer.getInt("numPatient");
		stmt.close();
		return count > 0;
	}

	public static int scheduleImmunization(int patientID, String illness, String dateTime, int dose){
		if(!patientExists(patientID)){
			System.err.println("Must create patient record before scheduling appointment for them.");
			return -1; // -1 == no patient
		}
		int apptNo = addScheduled(dateTime, "Y", "Immunization", "NULL", patientID);
		Statement stmt = dbConnect.createStatement();
		ResultSet answer = stmt.executeQuery("SELECT MAX(INo) As maxIno FROM Immunization");
		int newIno;
		if(answer.next()){ // make sure this works; there are records in table
			newIno = answer.getInt("maxIno") + 1;
		} else{
			newIno = 1;
		}
		int rowsAffected = stmt.executeUpdate(
			String.format("INSERT INTO Immunization VALUES " +
			"(%d, %d, '%s')", newIno, apptNo, illness
		);
		int rc = 0; // not covid vaccine or the patient is under 50 or the dose != 3
		if(illness.equalsIgnoreCase("covid-19")){
			stmt.executeUpdate(
				String.format("INSERT INTO Covid VALUES (%d, %d)", dose, newIno)
			);
			answer = stmt.executeQuery(
				"SELECT PatientID FROM " +
				"Patient WHERE PatientId=%d AND DATEDIFF(year, CONVERT(date, GETDATE()), Birthday) >= 50", patientID
			);
			if(answer.next() && dose == 3){ // patient is >= 50 and dose = 3
				rc = 1; // covid vaccine and patient >= 50 and dose = 3
			}
		}
		stmt.close();
		return rc; 
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
