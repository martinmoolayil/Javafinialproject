package cst8288.databaseLayer;

import cst8288.businessLayer.users.UserLoginResult;

import java.sql.*;

public class DatabaseHelper {

    private Connection connection;

    public DatabaseHelper() {
        // Initialize database connection
        // Replace with your database connection details
        String url = "jdbc:mysql://localhost:3306/foodwastedb";
        String user = "root";
        String password = "root";

        try {
            this.connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean registerUser(String name, String email, String password, String userType) {
        String sql = "INSERT INTO users (Name, Email, Password, UserType) VALUES (?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            statement.setString(2, email);
            statement.setString(3, password);
            statement.setString(4, userType);
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Close connection
    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int validateUser(String email, String password) {
        String sql = "SELECT UserID FROM Users WHERE Email = ? AND Password = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("UserID");
            } else {
                return -1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public boolean addFoodItem(int retailerId, String name, int quantity, String expiryDate) {
        String sql = "INSERT INTO FoodItem (RetailerID, Name, Quantity, ExpiryDate) VALUES (?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, retailerId);
            statement.setString(2, name);
            statement.setInt(3, quantity);
            statement.setString(4, expiryDate);
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateFoodItem(int itemId, int newQuantity, String newExpiryDate) {
        String sql = "UPDATE FoodItem SET Quantity = ?, ExpiryDate = ? WHERE ItemID = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, newQuantity);
            statement.setString(2, newExpiryDate);
            statement.setInt(3, itemId);
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ResultSet getSurplusItems(int retailerId, String currentDate) {
        String sql = "SELECT * FROM FoodItem WHERE RetailerID = ? AND ExpiryDate BETWEEN ? AND DATE_ADD(?, INTERVAL 7 DAY)";

        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, retailerId);
            statement.setString(2, currentDate);
            statement.setString(3, currentDate);
            return statement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void updateItemStatus(int itemId, String status) {
        String sql = "UPDATE FoodItem SET Status = ? WHERE ItemID = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, status);
            statement.setInt(2, itemId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateItemForSale(int itemId, double price) {
        String sql = "UPDATE FoodItem SET Price = ?, Status = 'Sale' WHERE ItemID = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setDouble(1, price);
            statement.setInt(2, itemId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet getAvailableDonations() {
        String sql = "SELECT * FROM FoodItem WHERE Status = 'Donation'";

        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            return statement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void claimDonation(int itemId, int organizationId) {
        String updateStatusSql = "UPDATE FoodItem SET Status = 'Claimed', ClaimedBy = ? WHERE ItemID = ?";
        String updateInventorySql = "UPDATE FoodItem SET Quantity = Quantity - 1 WHERE ItemID = ?";

        try {
            // Start a transaction
            connection.setAutoCommit(false);

            // Update the status of the food item
            try (PreparedStatement updateStatusStmt = connection.prepareStatement(updateStatusSql)) {
                updateStatusStmt.setInt(1, organizationId);
                updateStatusStmt.setInt(2, itemId);
                updateStatusStmt.executeUpdate();
            }

            // Update the inventory of the retailer
            try (PreparedStatement updateInventoryStmt = connection.prepareStatement(updateInventorySql)) {
                updateInventoryStmt.setInt(1, itemId);
                updateInventoryStmt.executeUpdate();
            }

            // Commit the transaction
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public ResultSet getItemsForSale() {
        String sql = "SELECT * FROM FoodItem WHERE Status = 'Sale'";

        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            return statement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ResultSet getDiscountedItemsForSale() {
        String sql = "SELECT * FROM FoodItem WHERE Status = 'Sale' AND DiscountedPrice IS NOT NULL";

        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            return statement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void itemPurchase(int itemId, int consumerId) {
        String sqlUpdateStatus = "UPDATE FoodItem SET Status = 'Sold', SoldTo = ? WHERE ItemID = ?";
        String sqlUpdateInventory = "UPDATE FoodItem SET Quantity = Quantity - 1 WHERE ItemID = ?";

        try {
            connection.setAutoCommit(false);

            try (PreparedStatement updateStatusStmt = connection.prepareStatement(sqlUpdateStatus)) {
                updateStatusStmt.setInt(1, consumerId);
                updateStatusStmt.setInt(2, itemId);
                updateStatusStmt.executeUpdate();
            }

            try (PreparedStatement updateInventoryStmt = connection.prepareStatement(sqlUpdateInventory)) {
                updateInventoryStmt.setInt(1, itemId);
                updateInventoryStmt.executeUpdate();
            }

            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean addSubscription(int userId, String location, String communicationMethod, String foodPreferences) {
        String sql = "INSERT INTO Subscription (UserID, Location, CommunicationMethod, FoodPreferences) VALUES (?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            statement.setString(2, location);
            statement.setString(3, communicationMethod);
            statement.setString(4, foodPreferences);
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void createDeliveryRecord(int orderId, int retailerId, int consumerId, double distance, double cost, String expectedDeliveryTime) {
        String sql = "INSERT INTO Delivery (OrderID, RetailerID, ConsumerID, Distance, Cost, ExpectedDeliveryTime) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, orderId);
            statement.setInt(2, retailerId);
            statement.setInt(3, consumerId);
            statement.setDouble(4, distance);
            statement.setDouble(5, cost);
            statement.setString(6, expectedDeliveryTime);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public UserLoginResult validateUserAndGetUserType(String email, String password) {
        String sql = "SELECT UserID, UserType FROM Users WHERE Email = ? AND Password = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new UserLoginResult(resultSet.getInt("UserID"), resultSet.getString("UserType"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new UserLoginResult(-1, null); // User not found or login failed
    }

    //public ResultSet getSurplusFoodItemsForDonation() {
        // Implement the query to get items that are marked as surplus and available for donation
   // }

    public void claimFoodItem(int itemId, int organizationId) {
        // Implement the update to mark an item as claimed by the specified organization
    }
}

