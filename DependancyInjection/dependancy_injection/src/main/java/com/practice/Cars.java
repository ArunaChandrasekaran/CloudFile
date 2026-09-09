package com.practice;

public class Cars
{
    @create
    Engine engine;

    public void drive()
    {
        engine.start();
        System.out.println("car is moving...");
    }
}