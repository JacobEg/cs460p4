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
		} catch(Exception exception){
			exception.printStackTrace();
			//System.exit(1);
		}
	}

	/**
	 * addPatient: Inserts new patient into Patient table in db
	 * @param fName patient's first name
	 * @param lName patient's last name
	 * @param bursarNum bursar account num
	 * @param insurance patient's insurance
	 * @param birthday patient's date of birth
	 * @return the id of the newly-added patient (0 if failed)
	 * Pre-condition: init has been called
	 * Post-condition: patient will be added into Patient table
	 */
	public static int addPatient(String fName, String lName, String bursarNum, String insurance, String birthday){
		int newPatientId = 0; // newly-inserted patient's id
		int rowsAffected = 0; // the number of rows affected by insert statement
		try{
			System.out.println("addPatient\n	creating statement and assigning id num");
			Statement stmt = dbConnect.createStatement(); // for querying db
			ResultSet answer = stmt.executeQuery("SELECT MAX(PatientID) AS pid FROM Patient"); // for getting patient id
			if(answer.next()){ // make sure this works; there are records in table
				System.out.println("	Getting new id for patient by adding 1");
				newPatientId = answer.getInt("pid") + 1;
			} else{
				System.out.println("	Assigning patient id to 1");
				newPatientId = 1;
			}
			rowsAffected = stmt.executeUpdate(
				String.format("INSERT INTO Patient VALUES (%d, '%s', '%s', %s, '%s', DATE '%s')",
					newPatientId, fName, lName, bursarNum, insurance, birthday));
			System.out.println("	Rows affected in: " + rowsAffected);
			stmt.close();
		} catch(Exception exception){
			exception.printStackTrace();
			return 0;
		}
		if(rowsAffected == 0){
			return 0;
		}
		return newPatientId;
	}

	/**
	 * addEmployee: inserts new employee into Employee table
	 * @param patientID employee's patient id
	 * @param acctNum employee's bank account number
	 * @param routingNum employee's routing number
	 * @return newly-inserted employee's id number (0 if failed)
	 * Pre-condition: init has been called
	 * Post-condition: employee is added into Employee table
	 */
	public static int addEmployee(int patientID, String acctNum, String routingNum){
		int newEmpID = 0; // newly-inserted employee's id number
		int rowsAffected = 0; // number of rows affected by insert statement
		try {
			System.out.println("addEmployee\n	creating statement and assigining id num");
			Statement stmt = dbConnect.createStatement(); // for querying db
			ResultSet answer = stmt.executeQuery("SELECT MAX(EmployeeID) AS eid FROM Employee"); // for getting new id num
			
			if(answer.next()){ // make sure this works; there are records in table
				System.out.println("	Getting new id for emp by adding 1");
				newEmpID = answer.getInt("eid") + 1;
			} else{
				System.out.println("	Setting emp id to 1");
				newEmpID = 1;
			}
			rowsAffected = stmt.executeUpdate(
				String.format("INSERT INTO Employee VALUES (%d, %d, %s, %s)",
				newEmpID, patientID, acctNum, routingNum));
			System.out.println("	rows affected: " + rowsAffected);
			stmt.close();
		} catch(Exception exception){
			exception.printStackTrace();
			return 0;
		}
		if(rowsAffected == 0){
			return 0;
		}
		return newEmpID;
	}

	/**
	 * addScheduled: inserts info into appointment and scheduled tables
	 * @param bookTime time the appointment is scheduled for
	 * @param inPerson whether the appointment will be in person or not ('Y' or 'N')
	 * @param service the service the patient wants (Immunization, CAPS, General Medicine, Laboratory  & Testing)
	 * @param empId the employee the appointment will be with
	 * @param patientID the id of the patient
	 * @return newly added appointment's number
	 * Pre-condition: init has been called
	 * Post-condition: new appointment inserted into Appointment and Schedudled tables (0 if failed)
	 */
	public static int addScheduled(String bookTime, String inPerson, String service, String empId, String patientID){
		int newApptNo = 0; // newly created appointment's number
		int rowsAffected = 0; // number of rows affected by insert statement
		try{
			System.out.println("addScheduled\n	creating statement and assigining appt num");
			Statement stmt = dbConnect.createStatement(); // for querying db
			ResultSet answer = stmt.executeQuery("SELECT MAX(ApptNo) AS ano FROM Appointment"); // for getting new id number
			if(answer.next()){ // make sure this works; there are records in table
				System.out.println("	getting new appt no by adding 1");
				newApptNo = answer.getInt("ano") + 1;
			} else{
				System.out.println("	setting appt no to 1");
				newApptNo = 1;
			}
			rowsAffected = stmt.executeUpdate(
				String.format("INSERT INTO Appointment VALUES (%d, NULL, '%s', '%s', %s, %s)",
				newApptNo, inPerson, service, empId, patientID)
			);
			System.out.println("	rows affected: " + rowsAffected);
			stmt.executeUpdate(
				String.format("INSERT INTO Scheduled VALUES (TIMESTAMP '%s', %s)", bookTime, newApptNo)
			);
			stmt.close();
		} catch(Exception exception){
			exception.printStackTrace();
			return 0;
		}
		if(rowsAffected == 0){
			return 0;
		}
		return newApptNo;
	}

	/**
	 * addWalkin: Add info into Walkin and Appointment tables
	 * @param walkinTime time the patient walked in
	 * @param inPerson whether the appointment will be in person or not ('Y' or 'N')
	 * @param service the service the patient wants (Immunization, CAPS, General Medicine, Laboratory  & Testing)
	 * @param empId the employee the appointment will be with
	 * @param patientID the id of the patient
	 * @return newly added appointment's number
	 * Pre-condition: init has been called
	 * Post-condition: new appointment inserted into Appointment and Walkin tables
	 */
	public static int addWalkin(String walkinTime, String inPerson, String service, String empId, String patientID, String isEmergency){
		int newApptNo = 0; // newly created appointment's number
		int rowsAffected = 0; // number of rows affected by insert statement
		try{
			System.out.println("addWalkin\n	creating statement and assigining appt num");
			Statement stmt = dbConnect.createStatement(); // for querying db
			ResultSet answer = stmt.executeQuery("SELECT MAX(ApptNo) AS ano FROM Appointment"); // for getting new id number
			if(answer.next()){ // make sure this works; there are records in table
				System.out.println("	adding 1 to get appt no");
				newApptNo = answer.getInt("ano") + 1;
			} else{
				System.out.println("	setting appt no to 1");
				newApptNo = 1;
			}
			rowsAffected = stmt.executeUpdate(
				String.format("INSERT INTO Appointment VALUES (%d, TIMESTAMP '%s', '%s', '%s', %s, %s)",
				newApptNo, walkinTime, inPerson, service, empId, patientID)
			);
			System.out.println("	rows affected: " + rowsAffected);
			stmt.executeUpdate(
				String.format("INSERT INTO Walkin VALUES ('%s', %d)", isEmergency, newApptNo)
			);
			stmt.close();
		} catch(Exception exception){
			exception.printStackTrace();
			return 0;
		}
		if(rowsAffected == 0){
			return 0;
		}
		return newApptNo;
	}

	/**
	 * updatePatient: updates an attribute of a patient with a new value
	 * @param patientId id of patient to update
	 * @param attr attribute to update
	 * @param newVal value to update
	 * @return true if successfully updated, else false
	 * Pre-condition: init has been called
	 * Post-condition: Patient's attr has been updated
	 */
	public static boolean updatePatient(int patientId, String attr, String newVal){
		int rowsAffected = 0; // for checking whether update successful
		try{
			Statement stmt = dbConnect.createStatement(); // for querying db
			String quote = "'"; // "'" if attr is str, else ""
			if(attr.equalsIgnoreCase("bursaracct")){ // bursaracct is int, shouldn't be quoted
				quote = "";
			}
			rowsAffected = stmt.executeUpdate(
				String.format("UPDATE Patient SET %s=%s%s%s WHERE PatientID=%d", attr, quote, newVal, quote, patientId)
			);
			stmt.close();
		} catch(Exception exception){
			exception.printStackTrace();
			return false;
		}
		return rowsAffected != 0;
	}

	/**
	 * updateEmployee: updates the attribute of an employee with a new value
	 * @param empId id of employee to update
	 * @param attr attribute of employee to update
	 * @param newVal new value to assign to attribute
	 * @return true if successful update else false
	 * Pre-condition: init called
	 * Post-condition: employee's attriute has been successfully updated
	 */
	public static boolean updateEmployee(int empId, String attr, String newVal){
		int rowsAffected = 0; // for determining whether update successful
		try{
			Statement stmt = dbConnect.createStatement(); // for querying db
			rowsAffected = stmt.executeUpdate(
				String.format("UPDATE Employee SET %s=%s WHERE EmployeeID=%d", attr, newVal, empId)
			);
			stmt.close();
		} catch (Exception exception){
			exception.printStackTrace();
			return false;
		}
		return rowsAffected != 0;
	}
	
	/**
	 * updateAppointment: update Appointment, Scheduled, or Walkin tables with a new value for an attribute
	 * @param apptNo appointment number to update
	 * @param attr attribute to update
	 * @param newVal new value to use
	 * @return true if successful, else false
	 * Pre-condition: init is called
	 * Post-condition: appointment has been updated
	 */
	public static boolean updateAppointment(int apptNo, String attr, String newVal){
		int rowsAffected = 0; // for determining whether it was successful
		try{
			String table = "Appointment"; // might have to update sub-table
			String quote = "'"; // "'" if str or date, else ""
			if(attr.equalsIgnoreCase("booktime")){
				table = "Scheduled";
			} else if(attr.equalsIgnoreCase("isemergency")){
				table = "Walkin";
			}
			if(attr.equalsIgnoreCase("patientid") || attr.equalsIgnoreCase("employeeid")){ // these are int vals; no quotes
				quote = "";
			}
			Statement stmt = dbConnect.createStatement(); // for querying db
			rowsAffected = stmt.executeUpdate(
				String.format("UPDATE %s SET %s=%s%s%s WHERE ApptNo=%s", table, attr, quote, newVal, quote, apptNo)
			);
			stmt.close();
		} catch (Exception exception){
			exception.printStackTrace();
			return false;
		}
		return rowsAffected != 0;
	}

	/**
	 * deletePatient: delete given patient from db
	 * @param patientId id of patient to delete
	 * @return true if successful delete, else false
	 * Pre-condition: init is called
	 * Post-condition: patient has been deleted
	 */
	public static boolean deletePatient(int patientId){
		int rowsAffected = 0; // for determining if delete was successful
		try{
			Statement stmt = dbConnect.createStatement(); // for querying db
			ResultSet answer = stmt.executeQuery( // for making sure employee has already been deleted
				String.format("SELECT COUNT(EmployeeID) FROM " + 
				"Patient JOIN Employee USING (PatientID) WHERE PatientID=%d", patientId));
			if(answer.next()){ // patient exists in employee table
				System.err.println("Patient exists in Employeee table, remove employee first.");
				return false;
			}
			rowsAffected = stmt.executeUpdate(String.format("DELETE FROM Patient WHERE PatientId=%d", patientId));
			stmt.close();
		} catch(Exception exception){
			exception.printStackTrace();
			return false;
		}
		return rowsAffected != 0;
	}

	/**
	 * deleteEmployee: delete the given employee from the table
	 * @param empId id of employee to delete
	 * @return true if successful delete, else false
	 * Pre-condition: init is called
	 * Post-condition: employee has been deleted
	 */
	public static boolean deleteEmployee(int empId){
		int rowsAffected = 0; // for determining if successful delete
		try{
			Statement stmt = dbConnect.createStatement(); // for querying db
			rowsAffected = stmt.executeUpdate(String.format("DELETE FROM Employee WHERE EmployeeId=%d", empId));
			stmt.close();
		} catch(Exception exception){
			exception.printStackTrace();
			return false;
		}
		return rowsAffected != 0;
	}
	
	/**
	 * deleteAppointment: delete appointment from Appointment and Walkin/Scheduled tables
	 * @param apptNo number of appointment to delete
	 * @return true if successfully deleted, else false
	 * Pre-condition: init is called
	 * Post-condition: appointment has been deleted
	 */
	public static boolean deleteAppointment(int apptNo){
		int rowsAffected = 0; // for determining if successful delete
		try{
			Statement stmt = dbConnect.createStatement(); // for querying db
			rowsAffected = stmt.executeUpdate(String.format("DELETE FROM Appointment WHERE ApptNo=%d", apptNo));
			stmt.executeUpdate(String.format("DELETE FROM Walkin WHERE ApptNo=%d", apptNo));
			stmt.executeUpdate(String.format("DELETE FROM Scheduled WHERE ApptNo=%d", apptNo));
			stmt.close();
		} catch(Exception exception){
			exception.printStackTrace();
			return false;
		}
		return rowsAffected != 0;
	}

	/**
	 * patientExists: determine if patient with given id exists
	 * @param patientID id of patient to check exists
	 * @return true if patient exists, else false
	 * Pre-condition: init is called
	 * Post-condition: n/a
	 */
	public static boolean patientExists(int patientID){
		int count = 0; // used for checking if patient exists
		try{
			Statement stmt = dbConnect.createStatement(); // for querying db
			ResultSet answer = stmt.executeQuery( // for setting count
				String.format("SELECT COUNT(PatientID) AS numPatient FROM Patient WHERE PatientId=%d", patientID)
			);
			if(!answer.next()){ // make sure this works; there are no records in table
				return false;
			}
			count = answer.getInt("numPatient");
			stmt.close();
		} catch(Exception exception){
			exception.printStackTrace();
			return false;
		}
		return count > 0;
	}

	/**
	 * exployeeExists: determine if employee with given id exists
	 * @param empID id of employee to check exists
	 * @return true if employee exists, else false
	 * Pre-condition: init is called
	 * Post-condition: n/a
	 */
	public static boolean employeeExists(int empID){
		int count = 0; // used for checking if employee exists
		try{
			Statement stmt = dbConnect.createStatement(); // for querying db
			ResultSet answer = stmt.executeQuery( // for setting count
				String.format("SELECT COUNT(EmployeeID) AS numEmp FROM Employee WHERE EmployeeID=%d", empID)
			);
			if(!answer.next()){ // make sure this works; there are no records in table
				return false;
			}
			count = answer.getInt("numEmp");
			stmt.close();
		} catch(Exception exception){
			exception.printStackTrace();
			return false;
		}
		return count > 0;
	}

	/**
	 * addShift: add informtation into shift table
	 * @param empId id of employee to assign a shift
	 * @param startTime start time of shift
	 * @param endTime end time of shift
	 * @param service service employee is providing (CAPS, Immunization, Laboratory & Testing, General Medicine)
	 * @return true if shift added successfully, else false
	 * Pre-condition: init is called
	 * Post-condition: shift has been added
	 */
	public static boolean addShift(int empId, String startTime, String endTime, String service){
		if(!employeeExists(empId)){
			System.err.println("Employee with an id of " + empId + " does not exist");
			return false;
		}
		int rowsAffected = 0; // for checking if insert successful
		try{
			Statement stmt = dbConnect.createStatement(); // for querying db
			rowsAffected = stmt.executeUpdate(
				String.format("INSERT INTO Shift VALUES (%d, TIMESTAMP '%s', TIMESTAMP '%s', '%s')", empId, startTime, endTime, service)
			);
			stmt.close();
		} catch (Exception exception){
			exception.printStackTrace();
			return false;
		}
		return rowsAffected != 0;
	}

	/**
	 * scheduleImmunization: schedules an immunization appointment for a patient
	 * @param patientID id of patient to schedule
	 * @param illness illness they are getting vaccinated for
	 * @param dateTime appointment time
	 * @param dose dose number (if covid, otherwise not used)
	 * @return -2 if SQLException, -1 if patient doesn't exist, 0 if not a covid vaccine or the
	 * patient is under 50 or the dose != 3, 1 if it's a covid vaccine and the dose == 3 and the
	 * patient is over 50
	 * Pre-condition: init is called
	 * Post-condition: immunization has been scheduled
	 */
	public static int scheduleImmunization(int patientID, String illness, String dateTime, int dose){
		if(!patientExists(patientID)){
			System.err.println("Must create patient record before scheduling appointment for them.");
			return -1; // -1 == no patient
		}
		int apptNo = addScheduled(dateTime, "Y", "Immunization", "NULL", "" + patientID); // appointment number of immunization
		int rc = 0; // not covid vaccine or the patient is under 50 or the dose != 3
		try{
			Statement stmt = dbConnect.createStatement(); // for querying db
			ResultSet answer = stmt.executeQuery("SELECT MAX(INo) As maxIno FROM Immunization"); // for getting new INo
			int newIno; // new INo for Immunization table
			if(answer.next()){ // make sure this works; there are records in table
				newIno = answer.getInt("maxIno") + 1;
			} else{
				newIno = 1;
			}
			stmt.executeUpdate(
				String.format("INSERT INTO Immunization VALUES " +
				"(%d, %d, '%s')", newIno, apptNo, illness)
			);
			if(illness.equalsIgnoreCase("covid-19")){
				stmt.executeUpdate(
					String.format("INSERT INTO Covid VALUES (%d, %d)", dose, newIno)
				);
				answer = stmt.executeQuery(
					String.format(
						"SELECT PatientID FROM Patient WHERE PatientId=%d AND " +
						"floor(months_between(CAST(SYSDATE AS DATE), Birthday) / 12) >= 50", patientID
					)
				);
				if(answer.next() && dose == 3){ // patient is >= 50 and dose = 3
					rc = 1; // covid vaccine and patient >= 50 and dose = 3; must schedule 4th
				}
			}
			stmt.close();
		} catch(Exception exception){
			exception.printStackTrace();
			return -2; // some SQLException
		}
		return rc; 
	}

	/**
	 * getPatientsScheduledForCOVIDImmunization: returns ResultSet of patients who have their second,
	 * third, or fourth covid doeses by the given date
	 * @param date date to check who has been vaccinated by then
	 * @return ResultSet with answers from query
	 * Pre-condition: init is called
	 * Post-condition: n/a
	 */
	public static ResultSet getPatientsScheduledForCOVIDImmunization(String date){ //q1
		ResultSet answer = null; // for returning results from query
		try{
			Statement stmt = dbConnect.createStatement(); // for querying db
			answer = stmt.executeQuery(
				String.format("SELECT distinct Patient.PatientID,FName,LName FROM " +
				"Patient JOIN Appointment USING (PatientID) JOIN Immunization USING (ApptNo) " + 
				"JOIN Covid USING (INo) JOIN Scheduled USING (ApptNo)" +
				"WHERE CAST(BookTime AS DATE) <= '%s' AND DoseNo > 1 AND DoseNo < 5", date));
			stmt.close();
		} catch(Exception exception){
			exception.printStackTrace();
			return null;
		}
		return answer;
	}

	/**
	 * getPatientsWithScheduledAppts: gets patients with a scheduled appointment for a given
	 * day, grouped by the service and ordered by the booked time
	 * @param date date to check appointments for
	 * @return ResultSet with answers from query
	 * Pre-condition: init is called
	 * Post-condition: n/a
	 */
	public static ResultSet getPatientsWithScheduledAppts(String date){ //q2
		ResultSet answer = null; // for returning results from query
		try{
			Statement stmt = dbConnect.createStatement(); // for querying db
			answer = stmt.executeQuery(
				String.format("SELECT FName,LName FROM " +
				"Patient JOIN Appointment USING (PatientID) JOIN Scheduled USING (ApptNo) " +
				"WHERE CAST(BookTime AS DATE) = DATE '%s' " +
				"GROUP BY Service ORDER BY BookTime", date)
			);
			stmt.close();
		} catch(Exception exception){
			exception.printStackTrace();
			return null;
		}
		return answer;
	}

	/**
	 * getStaffSchedule: get the schedule of the staff on a given date
	 * @param date the date to get the schedule for
	 * @return ResultSet with results from query
	 * Pre-condition: init is called
	 * Post-condition: n/a
	 */
	public static ResultSet getStaffSchedule(String date){ //q3
		ResultSet answer = null; // for returning results from query
		try{
			Statement stmt = dbConnect.createStatement(); // for querying db
			answer = stmt.executeQuery(
				String.format("SELECT FName,LName,StartTime,EndTime FROM " +
				"Patient JOIN Employee USING (PatientID) JOIN Shift USING (EmployeeID) " +
				"WHERE CAST(StartTime AS DATE) = DATE '%s' OR CAST(EndTime AS DATE) = DATE '%s'", date, date)
			);
			stmt.close();
		} catch(Exception exception){
			exception.printStackTrace();
			return null;
		}
		return answer;
	}

	/**
	 * getCOVIDImmunizationStats: Get vaccinated statistings for covid (# of people with each dose)
	 * for students and employees
	 * @return ResultSets with results from queries 0th index is students, 1st index is employees
	 * Pre-conditions: init has been called
	 * Post-conditions: N/A
	 */
	public static ResultSet[] getCOVIDImmunizationStats(){ //q4
		ResultSet[] answer = {null, null}; // for returning results from query
		try{
			Statement stmt = dbConnect.createStatement(); //for querying db
			answer[0] = stmt.executeQuery(
				"SELECT COUNT(Patient.PatientID) AS Patients,MAX(DoseNo) AS DoseNo FROM " +
				"Patient JOIN Appointment USING (PatientId) " +
				"JOIN Immunization USING (ApptNo) JOIN Covid USING (INo) " +
				"WHERE CheckinTime <= SYSDATE AND InsuranceProvider<>'UA'"
			);
			answer[1] = stmt.executeQuery(
				"SELECT COUNT(Patient.PatientID) AS Patients,MAX(DoseNo) AS DoseNo FROM " +
				"Patient JOIN Appointment USING (PatientId) " +
				"JOIN Immunization USING (ApptNo) JOIN Covid USING (INo) " +
				"WHERE CheckinTime <= SYSDATE AND InsuranceProvider='UA'"
			);
			stmt.close();
		} catch(Exception exception){
			exception.printStackTrace();
			return null;
		}
		return answer;
	}

	/**
	 * getVaccinatedForIllness: returns the ResultSet of the names of people who have been vaccinated
	 * for a given illness sorted by name
	 * @param illness name of illness to check who has been vaccinated for it
	 * @return ResultSet of list of people vaccinated for the illness
	 * Pre-conditions: init has been called
	 * Post-conditions: N/A
	 */
	public static ResultSet getVaccinatedForIllness(String illness){ //q5
		ResultSet answer = null; // for returning results from query
		try{
			Statement stmt = dbConnect.createStatement(); // for querying db
			answer = stmt.executeQuery(
				String.format("SELECT FName,LName FROM " +
				"Patient JOIN Appointment USING (PatientID) JOIN Immunization USING (ApptNo) " +
				"WHERE IType='%s' AND CheckinTime <= SYSDATE" +
				"ORDER BY Lname,FName", illness)
			);
			stmt.close();
		} catch(Exception exception){
			exception.printStackTrace();
			return null;
		}
		return answer;
	}
}
