package cst8288.businessLayer.managers;

import cst8288.databaseLayer.DatabaseHelper;

import java.sql.*;
import java.util.Scanner;

public class ConsumerManager {

    private DatabaseHelper dbHelper;
    private int consumerId; // Assuming each consumer has a unique ID

    public ConsumerManager(DatabaseHelper dbHelper, int consumerId) {
        this.dbHelper = dbHelper;
        this.consumerId = consumerId;
    }

    public void viewAndPurchaseItems() {
        Scanner scanner = new Scanner(System.in);
        try {
            ResultSet itemsForSale = dbHelper.getItemsForSale();

            if (!itemsForSale.next()) {
                System.out.println("No items available for sale at the moment.");
                return;
            }

            do {
                int itemId = itemsForSale.getInt("ItemID");
                String itemName = itemsForSale.getString("Name");
                double price = itemsForSale.getDouble("Price");
                System.out.println("Item ID: " + itemId + ", Name: " + itemName + ", Price: " + price);

                System.out.print("Purchase this item? (Y/N): ");
                String response = scanner.nextLine();

                if ("Y".equalsIgnoreCase(response)) {
                    dbHelper.itemPurchase(itemId, consumerId);
                    System.out.println("You have purchased " + itemName);
                }
            } while (itemsForSale.next());

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }
}
