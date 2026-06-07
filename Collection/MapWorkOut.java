package collection.hw;

import java.util.HashMap;
import java.util.Map;

public class MapWorkOut {
    public static void main(String[] args) {

   
        Map<Integer, String> students = new HashMap<>();

       
        students.put(1, "Aruna");
        students.put(2, "Priya");
        students.put(3, "Kumar");

        // Display the Map
        System.out.println(students);

        // Retrieve value using key
        System.out.println("Student with ID 102: " + students.get(2));

        // Update a value
        students.put(102, "Divya");
        System.out.println("After updating ID 102: " + students);

        // Remove a key-value pair
        students.remove(103);
        System.out.println("After removing ID 103: " + students);

        // Display all keys
        System.out.println("Student IDs:");
        for (Integer id : students.keySet()) {
            System.out.println(id);
        }

        // Display all values
        System.out.println("Student Names:");
        for (String name : students.values()) {
            System.out.println(name);
        }

        // Display all key-value pairs
        System.out.println("All Student Details:");
        for (Map.Entry<Integer, String> entry : students.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
    }
}