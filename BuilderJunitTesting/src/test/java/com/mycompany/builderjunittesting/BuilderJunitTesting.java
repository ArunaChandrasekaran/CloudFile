/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.mycompany.builderjunittesting;

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
public class BuilderJunitTesting {
   
    @Test
    
    void testCompareExpectedAndActual() {

        User expected = new User.Builder("Aruna")
                .setAge(25)
                .setEmail("arunadevitvr")
                .setPhone("5479437394")
                .build();
                
        User actual = new User.Builder("Aruna")
                .setAge(25)
                .setEmail("arunadevitvr")
                .setPhone("5479437394")
                .build();

        assertEquals(expected.getName(),
                actual.getName());
        assertEquals(expected.getAge(),
                actual.getAge());
        assertEquals(expected.getEmail(),
                actual.getEmail());
        assertEquals(expected.getPhone(),
                actual.getPhone());
    }
    
}
