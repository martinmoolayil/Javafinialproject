package cst8288.businessLayer.users;

import cst8288.businessLayer.managers.CharitableOrganizationManager;
import cst8288.businessLayer.managers.ConsumerManager;
import cst8288.businessLayer.managers.RetailerInventoryManager;
import cst8288.databaseLayer.DatabaseHelper;

import java.util.Scanner;

import java.util.Scanner;

public class UserRegistration {
    private DatabaseHelper dbHelper;
    private Scanner scanner;

    public UserRegistration(DatabaseHelper dbHelper) {
        this.dbHelper = dbHelper;
        this.scanner = new Scanner(System.in);
    }

    public void register() {
        System.out.println("User Registration");
        System.out.print("Enter Name: ");
        String name = scanner.nextLine();

        System.out.print("Enter Email: ");
        String email = scanner.nextLine();

        System.out.print("Enter Password: ");
        String password = scanner.nextLine();

        System.out.print("Enter User Type (Retailer/Consumer/CharitableOrganization): ");
        String userType = scanner.nextLine();

        boolean success = dbHelper.registerUser(name, email, password, userType);

        if (success) {
            System.out.println("Registration Successful");
        } else {
            System.out.println("Registration Failed");
        }
    }

    public void loginUser() {
        UserAuthentication authentication = new UserAuthentication(dbHelper);

        UserLoginResult loginResult = authentication.login();
        if (loginResult.getUserId() != -1) {
            switch (loginResult.getUserType()) {
                case "Consumer":
                    showConsumerMenu(loginResult.getUserId());
                    break;
                case "Retailer":
                    showRetailerMenu(loginResult.getUserId());
                    break;
                case "CharitableOrganization":
                    showCharitableOrganizationMenu(loginResult.getUserId());
                    break;
                // Additional cases for other user types
            }
        }
    }




    private void showConsumerMenu(int userId) {
        // Implement the logic for consumer menu
        System.out.println("Consumer Menu");
        // Add options for consumer-specific actions
    }

    private void showRetailerMenu(int userId) {
        // Implement the logic for retailer menu
        System.out.println("Retailer Menu");
        // Add options for retailer-specific actions
    }

    public static void main(String[] args) {
        DatabaseHelper dbHelper = new DatabaseHelper();
        UserRegistration registration = new UserRegistration(dbHelper);

        while (true) {
            System.out.println("Welcome to the Food Waste Reduction Platform");
            System.out.println("1. Register\n2. Login\n3. Exit");
            System.out.print("Choose an option: ");
            int option = registration.scanner.nextInt();
            registration.scanner.nextLine(); // Consume newline

            if (option == 1) {
                registration.register();
            } else if (option == 2) {
                registration.loginUser();
            } else {
                break;
            }
        }

        registration.scanner.close();
        dbHelper.close();
    }

    private void showCharitableOrganizationMenu(int userId) {
        CharitableOrganizationManager organizationManager = new CharitableOrganizationManager(dbHelper, userId);
        boolean exitMenu = false;

        while (!exitMenu) {
            System.out.println("Charitable Organization Menu:");
            System.out.println("1. Claim Food Items");
            System.out.println("2. View Claimed Items");
            System.out.println("3. Logout");

            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    // Code to claim food items
                    break;
                case 2:
                    // Code to view claimed items
                    break;
                case 3:
                    exitMenu = true;
                    break;
                default:
                    System.out.println("Invalid option.");
                    break;
            }
        }
    }
}
