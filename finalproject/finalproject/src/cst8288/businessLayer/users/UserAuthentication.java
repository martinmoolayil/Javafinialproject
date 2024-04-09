package cst8288.businessLayer.users;

import cst8288.businessLayer.managers.SessionManager;
import cst8288.databaseLayer.DatabaseHelper;

import java.util.Scanner;

public class UserAuthentication {
    private DatabaseHelper dbHelper;

    public UserAuthentication(DatabaseHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public UserLoginResult login() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("User Login");
        System.out.print("Enter Email: ");
        String email = scanner.nextLine();

        System.out.print("Enter Password: ");
        String password = scanner.nextLine();

        UserLoginResult loginResult = dbHelper.validateUserAndGetUserType(email, password);

        if (loginResult.getUserId() != -1) {
            System.out.println("Login Successful");
            return loginResult;
        } else {
            System.out.println("Login Failed");
            return new UserLoginResult(-1, null);
        }
    }
}