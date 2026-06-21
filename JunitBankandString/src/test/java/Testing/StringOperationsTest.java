/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Testing;

import homework.StringOperations;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;


import org.junit.jupiter.api.Test;

public class StringOperationsTest {

    @Test
    void testReverseString() {
        String input = "Java\n";

        System.setIn(new ByteArrayInputStream(input.getBytes()));

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        StringOperations obj = new StringOperations();
        obj.reverseString();

        assertTrue(output.toString().contains("avaJ"));
    }

    @Test
    void testRemoveSpace() {
        String input = "Hello World\n";

        System.setIn(new ByteArrayInputStream(input.getBytes()));

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        StringOperations obj = new StringOperations();
        obj.removeSpace();

        assertTrue(output.toString().contains("HelloWorld"));
    }

    @Test
    void testFindDuplicates() {
        String input = "programming\n";

        System.setIn(new ByteArrayInputStream(input.getBytes()));

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        StringOperations obj = new StringOperations();
        obj.findDuplicates();

        String result = output.toString();

        assertTrue(result.contains("r"));
        assertTrue(result.contains("g"));
        assertTrue(result.contains("m"));
    }
}