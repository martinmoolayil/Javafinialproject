package cst8288.businessLayer.managers;

import cst8288.databaseLayer.DatabaseHelper;

public class DeliveryManager {
    private DatabaseHelper dbHelper;
    private static final double BASE_COST_PER_KM = 2.0;
    private static final double DISTANCE_COEFFICIENT = 1.33;
    private static final double LONG_DISTANCE_THRESHOLD = 10.0;

    public DeliveryManager(DatabaseHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public void arrangeDelivery(int orderId, int retailerId, int consumerId, double distance) {
        double cost = calculateCost(distance);
        String expectedDeliveryTime = calculateExpectedDeliveryTime(distance);

        dbHelper.createDeliveryRecord(orderId, retailerId, consumerId, distance, cost, expectedDeliveryTime);
    }

    private double calculateCost(double distance) {
        double cost = BASE_COST_PER_KM * distance;
        if (distance > 5) {
            cost *= DISTANCE_COEFFICIENT;
        }
        return cost;
    }

    private String calculateExpectedDeliveryTime(double distance) {
        return distance > LONG_DISTANCE_THRESHOLD ? "2 days" : "1 day";
    }
}