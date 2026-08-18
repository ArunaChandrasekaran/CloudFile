/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package stringsum;
import java.util.Scanner;

public class Stringsum {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        String s = sc.nextLine();

        int star = 0;
        int hash = 0;

        for (int i = 0; i < s.length(); i++) {

            if (s.charAt(i) == '*') {
                star++;
            } 
            else if (s.charAt(i) == '#') {
                hash++;
            }
        }

        System.out.println(star - hash);
    }
}

