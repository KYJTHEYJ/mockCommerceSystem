package customer.order;

import product.Product;

import java.util.ArrayList;
import java.util.List;

public class Order {
    // 주문 일자 고려, 주문 상태 필요
    // 주문 상태에 따른 취소 가능 여부 반환 해야함
    List<Product> orderProductList = new ArrayList<>();

    public Order() {}

    public List<Product> getOrderProductList() {
        return orderProductList;
    }
}
