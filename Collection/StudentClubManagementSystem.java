package CollectionHw;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class StudentClubManagementSystem {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        Set<String> members = new HashSet<>();

        int choice;

        while(true){
            System.out.println("Student Club Management System");
            System.out.println("1. Add Member");
            System.out.println("2. Remove Member");
            System.out.println("3. Search Member");
            System.out.println("4. Display All Members");
            System.out.println("5. Count Members");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");

            choice = sc.nextInt();
           

            switch (choice) {

                case 1:
                    System.out.print("Enter member name to add: ");
                    String addMember = sc.nextLine();

                    if (members.add(addMember)) {
                        System.out.println("Member added successfully.");
                    } else {
                        System.out.println("Member already exists.");
                    }
                    break;

                case 2:
                    System.out.print("Enter member name to remove: ");
                    String removeMember = sc.nextLine();

                    if (members.remove(removeMember)) {
                        System.out.println("Member removed successfully.");
                    } else {
                        System.out.println("Member not found.");
                    }
                    break;

                case 3:
                    System.out.print("Enter member name to search: ");
                    String searchMember = sc.nextLine();

                    if (members.contains(searchMember)) {
                        System.out.println("Member found in the club.");
                    } else {
                        System.out.println("Member not found.");
                    }
                    break;

                case 4:
                    if (members.isEmpty()) {
                        System.out.println("No members in the club.");
                    } else {
                        System.out.println("Club Members:");
                        for (String member : members) {
                            System.out.println(member);
                        }
                    }
                    break;

                case 5:
                    System.out.println("Total Members: " + members.size());
                    break;

                case 6:
                    System.out.println("Exiting Application...");
                    System.exit(0);
                    break;

                default:
                    System.out.println("Invalid Choice! Please try again.");
            }

        } 

       
    }
}
