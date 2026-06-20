
package gamaexam;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class PatientManagementSystem 
{
    static Scanner sc = new Scanner(System.in);
    public static void main(String args[])
    {
        List<Patient> p = new ArrayList<>();
        
        while(true)
        {
            System.out.println("1.ADD PATIENT");
            System.out.println("2.VIEW ALL PATIENT");
            System.out.println("3.SEARCH PATIENT BY ID");
            System.out.println("4.UPDATE PATIENT DETAILS");
            System.out.println("5.DELETE PATIENT");
            System.out.println("6.COUNT TOTAL PATIENT");
            System.out.println("7.EXIT");
            
            System.out.println("CHOOSE ANY OPTION FROM THE ABOVE:");
            int option = sc.nextInt();
            
            if(option==1)
            {
                System.out.println("enter patient id:");
                int id = sc.nextInt();
                System.out.println("enter patient name:");
                String name = sc.next();
                System.out.println("enter age of the patient");
                int age = sc.nextInt();
                System.out.println("enter the gender");
                String gender = sc.next();
                System.out.println("enter the disease");
                String disease = sc.next();
                
                p.add(new Patient(id,name,age,gender,disease));
                
                
            }
            else if(option==2)
            {
                for(Patient obj:p)
                {
                    System.out.println(obj);
                }
            }
            else if(option ==3)
            {
                System.out.println("enter id to search:");
                int search = sc.nextInt();
                
                boolean found = false;
                
                for(Patient obj:p)
                {
                    if(search==obj.getId())
                    {
                        found = true;
                    System.out.println(obj);
                    }
                }
                
                if(!found)
                {
                    System.out.println("invalid id");
                }
                
                
            }
            else if(option ==4)
            {
                System.out.println("enter the patient id to update the disease:");
                int search = sc.nextInt();
                System.out.println("enter disease:");
                String disease = sc.next();
                
                boolean found = false;
                
                for(Patient obj:p)
                {
                    if(search==obj.getId())
                    {
                         found = true;
                         
                         obj.setDisease(disease);
                        
                        System.out.println("patient details updated successfully!");
                    }
                    
                    if(!found)
                    {
                        System.out.println("no patient record found");
                    }
                }
                
                
            }
            else if(option ==5)
            {
                System.out.println("enter id to delete:");
                int search = sc.nextInt();
                
                boolean found = false;
                
                 for(Patient obj:p)
                {
                    if(search==obj.getId())
                    {
                        found = true;
                        p.remove(obj);
                        System.out.println("patient details deleted successfully!");
                    }
                }
                 
                 if(!found)
                {
                    System.out.println("invalid id");
                }       
                
            }
            else if(option ==6)
            {
                 int count =0;
                for(Patient obj:p)
                {
                    count++;
                }
                System.out.println("total count of the patients: "+count);
            }
            else
            {
                System.out.println("program exited");
                System.exit(0);
            }
        }
          
    }
    
}
