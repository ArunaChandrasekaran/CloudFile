
package StaticAndFinal;

import java.util.Scanner;

public class MainCollege 
{
    Scanner sc = new Scanner(System.in);
    public static void main(String[] args)
    {
        College s1 = new College(25000);
        College s2 = new College(30000);
        
        College.displayCollegeDetails();
        s1.displayStudentFee();
        s2.displayStudentFee();
    }
}
