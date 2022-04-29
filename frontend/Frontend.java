/**
 * Frontend.java
 * Authors: Jacob Egestad and Cade Marks
 * Course: CSC 460 - Database Design
 * Instructor: Dr. McCann
 * TAs: Haris Riaz and Aayush Pinto
 * Assignment: Program #4: Database Design and Implementation
 * Due: May 2, 2022
 * Description: This file represents the frontend of Program 4. It produces a basic text interface
 *              which allows the user to make requests that are then translated to SQL statements
 *              executed on the Database by Backend.java using JDBC. User requests are accepted by
 *              first displaying a menu of options for inserting, deleting, updating records, and
 *              making five different preset SQL queries to retrieve information from the DB. Some
 *              of these SQL queries take input from the user, such as a specific date.
 * Operational Requirements: Java 16
 * Missing Features and Bugs: N/A
 */

package frontend;

import java.util.Scanner;

public class Frontend {
    public static void main(String[] args) {
        //Backend.init();
        promptUser();
    }

    private static void promptUser() {
        System.out.println("1. Insert record\n2. Delete record\n3. Update record\n4. Query database\n-1:EXIT");
        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose an option from the menu: ");
        int option = Integer.parseInt(scanner.nextLine());
        while (option != -1) {
            switch (option) {
                case 1:
                    insert(scanner);
                case 2:
                    delete(scanner);
                case 3:
                    update(scanner);
                case 4:
                    query(scanner);
                default:
                    System.out.println("1. Insert record\n2. Delete record\n3. Update record\n4. Query database\n-1:EXIT");
                    System.out.println("Choose an option from the menu: ");
                    option = Integer.parseInt(scanner.nextLine());
            }
        }
        scanner.close();
    }

    private static void insert(Scanner scanner) {
        System.out.println("Insert a(n)...\n  1. Patient\n  2. Employee\n  3. Appointment\n 4. Return to options menu");
        int option = Integer.parseInt(scanner.nextLine());
        int id;
        while (option != 4) {
            switch (option) {
                case 1:
                    System.out.println("Enter a patient id: ");
                    id = Integer.parseInt(scanner.nextLine());
                case 2:
                    System.out.println("Enter an employee id: ");
                    id = Integer.parseInt(scanner.nextLine());
                case 3:
                    System.out.println("Enter an appointment id: ");
                    id = Integer.parseInt(scanner.nextLine());
                case 4:
                    break;
                default:
                    System.out.println("Insert a(n)...\n  1. Patient\n  2. Employee\n  3. Appointment\n 4. Return to options menu");
                    System.out.println("Choose an option from the menu: ");
                    option = Integer.parseInt(scanner.nextLine());
            }
        }
    }

    private static void delete(Scanner scanner) {
        System.out.println("Delete a(n)...\n  1. Patient\n  2. Employee\n  3. Appointment\n 4. Return to options menu");
        int option = Integer.parseInt(scanner.nextLine());
        int id;
        while (option != 4) {
            switch (option) {
                case 1:
                    System.out.println("Enter a patient id: ");
                    id = Integer.parseInt(scanner.nextLine());
                case 2:
                    System.out.println("Enter an employee id: ");
                    id = Integer.parseInt(scanner.nextLine());
                case 3:
                    System.out.println("Enter an appointment id: ");
                    id = Integer.parseInt(scanner.nextLine());
                case 4:
                    break;
                default:
                    System.out.println("Delete a(n)...\n  1. Patient\n  2. Employee\n  3. Appointment\n 4. Return to options menu");
                    System.out.println("Choose an option from the menu: ");
                    option = Integer.parseInt(scanner.nextLine());
            }
        }
    }

    private static void update(Scanner scanner) {
        System.out.println("Update a(n)...\n  1. Patient\n  2. Employee\n  3. Appointment\n 4. Return to options menu");
        int option = Integer.parseInt(scanner.nextLine());
        int id;
        while (option != 4) {
            switch (option) {
                case 1:
                    System.out.println("Enter a patient id: ");
                    id = Integer.parseInt(scanner.nextLine());
                case 2:
                    System.out.println("Enter an employee id: ");
                    id = Integer.parseInt(scanner.nextLine());
                case 3:
                    System.out.println("Enter an appointment id: ");
                    id = Integer.parseInt(scanner.nextLine());
                case 4:
                    break;
                default:
                    System.out.println("Update a(n)...\n  1. Patient\n  2. Employee\n  3. Appointment\n 4. Return to options menu");
                    System.out.println("Choose an option from the menu: ");
                    option = Integer.parseInt(scanner.nextLine());
            }
        }
    }

    private static void query(Scanner scanner) {
        System.out.println("\nPlease choose a query:\n  1. Print a list of patients who have their 2nd, 3rd or 4th doses of the COVID-19 vaccine scheduled by a certain date.\n" +
                "  2. Given a certain date, output which patients had a non-walk-in appointment. Sort in order by appointment time and group by type of service.\n" +
                "  3. Print the schedule of staff given a certain date . A schedule contains the list of staff members working that day and a staff member's working hours.\n" +
                "  4. Print the vaccine statistics of the two categories of patients (student, employees). \n" +
                "  5. A query of your choice\n  6. Return to options menu");
        int option = Integer.parseInt(scanner.nextLine());
        while (option != 6) {
            String date;
            switch (option) {
                case 1:
                    System.out.println("Please enter a date: ");
                    date = scanner.nextLine();
                case 2:
                    System.out.println("Please enter a date: ");
                    date = scanner.nextLine();
                case 3:
                    System.out.println("Please enter a date: ");
                    date = scanner.nextLine();
                case 4:

                case 5:

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


}
