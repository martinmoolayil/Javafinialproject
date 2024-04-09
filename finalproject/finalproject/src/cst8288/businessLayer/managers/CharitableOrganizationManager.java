package cst8288.businessLayer.managers;

import cst8288.databaseLayer.DatabaseHelper;

import java.sql.*;
import java.util.Scanner;

public class CharitableOrganizationManager {

    private DatabaseHelper dbHelper;
    private int organizationId; // Assuming each organization has a unique ID

    public CharitableOrganizationManager(DatabaseHelper dbHelper, int organizationId) {
        this.dbHelper = dbHelper;
        this.organizationId = organizationId;
    }

    public void viewAndClaimDonations() {
        Scanner scanner = new Scanner(System.in);
        try {
            ResultSet donations = dbHelper.getAvailableDonations();

            if (!donations.next()) {
                System.out.println("No available donations at the moment.");
                return;
            }

            do {
                int itemId = donations.getInt("ItemID");
                String itemName = donations.getString("Name");
                System.out.println("Item ID: " + itemId + ", Name: " + itemName);

                System.out.print("Claim this item? (Y/N): ");
                String response = scanner.nextLine();

                if ("Y".equalsIgnoreCase(response)) {
                    dbHelper.claimDonation(itemId, organizationId);
                    System.out.println("You have claimed " + itemName);
                }
            } while (donations.next());

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }
}
