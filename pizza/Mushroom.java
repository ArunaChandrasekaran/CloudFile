/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pizza;

/**
 *
 * @author aruna
 */
class Mushroom extends PizzaDecorator {

    public Mushroom(Pizza pizza) {
        super(pizza);
    }

    @Override
    public String getDescription() {
        return pizza.getDescription() + ", Mushroom";
    }

    @Override
    public double getCost() {
        return pizza.getCost() + 25.0;
    }
}
