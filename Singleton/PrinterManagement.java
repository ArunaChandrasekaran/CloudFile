/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Homework;

class PrinterManager {

    private static PrinterManager instance;

    private PrinterManager() {
    }
    
    public static PrinterManager getInstance() {
        if (instance == null) {
            instance = new PrinterManager();
        }
        return instance;
    }

    public void print() {
        System.out.println("Printing document...");
    }

    public static void main(String[] args) {

        PrinterManager p1 = PrinterManager.getInstance();
        PrinterManager p2 = PrinterManager.getInstance();

        p1.print();
        p2.print();

        if (p1 == p2) {
            System.out.println("Both references point to the same object.");
        } else {
            System.out.println("Different objects created.");
        }
    }
}
