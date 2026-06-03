
package thread;


public class Thread2 extends Thread 
{
    
    @Override
    public void run()
    {
      for(int i = 1; i<=5;i++)
      {
          System.out.print(i+" ");
      }
    }
    
    
    
}
