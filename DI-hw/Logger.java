package com.practice;
public class Logger 
{

   @Annotation
     UserService us;

    public void logMessages()
    {
        us.validation();
        System.out.println("log messages are shown");

    }
    
}
