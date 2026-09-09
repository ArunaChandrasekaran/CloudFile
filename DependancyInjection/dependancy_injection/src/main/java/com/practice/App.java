package com.practice;

public class App {
    public static void main(String[] args) throws Exception {
    
        Cars c = Injection.getobject();
        c.drive();
    }
}
