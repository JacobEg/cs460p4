/**
 * Frontend.java
 * Authors: Jacob Egestad & Cade Marks
 * Course: CSC 460 - Database Design
 * Instructor: Dr. Lester McCann
 * TAs: Haris Riaz & Aayush Pinto
 * Assignment: Program #4: Database Design and Implementation
 * Due: May 2, 2022
 * Description: This file represents the frontend of Program 4. It produces a basic text interface
 *              which allows the user to make requests that are then translated to SQL statements
 *              executed on the Database by Backend.java using JDBC. User requests are accepted by
 *              first displaying a menu of options for inserting, deleting, updating records, and
 *              making five different preset SQL queries to retrieve information from the DB. Some
 *              of these SQL queries take input from the user, such as a specific date.
 * Operational Requirements: Java 16.0.2
 * Missing Features and Bugs: N/A
 */

package frontend;

import backend.Backend;

import java.sql.ResultSet;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Frontend {
    public static void main(String[] args) {
        //Backend.init();
        promptUser();
        //Backend.close();
    }

    /**
     * promptUser: Prints a text menu that prompts the user to select the insert, delete, update,
     *             or query function by entering the corresponding number, or -1 to terminate the
     *             program.
     *
     * @return void
     * Pre-conditions: N/A
     * Post-conditions: scanner is closed
     */
    private static void promptUser() {
        System.out.println("1. Insert record\n2. Delete record\n3. Update record\n4. Query database\n-1:EXIT");
        Scanner scanner = new Scanner(System.in); // user's selection from text menu
        System.out.println("Choose an option from the menu: ");
        int option = Integer.parseInt(scanner.nextLine()); // user's selection from text menu
        while (option != -1) {
            switch (option) {
                case 1: // Insert record
                    insert(scanner);
                    break;
                case 2: // Delete record
                    delete(scanner);
                    break;
                case 3: // Update record
                    update(scanner);
                    break;
                case 4: // Query database
                    query(scanner);
                    break;
                default:
                    System.out.println("1. Insert record\n2. Delete record\n3. Update record\n4. Query database\n-1:EXIT");
                    System.out.println("Choose an option from the menu: ");
                    option = Integer.parseInt(scanner.nextLine());
            }
        }
        scanner.close();
    }

    /**
     * insert: Allows user to add a record to the Patient, Employee, or Appointment table. The
     *         user will be asked to provide values for each attribute in the record, and prompted
     *         again if those attributes are not of the correct type. At any point, the user can
     *         return to the previous menu. The id of the record is generated by the back-end.
     *
     * @param scanner The user input scanner
     * @return void
     * Pre-conditions: scanner is an open Scanner object
     * Post-conditions: N/A
     */
    private static void insert(Scanner scanner) {
        System.out.println("Insert a(n)...\n  1. Patient\n  2. Employee\n  3. Appointment\n 4. Return to options menu");
        int option = Integer.parseInt(scanner.nextLine()); // user's selection from text menu
        int patientID; // patientID attribute
        while (option != 4) {
            switch (option) {
                case 1: // insert Patient record
                    // verifies patient fname is properly formatted
                    String fname; // Patient's first name
                    while(true) {
                        System.out.print("Enter the patient's first name: ");
                        fname = scanner.nextLine();
                        if (fname.length() > 30) {
                            System.out.println("\nPatient's first name must be 30 characters or less");
                        } else {
                            break;
                        }
                    }

                    // verifies patient lname is properly formatted
                    String lname; // Patient's last name
                    while(true) {
                        System.out.print("Enter the patient's last name: ");
                        lname = scanner.nextLine();
                        if (fname.length() > 30) {
                            System.out.println("\nPatient's last name must be 30 characters or less");
                        } else {
                            break;
                        }
                    }

                    // verifies patient bursar acct number is properly formatted
                    String bursar; // Patient's bursar account number
                    while(true) {
                        System.out.print("Enter the patient's bursar account number: ");
                        bursar = scanner.nextLine();
                        try {
                            int bursarInt = Integer.parseInt(bursar); // Integer form of bursar acct number
                        } catch (Exception e) {
                            System.out.println("Bursar account number must be an integer.");
                            continue;
                        }
                        break;
                    }

                    // verifies patient insurance provider is properly formatted
                    String provider; // Patient's insurance provider
                    while(true) {
                        System.out.print("Enter the patient's insurance provider: ");
                        provider = scanner.nextLine();
                        if (provider.length() > 50) {
                            System.out.println("\nInsurance provider name must be 50 characters or less");
                        } else {
                            break;
                        }
                    }

                    // verifies patient birthdate is properly formatted
                    String birth; // Patient's birthdate
                    Pattern date = Pattern.compile("\\d\\d\\d\\d-\\d\\d-\\d\\d"); // regex pattern for SQL date type
                    Matcher dateMatcher; // SQL date type pattern matcher
                    while(true) {
                        System.out.print("Enter the patient's birthdate (YYYY-MM-DD): ");
                        birth = scanner.nextLine();
                        dateMatcher = date.matcher(birth);
                        if (!dateMatcher.find() || birth.length() != 10) {
                            System.out.println("\nBirthdate must be in form 'YYYY-MM-DD'");
                        } else {
                            break;
                        }
                    }

                    // insert Patient record
                    Backend.addPatient(fname, lname, bursar, provider, birth);
                    break;

                case 2: // insert Employee record
                    // verifies employee's patientID is properly formatted and in Patient table
                    while(true) {
                        System.out.print("Enter the employee's patientID: ");
                        try {
                            patientID = Integer.parseInt(scanner.nextLine());
                        } catch (Exception e) {
                            System.out.println("patientID must be an integer.");
                            continue;
                        }
                        if (Backend.patientExists(patientID)) {
                            break;
                        } else {
                            System.out.println("Employee's patientID must match an existing Patient record.");
                        }
                    }

                    // verifies employee acct number is properly formatted
                    String acct; // Employee's direct deposit account number
                    while(true) {
                        System.out.print("Enter the employee's direct deposit account number: ");
                        acct = scanner.nextLine();
                        try {
                            int acctInt = Integer.parseInt(acct); // Integer form of acct number
                        } catch (Exception e) {
                            System.out.println("Account number must be an integer.");
                            continue;
                        }
                        break;
                    }

                    // verifies employee routing number is properly formatted
                    String rout; // Employee's direct deposit routing number
                    while(true) {
                        System.out.print("Enter the employee's direct deposit routing number: ");
                        rout = scanner.nextLine();
                        try {
                            int acctInt = Integer.parseInt(rout); // Integer form of routing number
                        } catch (Exception e) {
                            System.out.println("Routing number must be an integer.");
                            continue;
                        }
                        break;
                    }

                    // insert Employee record
                    Backend.addEmployee(patientID, acct, rout);
                    break;

                case 3: // insert Appointment record
                    System.out.println("Is this a booked appointment or a walk-in appointment?\n  1. Booked\n  2. Walk-in");
                    int apptOption = Integer.parseInt(scanner.nextLine()); // user's selection from text menu

                    if (apptOption == 1) {
                        checkImmun(scanner);
                        while (true) {
                            System.out.println("Is this appointment for an immunization? ('Y'/'N')");
                            String immunAppt = scanner.nextLine(); // Whether appointment is for immunization
                            if (immunAppt.equals("Y")) {
                                checkImmun(scanner);
                                break;
                            } else if (immunAppt.equals("N")) {
                                break;
                            } else {
                                System.out.println("Only 'Y' and 'N' accepted for input");
                            }
                        }
                    }

                    // verifies the value for inPerson is formatted correctly
                    String inPerson; // Whether appointment is in person (or remote)
                    while(true) {
                        System.out.println("Is this appointment in person? ('Y'/'N')");
                        inPerson = scanner.nextLine();
                        if (inPerson.equals("Y") || inPerson.equals("N"))
                            break;
                        System.out.println("In-person status must be input as 'Y' or 'N'");
                    }

                    // verifies the value for service is formatted correctly
                    String service; // Name of Campus Health service
                    while(true) {
                        System.out.println("Enter the Campus Health Service for the appointment: ");
                        service = scanner.nextLine();
                        if (service.equals("CAPS") || service.equals("Immunization") ||
                                service.equals("Laboratory and Testing") || service.equals("General Medicine"))
                        break;
                        System.out.println("Service must be 'CAPS', 'Immunization', 'Laboratory and Testing', or 'General Medicine'.");
                    }

                    // verifies the value for employeeID is formatted correctly
                    String employeeID; // Employee ID of employee working the appointment
                    while(true) {
                        System.out.println("Enter the employeeID of the employee working the appointment: ");
                        employeeID = scanner.nextLine();
                        if (employeeID.equals("NULL") || Backend.employeeExists(Integer.parseInt(employeeID))) {
                            break;
                        }
                        System.out.println("Employee could not be found.");
                    }

                    // verifies the value for patientID is formatted correctly
                    String patientid; // Patient ID of patient attending appointment
                    while(true) {
                        System.out.println("Enter the patientID of the patient attending the appointment: ");
                        patientid = scanner.nextLine();
                        if (Backend.patientExists(Integer.parseInt(patientid))) {
                            break;
                        }
                        System.out.println("Patient could not be found.");
                    }

                    // retrieves specific attributes depending on type of appointment
                    switch(apptOption){
                        case 1: // Insert booked appointment
                            // verifies Appointment booked time is formatted as SQL DATETIME
                            String bookDate; // Time/date appointment is booked for
                            Pattern bookDatePattern = Pattern.compile("\\d\\d\\d\\d-\\d\\d-\\d\\d \\d\\d:\\d\\d:\\d\\d"); // regex pattern for datetime
                            Matcher bookedMatcher; // regex pattern matcher for datetime
                            while(true) {
                                System.out.print("Enter the check-in date/time (YYYY-MM-DD HH:MI:SS): ");
                                bookDate = scanner.nextLine();
                                bookedMatcher = bookDatePattern.matcher(bookDate);
                                if (!bookedMatcher.find() || bookDate.length() != 19) {
                                    System.out.println("\nAppointment time must be in form 'YYYY-MM-DD HH:MI:SS'");
                                } else {
                                    break;
                                }
                            }

                            // verifies emergency status of Appointment is correctly formatted
                            String isEmergency; // Whether appointment is an emergency
                            while(true) {
                                System.out.println("Is this appointment an emergency? ('Y'/'N')");
                                isEmergency = scanner.nextLine();
                                if (isEmergency.equals("Y") || isEmergency.equals("N"))
                                    break;
                                System.out.println("Emergency status must be input as 'Y' or 'N'");
                            }

                            // insert walk-in appointment record
                            Backend.addWalkin(bookDate, inPerson, service, employeeID, patientid, isEmergency);
                            break;

                        case 2: // Insert walk-in appointment
                            // verifies Appointment check-in time is formatted as SQL DATETIME
                            String checkIn; // check-in time/date of appointment
                            Pattern checkInDate = Pattern.compile("\\d\\d\\d\\d-\\d\\d-\\d\\d \\d\\d:\\d\\d:\\d\\d"); // regex pattern for datetime
                            Matcher checkInMatcher; // regex pattern matcher for datetime
                            while(true) {
                                System.out.print("Enter the check-in date/time (YYYY-MM-DD HH:MI:SS): ");
                                checkIn = scanner.nextLine();
                                checkInMatcher = checkInDate.matcher(checkIn);
                                if (!checkInMatcher.find() || checkIn.length() != 19) {
                                    System.out.println("\nCheck-in must be in form 'YYYY-MM-DD HH:MI:SS'");
                                } else {
                                    break;
                                }
                            }

                            // insert walk-in appointment record
                            Backend.addScheduled(checkIn, inPerson, service, employeeID, patientid);
                            break;

                        default:
                            System.out.println("Invalid appointment option.");
                    }
                    break;

                case 4: // Return to options menu
                    break;
                default:
                    System.out.println("Insert a(n)...\n  1. Patient\n  2. Employee\n  3. Appointment\n 4. Return to options menu");
                    System.out.println("Choose an option from the menu: ");
                    option = Integer.parseInt(scanner.nextLine());
            }
        }
    }

    /**
     * delete: Allows user to delete a record to the Patient, Employee, or Appointment table. The
     *         user then enters the id of the record they wish to delete. If the id does not match
     *         a record in the table, the user is notified and asked to reenter the id. At any
     *         point, the user can return to the previous menu.
     *
     * @param scanner The user input scanner
     * @return void
     * Pre-conditions: scanner is an open Scanner object
     * Post-conditions: N/A
     */
    private static void delete(Scanner scanner) {
        System.out.println("Delete a(n)...\n  1. Patient\n  2. Employee\n  3. Appointment\n 4. Return to options menu");
        int option = Integer.parseInt(scanner.nextLine()); // user's selection from text menu
        int id; // id of table entry (either PatientID, EmployeeID, AppointmentNo)
        boolean successfulDelete; // whether record was deleted successfully
        while (option != 4) {
            switch (option) {
                case 1:
                    // verifies existence of patient in DB
                    while(true) {
                        System.out.println("Enter a patient id: ");
                        id = Integer.parseInt(scanner.nextLine());
                        successfulDelete = Backend.deletePatient(id);
                        if (successfulDelete)
                            break;
                        System.out.println("Patient id could not be found. Try a different id.");
                    }
                    break;

                case 2:
                    // verifies existence of employee in DB
                    while(true) {
                        System.out.println("Enter an employee id: ");
                        id = Integer.parseInt(scanner.nextLine());
                        successfulDelete = Backend.deleteEmployee(id);
                        if (successfulDelete)
                            break;
                        System.out.println("Employee id could not be found. Try a different id.");
                    }
                    break;

                case 3:
                    // verifies existence of appointment in DB
                    while(true) {
                        System.out.println("Enter an appointment id: ");
                        id = Integer.parseInt(scanner.nextLine());
                        successfulDelete = Backend.deleteAppointment(id);
                        if (successfulDelete)
                            break;
                        System.out.println("Appointment number could not be found. Try a different number.");
                    }
                    break;

                case 4:
                    break;
                default:
                    System.out.println("Delete a(n)...\n  1. Patient\n  2. Employee\n  3. Appointment\n 4. Return to options menu");
                    System.out.println("Choose an option from the menu: ");
                    option = Integer.parseInt(scanner.nextLine());
            }
        }
    }

    /**
     * update: Allows user to update a record to the Patient, Employee, or Appointment table. The
     *         user then enters the id of the record they wish to update. If the id does not match
     *         a record in the table, the user is notified and asked to reenter the id. Then the
     *         user is prompted for the attribute they wish to change in that record. If the user-
     *         input attribute name does not match one in the table, they are notified and asked
     *         again for the attribute name. At any point, the user can return to the previous
     *         menu.
     *
     * @param scanner The user input scanner
     * @return void
     * Pre-conditions: scanner is an open Scanner object
     * Post-conditions: N/A
     */
    private static void update(Scanner scanner) {
        System.out.println("Update a(n)...\n  1. Patient\n  2. Employee\n  3. Appointment\n 4. Add shift\n 5. Return to options menu");
        int option = Integer.parseInt(scanner.nextLine()); // user's selection from text menu
        int id; // id of table entry (either PatientID, EmployeeID, AppointmentNo)
        boolean successfulUpdate;  // whether record was successfully updated
        String attr; // attribute user wishes to update
        String value; // new value of record
        while (option != 4) {
            switch (option) {
                case 1:
                    while(true) {
                        System.out.println("Enter a patient id: ");
                        id = Integer.parseInt(scanner.nextLine());
                        System.out.println("Enter an attribute to change: ");
                        attr = scanner.nextLine();
                        System.out.println("Enter a new value for the attribute: ");
                        value = scanner.nextLine();
                        successfulUpdate = Backend.updatePatient(id, attr, value);
                        if (successfulUpdate)
                            break;
                        System.out.println("Patient id could not be found. Try a different id.");
                    }
                    break;
                case 2:
                    while(true) {
                        System.out.println("Enter an employee id: ");
                        id = Integer.parseInt(scanner.nextLine());
                        System.out.println("Enter an attribute to change: ");
                        attr = scanner.nextLine();
                        System.out.println("Enter a new value for the attribute: ");
                        value = scanner.nextLine();
                        successfulUpdate = Backend.updateEmployee(id, attr, value);
                        if (successfulUpdate)
                            break;
                        System.out.println("Employee id could not be found. Try a different id.");
                    }
                    break;

                case 3:
                    while(true) {
                        System.out.println("Enter an appointment number: ");
                        id = Integer.parseInt(scanner.nextLine());
                        System.out.println("Enter an attribute to change: ");
                        attr = scanner.nextLine();
                        System.out.println("Enter a new value for the attribute: ");
                        value = scanner.nextLine();
                        successfulUpdate = Backend.updateAppointment(id, attr, value);
                        if (successfulUpdate)
                            break;
                        System.out.println("Appointment number could not be found. Try a different number.");
                    }
                    break;

                case 4:
                    while(true) {
                        System.out.println("Enter an employee id: ");
                        id = Integer.parseInt(scanner.nextLine());
                        if (Backend.employeeExists(id)) {
                            break;
                        }
                        System.out.println("Employee not found. Try a different id.");
                     }
                    String startTime;
                    Pattern startDateTime = Pattern.compile("\\d\\d\\d\\d-\\d\\d-\\d\\d \\d\\d:\\d\\d:\\d\\d");
                    Matcher startTimeMatcher;
                    while(true) {
                        System.out.print("Enter the shift start date/time (YYYY-MM-DD HH:MI:SS): ");
                        startTime = scanner.nextLine();
                        startTimeMatcher = startDateTime.matcher(startTime);
                        if (!startTimeMatcher.find() || startTime.length() != 19) {
                            System.out.println("\nDate/time must be in form 'YYYY-MM-DD HH:MI:SS'");
                        } else {
                            break;
                        }
                    }

                    String endTime;
                    Pattern endDateTime = Pattern.compile("\\d\\d\\d\\d-\\d\\d-\\d\\d \\d\\d:\\d\\d:\\d\\d");
                    Matcher endTimeMatcher;
                    while(true) {
                        System.out.print("Enter the shift start date/time (YYYY-MM-DD HH:MI:SS): ");
                        endTime = scanner.nextLine();
                        endTimeMatcher = endDateTime.matcher(endTime);
                        if (!endTimeMatcher.find() || endTime.length() != 19) {
                            System.out.println("\nDate/time must be in form 'YYYY-MM-DD HH:MI:SS'");
                        } else {
                            break;
                        }
                    }

                    // verifies the value for service is formatted correctly
                    String service;
                    while(true) {
                        System.out.println("Enter the Campus Health Service for the appointment: ");
                        service = scanner.nextLine();
                        if (service.equals("CAPS") || service.equals("Immunization") ||
                                service.equals("Laboratory and Testing") || service.equals("General Medicine"))
                            break;
                        System.out.println("Service must be 'CAPS', 'Immunization', 'Laboratory and Testing', or 'General Medicine'.");
                    }

                    // add shift to table
                    Backend.addShift(id, startTime, endTime, service);
                    break;

                case 5:
                    break;
                default:
                    System.out.println("Update a(n)...\n  1. Patient\n  2. Employee\n  3. Appointment\n 4. Return to options menu");
                    System.out.println("Choose an option from the menu: ");
                    option = Integer.parseInt(scanner.nextLine());
            }
        }
    }

    /**
     * query: The user is prompted to choose a query from the generated text menu. Some of these
     *        queries will prompt the user for information to specify the nature of the query. At
     *        any point, the user can return to the previous menu.
     *
     * @param scanner The user input scanner
     * @return void
     * Pre-conditions: scanner is an open Scanner object
     * Post-conditions: N/A
     */
    private static void query(Scanner scanner) {
        System.out.println("\nPlease choose a query:\n  1. Print a list of patients who have their 2nd, 3rd or 4th dos "+
                "es of the COVID-19 vaccine scheduled by a certain date.\n  2. Given a certain date, output wh" +
                "ich patients had a non-walk-in appointment. Sort in order by appointment time and group by ty" +
                "pe of service.\n  3. Print the schedule of staff given a certain date . A schedule contains t" +
                "he list of staff members working that day and a staff member's working hours.\n  4. Print the" +
                " vaccine statistics of the two categories of patients (student, employees). \n  5. Given an illnes, " +
                "print the list of patients who have received a vaccination for the illness ordered by name\n  " +
                "6. Return to options menu");
        int option = Integer.parseInt(scanner.nextLine()); // user's selection from text menu
        while (option != 6) {
            String date; // the date to be input by the user
            ResultSet answer; // JDBC response to query
            switch (option) {
                case 1:
                    while(true) {
                        System.out.println("Please enter a date (YYYY-MM-DD):");
                        date = scanner.nextLine();
                        if (checkDate(date)) {
                            answer = Backend.getPatientsScheduledForCOVIDImmunization(date);
                            System.out.println("Patients who have received their 2nd-4th COVID vaccine by this date\n" +
                            "----------------------------------------------------------");
                            try{
                                while(answer.next()){
                                    System.out.println(String.format("%s %s", answer.getString("FName"), answer.getString("LName")));
                                }
                            } catch(Exception exception){
                                System.err.println("Error fetching results for 1st query");
                            }
                            break;
                        }
                        System.out.println("Date must be in form 'YYYY-MM-DD'");
                    }
                    break;

                case 2:
                    while(true) {
                        System.out.println("Please enter a date (YYYY-MM-DD):");
                        date = scanner.nextLine();
                        if (checkDate(date)) {
                            answer = Backend.getPatientsWithScheduledAppts(date);
                            System.out.println("Patients with scheduled appointments on this day\n" +
                            "------------------------------------------");
                            try{
                                while(answer.next()){
                                    System.out.println(String.format("%s %s", answer.getString("FName"), answer.getString("LName")));
                                }
                            } catch(Exception exception){
                                System.err.println("Error fetching results for 2nd query");
                            }
                            break;
                        }
                        System.out.println("Date must be in form 'YYYY-MM-DD'");
                    }
                    break;

                case 3:
                    while(true) {
                        System.out.println("Please enter a date (YYYY-MM-DD):");
                        date = scanner.nextLine();
                        if (checkDate(date)) {
                            answer = Backend.getStaffSchedule(date);
                            System.out.println("Schedule of staff on this day\n-------------------------");
                            try{
                                while(answer.next()){
                                    System.out.println(String.format(
                                        "%s %s: %s -> %s",
                                        answer.getString("FName"), answer.getString("LName"),
                                        answer.getDate("StartTime").toString(), answer.getDate("EndTime").toString())
                                    );
                                }
                            } catch(Exception exception){
                                System.err.println("Error fetching results for 3rd query");
                            }
                            break;
                        }
                        System.out.println("Date must be in form 'YYYY-MM-DD'");
                    }
                    break;

                case 4:
                    ResultSet[] answers = Backend.getCOVIDImmunizationStats();
                    try {
                        System.out.println("Student statistics\n-----------------");
                        while(answers[0].next()){
                            System.out.println(String.format(
                                "%d people have received COVID-19 vaccine dose %d",
                                answers[0].getInt("Patients"), answers[0].getString("DoseNo"))
                            );
                        }
                        System.out.println("Employee statistics\n------------------");
                        while(answers[1].next()){
                            System.out.println(String.format(
                                "%d people have received COVID-19 vaccine dose %d",
                                answers[1].getInt("Patients"), answers[1].getString("DoseNo"))
                            );
                        }
                    } catch (Exception exception) {
                        System.err.println("Error fetching results for 4th query");
                    }
                    break;

                case 5:
                    System.out.println("Enter a type of immunization/illness to search for:");
                    String immunType = scanner.nextLine();
                    answer = Backend.getVaccinatedForIllness(immunType);
                    System.out.println("People vaccinated for this illness\n---------------------");
                    try {
                        while(answer.next()){
                            System.out.println(String.format("%s %s", answer.getString("FName"), answer.getString("LName")));
                        }
                    } catch (Exception exception) {
                        System.err.println("Error fetching results for 5th query");
                    }
                    break;

                case 6:
                    break;
                default:
                    System.out.println("\nPlease choose a query:\n  1. Print a list of patients who have their 2nd, 3rd or 4th doses of the COVID-19 vaccine scheduled by a certain date.\n" +
                            "  2. Given a certain date, output which patients had a non-walk-in appointment. Sort in order by appointment time and group by type of service.\n" +
                            "  3. Print the schedule of staff given a certain date . A schedule contains the list of staff members working that day and a staff member's working hours.\n" +
                            "  4. Print the vaccine statistics of the two categories of patients (student, employees). \n" +
                            "  5. A query of your choice\n  6. Return to options menu");
                    System.out.println("Choose an option from the menu: ");
                    option = Integer.parseInt(scanner.nextLine());
            }
        }
    }

    private static boolean checkDate(String date) {
        // verifies patient date is properly formatted
        Pattern datePattern = Pattern.compile("\\d\\d\\d\\d-\\d\\d-\\d\\d");
        Matcher dateMatcher;
        dateMatcher = datePattern.matcher(date);
        return dateMatcher.find() && date.length() == 10;
    }

    private static void checkImmun(Scanner scanner) {
        // verifies the value for patientID is formatted correctly
        int patientID;
        while(true) {
            System.out.println("Enter the patientID of the patient attending the appointment: ");
            try {
                patientID = Integer.parseInt(scanner.nextLine());
            } catch (Exception e) {
                System.out.println("patientID must be an integer value.");
                continue;
            }
            if (Backend.patientExists(patientID)) {
                break;
            }
            System.out.println("Patient could not be found.");
        }

        System.out.println("Enter a type of immunization/illness to search for: ");
        String immunType = scanner.nextLine();

        // verifies Appointment booked time is formatted as SQL DATETIME
        String bookDate;
        Pattern bookDatePattern = Pattern.compile("\\d\\d\\d\\d-\\d\\d-\\d\\d \\d\\d:\\d\\d:\\d\\d");
        Matcher bookedMatcher;
        while(true) {
            System.out.print("Enter the check-in date/time (YYYY-MM-DD HH:MI:SS): ");
            bookDate = scanner.nextLine();
            bookedMatcher = bookDatePattern.matcher(bookDate);
            if (!bookedMatcher.find() || bookDate.length() != 19) {
                System.out.println("\nAppointment time must be in form 'YYYY-MM-DD HH:MI:SS'");
            } else {
                break;
            }
        }

        // verifies dose is formatted as int
        int dose;
        while(true) {
            System.out.print("Enter the dose number: ");
            try {
                dose = Integer.parseInt(scanner.nextLine());
            } catch (Exception e) {
                System.out.println("Dose number must be an integer value.");
                continue;
            }
            break;
        }

        Backend.scheduleImmunization(patientID, immunType, bookDate, dose);
    }


}
