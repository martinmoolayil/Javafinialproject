package cst8288.presentationLayer;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cst8288.databaseLayer.DatabaseHelper;

@WebServlet("/claim")
public class ClaimFoodServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve form data
        String foodType = request.getParameter("foodType");
        int quantity = Integer.parseInt(request.getParameter("quantity"));
        String expiryDate = request.getParameter("expiryDate");

        // Process claim logic
        DatabaseHelper dbHelper = new DatabaseHelper();
        boolean success = dbHelper.claimFoodItem(foodType, quantity, expiryDate);

        // Send response
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        if (success) {
            out.println("<h2>Food Item Claimed Successfully</h2>");
            out.println("<p>Food Type: " + foodType + "</p>");
            out.println("<p>Quantity: " + quantity + "</p>");
            out.println("<p>Expiry Date: " + expiryDate + "</p>");
        } else {
            out.println("<h2>Failed to Claim Food Item</h2>");
        }
        out.println("</body></html>");

        dbHelper.close(); // Close the database connection
    }
}