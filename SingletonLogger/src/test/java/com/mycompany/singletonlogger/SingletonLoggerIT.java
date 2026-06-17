/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.mycompany.singletonlogger;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author aruna
 */
public class SingletonLoggerIT {
    
    
    @Test
    public void testSingletonInstanceNotNull() {
        SingletonLogger logger = SingletonLogger.getInstance();
        assertNotNull(logger);
    }
    
    @Test
    public void testMultipleCallsReturnSameInstance() {
        SingletonLogger logger1 = SingletonLogger.getInstance();
        SingletonLogger logger2 = SingletonLogger.getInstance();
        SingletonLogger logger3 = SingletonLogger.getInstance();

        assertSame(logger1, logger2);
        assertSame(logger2, logger3);
    }

    
}
