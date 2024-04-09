package cst8288.businessLayer.managers;

import cst8288.databaseLayer.DatabaseHelper;

import java.sql.*;
import java.util.Scanner;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RetailerInventoryManager {

    private DatabaseHelper dbHelper;
    private int retailerId; // Assuming each retailer has a unique ID

    public RetailerInventoryManager(DatabaseHelper dbHelper, int retailerId) {
        this.dbHelper = dbHelper;
        this.retailerId = retailerId;
    }

    public void addNewItem() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Adding New Food Item");

        System.out.print("Enter Item Name: ");
        String name = scanner.nextLine();

        System.out.print("Enter Quantity: ");
        int quantity = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        System.out.print("Enter Expiration Date (YYYY-MM-DD): ");
        String expiryDate = scanner.nextLine();

        if (dbHelper.addFoodItem(retailerId, name, quantity, expiryDate)) {
            System.out.println("Item added successfully.");
        } else {
            System.out.println("Failed to add item.");
        }
    }

    public void updateItem() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Updating Food Item");

        System.out.print("Enter Item ID: ");
        int itemId = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        System.out.print("Enter New Quantity: ");
        int newQuantity = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        System.out.print("Enter New Expiration Date (YYYY-MM-DD): ");
        String newExpiryDate = scanner.nextLine();

        if (dbHelper.updateFoodItem(itemId, newQuantity, newExpiryDate)) {
            System.out.println("Item updated successfully.");
        } else {
            System.out.println("Failed to update item.");
        }
    }

    // Additional methods for inventory management can be added here



    private ResultSet identifySurplusItems() {
        System.out.println("Identifying Surplus Food Items...");

        Date currentDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = sdf.format(currentDate);

        return dbHelper.getSurplusItems(retailerId, formattedDate);
    }


    public void listSurplusItems() {
        Scanner scanner = new Scanner(System.in);

        try {
            ResultSet surplusItems = identifySurplusItems();
            if (surplusItems != null) {
                while (surplusItems.next()) {
                    int itemId = surplusItems.getInt("ItemID");
                    String itemName = surplusItems.getString("Name");

                    System.out.println("Item ID: " + itemId + ", Name: " + itemName);
                    System.out.print("Is this item for Donation or Sale (D/S)? ");
                    String action = scanner.nextLine();

                    if ("D".equalsIgnoreCase(action)) {
                        dbHelper.updateItemStatus(itemId, "Donation");
                        System.out.println(itemName + " marked for donation.");
                    } else if ("S".equalsIgnoreCase(action)) {
                        System.out.print("Enter discounted price: ");
                        double discountedPrice = scanner.nextDouble();
                        scanner.nextLine(); // Consume newline
                        dbHelper.updateItemForSale(itemId, discountedPrice);
                        System.out.println(itemName + " marked for sale at discounted price: " + discountedPrice);
                    }
                }
            } else {
                System.out.println("No surplus items identified.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }
}
