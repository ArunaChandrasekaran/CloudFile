
package gamaexam;



public class MountainArray 
{
   
    public static void main(String args[])
    {
        MountainArray obj = new MountainArray();
        int arr[] = {0,3,5,2,1};
        
        
        if(obj.check(arr))
        {
            System.out.println("given array is a mountain array");
        }else
        {
            System.out.println("not a mountain array");
        }
        
        
    }
    
    public boolean check(int arr[])
    {
        int i = 0;
        if(arr.length<3)
        {
            return false;
        }
        
        while(i<arr.length-1&&arr[i]<arr[i+1])
        {
            i++;
        }
        if(i==0||i==arr.length-1)
        {
            return false;
        }
        while(i<arr.length-1&&arr[i]>arr[i+1] )
        {
          
            i++;
        }
        return i == arr.length-1;
    }
    
    
}
