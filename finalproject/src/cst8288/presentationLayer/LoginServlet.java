package cst8288.presentationLayer;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve form data
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        // Validate login credentials (dummy validation for demonstration)
        boolean isValidUser = validateUser(email, password);

        // Send response
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        if (isValidUser) {
            out.println("<h2>Login Successful</h2>");
            out.println("<p>Welcome, " + email + "!</p>");
        } else {
            out.println("<h2>Login Failed</h2>");
            out.println("<p>Invalid email or password. Please try again.</p>");
        }
        out.println("</body></html>");
    }

    // Dummy user validation method (replace with your actual validation logic)
    private boolean validateUser(String email, String password) {
        // Sample validation logic (always returns true for demonstration)
        return true;
    }
}