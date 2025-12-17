package customer;

import customer.order.Order;
import customer.shoppingcart.ShoppingCart;

public class Customer {
    private final String name;
    private final String email;
    private final Grade grade;
    private final ShoppingCart shoppingCart;
    private final Order order;

    public Customer(String name, String email, Grade grade, ShoppingCart shoppingCart, Order order) {
        this.name = name;
        this.email = email;
        this.grade = grade;
        this.shoppingCart = shoppingCart;
        this.order = order;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public Grade getGrade() {
        return grade;
    }

    public ShoppingCart getShoppingCart() {
        return shoppingCart;
    }

    public Order getOrder() {
        return order;
    }
}
