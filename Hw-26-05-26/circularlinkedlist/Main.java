
package circularlinkedlist;


public class Main
{
    public static void main(String[] args)
    {
        CircularList list = new CircularList();

        list.insertHead(20);
        list.insertHead(10);

        list.insertTail(40);

        list.insertMiddle(30, 3);

        list.display();
    }
    
}
