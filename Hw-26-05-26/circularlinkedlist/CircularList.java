
package circularlinkedlist;


public class CircularList 
{
    
    Node head;

    // Insert at Head
    void insertHead(int data)
    {
        Node newNode = new Node(data);

        if(head == null)
        {
            head = newNode;
            head.next = head;
            head.prev = head;
        }
        else
        {
            Node tail = head.prev;

            newNode.next = head;
            newNode.prev = tail;

            tail.next = newNode;
            head.prev = newNode;

            head = newNode;
        }
    }

    // Insert at Tail
    void insertTail(int data)
    {
        Node newNode = new Node(data);

        if(head == null)
        {
            head = newNode;
            head.next = head;
            head.prev = head;
        }
        else
        {
            Node tail = head.prev;

            tail.next = newNode;
            newNode.prev = tail;

            newNode.next = head;
            head.prev = newNode;
        }
    }

    // Insert at Middle Position
    void insertMiddle(int data, int position)
    {
        Node newNode = new Node(data);

        if(position == 1)
        {
            insertHead(data);
            return;
        }

        Node temp = head;
        int count = 1;

        while(count < position - 1)
        {
            temp = temp.next;
            count++;
        }

        newNode.next = temp.next;
        newNode.prev = temp;

        temp.next.prev = newNode;
        temp.next = newNode;
    }

    // Display List
    void display()
    {
        if(head == null)
        {
            System.out.println("List is empty");
            return;
        }

        Node temp = head;

        do
        {
            System.out.print(temp.data + " <-> ");
            temp = temp.next;
        }
        while(temp != head);

        System.out.println("(HEAD)");
    }
    
}
