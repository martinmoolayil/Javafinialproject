package cst8288.presentationLayer;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/register")
public class RegistrationServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve form data
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String userType = request.getParameter("userType");

        // Save user registration data to the database (dummy operation for demonstration)
        boolean isRegistrationSuccessful = registerUser(name, email, password, userType);

        // Send response
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        if (isRegistrationSuccessful) {
            out.println("<h2>Registration Successful</h2>");
            out.println("<p>Thank you for registering, " + name + "!</p>");
        } else {
            out.println("<h2>Registration Failed</h2>");
            out.println("<p>Failed to register user. Please try again.</p>");
        }
        out.println("</body></html>");
    }

    // Dummy user registration method (replace with your actual database operation)
    private boolean registerUser(String name, String email, String password, String userType) {
        // Sample registration logic (always returns true for demonstration)
        return true;
    }
}