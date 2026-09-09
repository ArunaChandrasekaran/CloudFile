package com.practice;


import java.lang.reflect.Field;

public class Injection {
    


    public  static Cars getobject() throws Exception
    {
        

      Cars car=Cars.class.getDeclaredConstructor().newInstance();

      Field field=Cars.class.getDeclaredField("engine");

      if(field.isAnnotationPresent(create.class))
      {
        Engine engine=Engine.class.getDeclaredConstructor().newInstance();
        field.setAccessible(true);
        field.set(car, engine);
      }

return car;
    }
}
