package cst8288.presentationLayer;

import cst8288.databaseLayer.DatabaseHelper;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/deleteProduct")
public class DeleteProduct extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve form data
        String productName = request.getParameter("deleteProductName");

        // Initialize DatabaseHelper
        DatabaseHelper dbHelper = new DatabaseHelper();

        // Delete product logic
        boolean isDeleted = dbHelper.deleteProduct(productName);
        
        // Send response
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        if (isDeleted) {
            out.println("<h2>Product Deleted Successfully</h2>");
            out.println("<p>Name: " + productName + "</p>");
        } else {
            out.println("<h2>Failed to Delete Product</h2>");
            out.println("<p>An error occurred while deleting the product.</p>");
        }
        out.println("</body></html>");

        // Close DatabaseHelper
        dbHelper.close();
    }
}