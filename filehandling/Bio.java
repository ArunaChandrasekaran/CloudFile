/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package homework;

/**
 *
 * @author aruna
 */
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Bio {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        // Personal Details
        System.out.print("Enter Name: ");
        String name = sc.nextLine();

        System.out.print("Enter Age: ");
        int age = sc.nextInt();
        sc.nextLine();

        System.out.print("Enter Gender: ");
        String gender = sc.nextLine();

        System.out.print("Enter Date of Birth: ");
        String dob = sc.nextLine();

        System.out.print("Enter Blood Group: ");
        String bloodGroup = sc.nextLine();

        
        System.out.print("Enter Qualification: ");
        String qualification = sc.nextLine();

        System.out.print("Enter College Name: ");
        String college = sc.nextLine();

        System.out.print("Enter Percentage / CGPA: ");
        String cgpa = sc.nextLine();

        
        System.out.print("Enter Skills: ");
        String skills = sc.nextLine();

        System.out.print("Enter Experience (Years): ");
        String experience = sc.nextLine();

      
        System.out.print("Enter Phone Number: ");
        String phone = sc.nextLine();

        System.out.print("Enter Email ID: ");
        String email = sc.nextLine();

        System.out.print("Enter Address: ");
        String address = sc.nextLine();

        
        System.out.print("Enter Father Name: ");
        String father = sc.nextLine();

        System.out.print("Enter Mother Name: ");
        String mother = sc.nextLine();

        try {
            FileWriter fw = new FileWriter("C:\\Users\\aruna\\OneDrive\\Documents\\Folder for fileHandling\\biodata.txt");

            fw.write("BIO DATA \n");

            fw.write("\nPersonal Details\n");
            fw.write("Name : " + name + "\n");
            fw.write("Age : " + age + "\n");
            fw.write("Gender : " + gender + "\n");
            fw.write("DOB : " + dob + "\n");
            fw.write("Blood Group : " + bloodGroup + "\n");

            fw.write("\nEducation\n");
            fw.write("Qualification : " + qualification + "\n");
            fw.write("College : " + college + "\n");
            fw.write("CGPA : " + cgpa + "\n");

            fw.write("\nProfessional Details\n");
            fw.write("Skills : " + skills + "\n");
            fw.write("Experience : " + experience + " Years\n");

            fw.write("\nContact\n");
            fw.write("Phone : " + phone + "\n");
            fw.write("Email : " + email + "\n");
            fw.write("Address : " + address + "\n");

            fw.write("\nFamily\n");
            fw.write("Father Name : " + father + "\n");
            fw.write("Mother Name : " + mother + "\n");

            fw.close();

            System.out.println("\nBio Data successfully saved in biodata.txt");

        } catch (IOException e) {
            System.out.println("Error while writing file: " + e.getMessage());
        }

        sc.close();
    }
}
