
package Homework;

class UserSession {

    private static UserSession instance;
    
    private String userName;

    private UserSession() {
    }

    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public void setUser(String name) {
        userName = name;
    }
    
    public void showUser() {
        System.out.println("User Name: " + userName);
    }

    public static void main(String[] args) {

        UserSession session1 = UserSession.getInstance();
        session1.setUser("Aruna");

        UserSession session2 = UserSession.getInstance();

        session2.showUser();

        System.out.println("Same Object: " + (session1 == session2));
    }
}