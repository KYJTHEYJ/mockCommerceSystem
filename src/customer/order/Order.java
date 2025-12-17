package customer.order;

import product.Product;

import java.util.ArrayList;
import java.util.List;

public class Order {
    List<Product> orderProductList = new ArrayList<>();

    public Order() {}

    public List<Product> getProductList() {
        return orderProductList;
    }
}
