/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Homework;

import Classwork.BlackTea;
import Classwork.GingerTea;
import Classwork.MilkTea;


public class NotificationFactory 
{
    public Notification get(String type)
    {
    if(type =="SmsNotification")
       {
           return new SMSNotification();
       }
       else if(type=="emailNotification")
       {
           return new EmailNotification();
       }
       else if(type=="PushNotification")
       {
           return new PushNotification();
       }
       else
       {
           System.out.println("sorry ... not available!");
       }
       return null;
       
    }
}
    

