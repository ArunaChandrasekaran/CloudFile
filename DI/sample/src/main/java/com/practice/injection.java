package com.practice;

import java.lang.reflect.Field;

public class injection 
{

    public static Mobile getObject() throws Exception
    {
        Mobile mobile = Mobile.class.getDeclaredConstructor().newInstance();

        Field field=Mobile.class.getDeclaredField("engine");

        if(field.isAnnotationPresent(create.class))
        {
            Battery battery=Battery.class.getDeclaredConstructor().newInstance();
        field.setAccessible(true);
        field.set(mobile, battery);
        }

        return mobile;

    }
    
}
