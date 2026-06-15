package Homework;
import java.util.Scanner;



public class UserIdValidation {

    public static void validateUserId(int userId)
            throws InvalidUserIdException {

        if (userId <= 0) {
            throw new InvalidUserIdException(
                    "User ID should be positive");
        }

        System.out.println("Your User ID: " + userId);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter User ID: ");
        String input = sc.nextLine();

        try {
           
            int userId = Integer.parseInt(input);
            validateUserId(userId);

        } catch (NumberFormatException e) {
            System.out.println("User Id should be a number");
        } catch (InvalidUserIdException e) {
            System.out.println(e.getMessage());
        }

        
    }
}