package cst8288.presentationLayer;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cst8288.databaseLayer.DatabaseHelper;

@WebServlet("/addProduct")
public class AddProduct extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve form data
        String productName = request.getParameter("productName");
        int quantity = Integer.parseInt(request.getParameter("quantity"));
        String expiryDate = request.getParameter("expiryDate");

        // Add product logic
        DatabaseHelper dbHelper = new DatabaseHelper();
        boolean success = dbHelper.addFoodItem(productName, quantity, expiryDate);

        // Send response
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        if (success) {
            out.println("<h2>Product Added Successfully</h2>");
            out.println("<p>Name: " + productName + "</p>");
            out.println("<p>Quantity: " + quantity + "</p>");
            out.println("<p>Expiry Date: $" + expiryDate + "</p>");
        } else {
            out.println("<h2>Failed to Add Product</h2>");
        }
        out.println("</body></html>");

        dbHelper.close(); // Close the database connection
    }
}
