/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package doublylist;

/**
 *
 * @author aruna
 */
public class Main 
{
    public static void main(String[] args) {
        
        Doublylist d = new Doublylist();
        d.insert(10);
        d.insert(20);
        d.insert(40);
        d.insert(50);
        d.display();
        d.insertMid(30,3);
        d.display();
     
    }
    
}
