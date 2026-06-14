/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Homework;

/**
 *
 * @author aruna
 */
public class Main {
    public static void main(String[] args) 
    {
        NotificationFactory obj =new NotificationFactory();
        Notification n = obj.get("SmsNotification");
        n.sendMessage();
        
        NotificationFactory obj1 =new NotificationFactory();
        Notification n1 = obj.get("emailNotification");
        n1.sendMessage();
        
        NotificationFactory obj2 =new NotificationFactory();
        Notification n2 = obj.get("PushNotification");
        n2.sendMessage();
    }
    
}
