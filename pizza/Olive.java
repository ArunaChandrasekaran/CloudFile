/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pizza;

/**
 *
 * @author aruna
 */
class Olive extends PizzaDecorator {

    public Olive(Pizza pizza) {
        super(pizza);
    }

    @Override
    public String getDescription() {
        return pizza.getDescription() + ", Olive";
    }

    @Override
    public double getCost() {
        return pizza.getCost() + 20.0;
    }
}
