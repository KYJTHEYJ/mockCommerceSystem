package customer;

import customer.shoppingcart.ShoppingCart;

public class Customer {
    private final String name;
    private final String email;
    private final Grade grade;
    private final ShoppingCart shoppingCart;

    public Customer(String name, String email, Grade grade, ShoppingCart shoppingCart) {
        this.name = name;
        this.email = email;
        this.grade = grade;
        this.shoppingCart = shoppingCart;
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
}
