package main;

import customer.Customer;
import customer.Grade;
import customer.order.Order;
import customer.shoppingcart.ShoppingCart;
import main.system.CommerceSystem;

public class Main {
    public static void main(String[] args) {
        //region main.system.CommerceSystem 객체 생성 및 초기화 수행
        CommerceSystem commerceSystem = new CommerceSystem(new Customer("TEST", "TEST@TEST.com", Grade.PLATINUM, new ShoppingCart(), new Order()));
        //endregion

        commerceSystem.start();
    }
}
