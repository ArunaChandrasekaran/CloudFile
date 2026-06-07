package collection.hw;

import java.util.HashSet;
import java.util.Set;

public class SetWorkOut {
    public static void main(String[] args) {

      
        Set<Integer> set = new HashSet<>();

        // Add elements
        set.add(10);
        set.add(20);
        set.add(30);
        set.add(20); // Duplicate
        set.add(40);
        set.add(10); // Duplicate

        // Display the Set
        System.out.println(set);

        // Check if an element exists
        if (set.contains(30)) {
            System.out.println(30 + " is present in the set.");
        } else {
            System.out.println(30+ " is not present in the set.");
        }

        // Remove an element
        set.remove(20);
        System.out.println("After removing 20: " + set);

        // Iterate through the Set
  
        for (Integer num : set) {
            System.out.println(num);
        }
    }
}