package cst8288.businessLayer.users;

public class UserLoginResult {
    private int userId;
    private String userType;

    public UserLoginResult(int userId, String userType) {
        this.userId = userId;
        this.userType = userType;
    }

    // Getters
    public int getUserId() {
        return userId;
    }

    public String getUserType() {
        return userType;
    }
}
