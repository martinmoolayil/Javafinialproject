package cst8288.businessLayer.managers;

import cst8288.databaseLayer.DatabaseHelper;

import java.util.Scanner;

public class SubscriptionManager {

    private DatabaseHelper dbHelper;
    private int userId; // Each user has a unique ID

    public SubscriptionManager(DatabaseHelper dbHelper, int userId) {
        this.dbHelper = dbHelper;
        this.userId = userId;
    }

    public void subscribeForAlerts() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Subscribe for Surplus Food Alerts");
        System.out.print("Enter your location: ");
        String location = scanner.nextLine();

        System.out.print("Preferred communication method (Email/Phone): ");
        String communicationMethod = scanner.nextLine();

        System.out.print("Enter your food preferences (comma-separated): ");
        String foodPreferences = scanner.nextLine();

        boolean success = dbHelper.addSubscription(userId, location, communicationMethod, foodPreferences);

        if (success) {
            System.out.println("Subscription successful!");
        } else {
            System.out.println("Failed to subscribe.");
        }
    }

    // Additional methods for managing subscriptions can be added here
}
