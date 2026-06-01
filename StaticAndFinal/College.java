
package StaticAndFinal;

public class College
{
   static final String college_name = "ADMC";
   final double registration_fee;
   static int total_students = 0;

    public College(double fee) 
    {
       registration_fee = fee;
       total_students++;
    }
    
    static void displayCollegeDetails()
    {
        System.out.println("college Name: "+ college_name);
        System.out.println("total students: "+total_students);
    }
    void displayStudentFee()
    {
        System.out.println("Registration Fee: "+registration_fee);
    }
   
         
}
