/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pizza;

/**
 *
 * @author aruna
 */
public class Main {
     public static void main(String[] args) {

        Pizza pizza1 = new PlainPizza();
        display(pizza1);

        Pizza pizza2 = new Cheese(new PlainPizza());
        display(pizza2);

        Pizza pizza3 = new Mushroom(new Cheese(new PlainPizza()));
        display(pizza3);

        Pizza pizza4 = new Olive(new Mushroom(new Cheese(new PlainPizza())));
        display(pizza4);
    }

    public static void display(Pizza pizza) {
        System.out.println("Description : " + pizza.getDescription());
        System.out.println("Cost        : " + pizza.getCost());
        System.out.println();
    }
    
}
