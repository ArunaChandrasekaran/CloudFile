package com.practice;

public class Mobile
 {

    @create
    private Battery battery;


    public void poweron()
    {
        battery.start();

        System.out.println("mobile can switch on");
    }
    
}
