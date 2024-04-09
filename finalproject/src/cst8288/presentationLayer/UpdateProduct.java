package cst8288.presentationLayer;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cst8288.databaseLayer.DatabaseHelper;

@WebServlet("/updateProduct")
public class UpdateProduct extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve form data
        int itemId = Integer.parseInt(request.getParameter("itemId"));
        int newQuantity = Integer.parseInt(request.getParameter("newQuantity"));
        String expiryDate = request.getParameter("expiryDate");

        // Update product logic
        DatabaseHelper dbHelper = new DatabaseHelper();
        boolean success = dbHelper.updateFoodItem(itemId, newQuantity, expiryDate);
        dbHelper.close();
        
        // Send response
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        if (success) {
            out.println("<h2>Product Updated Successfully</h2>");
            out.println("<p>Item ID: " + itemId + "</p>");
            out.println("<p>New Quantity: " + newQuantity + "</p>");
            out.println("<p>New Price: $" + expiryDate + "</p>");
        } else {
            out.println("<h2>Error updating product</h2>");
        }
        out.println("</body></html>");
    }
}