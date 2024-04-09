package cst8288.businessLayer.managers;

public class SessionManager {

    private static String currentUserEmail;

    public static void createSession(String email) {
        currentUserEmail = email;
        System.out.println("Session created for user: " + email);
    }

    public static void destroySession() {
        currentUserEmail = null;
        System.out.println("Session ended");
    }

    public static String getCurrentUserEmail() {
        return currentUserEmail;
    }
}
