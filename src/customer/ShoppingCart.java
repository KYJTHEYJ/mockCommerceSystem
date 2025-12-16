package customer;

import product.Product;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class ShoppingCart {
    Customer customer;
    LinkedHashMap<Product, Integer> productWithQty = new LinkedHashMap<>();
    List<LinkedHashMap<Product, Integer>> productList = new ArrayList<>();

    public ShoppingCart(Customer customer) {
        this.customer = customer;
    }

    public List<LinkedHashMap<Product, Integer>> getProductList() {
        return productList;
    }

    public void addProductToCart(Product product) {
        // 이미 상품이 있다면 수량 더하기, 없으면 add
    }

    public void removeProductToCart(Product product) {

    }
}
