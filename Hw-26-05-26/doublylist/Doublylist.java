
package doublylist;


public class Doublylist 
{
    
    Node head;
    
    void insert(int data)
    {
        Node newNode = new Node(data);
        
        if(head==null)
        {
            head = newNode;
        }
        else
        {
            Node temp=head;
            
            while(temp.next!=null)
            {
                temp = temp.next;
                
            }
            temp.next = newNode;
            newNode.prev = temp;
        }
    }
    
    void display()
    {
        Node temp = head;
        while(temp!=null)
        {
        System.out.print(temp.data+" <-> ");
        temp = temp.next;
        }
        System.out.println("null");
    }
    
    void insertMid(int data,int position)
    {
        Node newNode = new Node(data);
        Node temp = head;
        int counter=1;
        
      while(counter<position)
      {
          temp = temp.next;
          if(temp==null)
          {
              System.out.println("POSITION IS OUT OF RANGE");
              break;
          }
          counter++;
          
      }
      
      if(temp!=null)
      {
      
      newNode.prev = temp.prev;
      newNode.next = temp;
      temp.prev.next = newNode;
      temp.prev = newNode;
      }
      
    }
    
    
    
}
