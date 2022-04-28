package frontend;

public class Frontend {
    public static void main() {
        Backend.init();
        promptUser();
    }

    private static void promptUser() {
        printMenu();
        Scanner myObj = new Scanner(System.in);
        System.out.println("Choose an option from the menu: ");
        int option = Integer.parseInt(myObj.nextLine());
        while (option != -1) {
            switch (option) {
                case 1:
                    insert();
                    break;
                case 2:
                    delete();
                    break;
                case 3:
                    update();
                    break;
                case 4:
                    query();
                    break;
                default:
                    System.out.println("Choose an option from the menu: ");
                    option = Integer.parseInt(myObj.nextLine());
            }
        }
    }

    // prints UI menu with querying/modifying options
    private static void printMenu() {
        System.out.println("1. Insert record\n2. Delete record\n3. Update record\n4. Query database");
    }

    private static void insert() {}

    private static void delete() {}

    private static void update() {}

    private static void query() {}


}
