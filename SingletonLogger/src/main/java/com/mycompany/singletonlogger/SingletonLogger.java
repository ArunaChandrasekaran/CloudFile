/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.singletonlogger;

/**
 *
 * @author aruna
 */
public class SingletonLogger {

    private static SingletonLogger instance;
    
    private SingletonLogger() {
        System.out.println("Logger Object Created");
    }
    public static SingletonLogger getInstance() {
        if (instance == null) { 
            synchronized (SingletonLogger.class) {
                if (instance == null) { 
                    instance = new SingletonLogger();
                }
            }
        }
        return instance;
    }
    
     public void log(String message) {
        System.out.println("LOG: " + message);
    }
}
