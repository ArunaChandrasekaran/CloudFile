package com.practice;

import java.lang.reflect.Field;



public class Inject 
{

    public static Logger getObject() throws Exception
    {

        Logger log = Logger.class.getDeclaredConstructor().newInstance();

        
        Field field = Logger.class.getDeclaredField("us");

        if(field.isAnnotationPresent(Annotation.class))
        {

            UserService us = UserService.class.getDeclaredConstructor().newInstance();
            
            field.set(log, us);


        }


		return log;


    }


    
}
