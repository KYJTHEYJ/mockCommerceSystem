package customer.shoppingcart;

import customer.Customer;
import product.Product;
import product.OrderingProduct;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ShoppingCart {
    // 주문하려는 상품, 수량
    private List<OrderingProduct> shoppingCartList = new ArrayList<>();

    public ShoppingCart() {
    }

    public List<OrderingProduct> getOrderingProductList() {
        return shoppingCartList;
    }

    public int getShoppingCartOriginSumPrice() {
        int sumPrice = 0;

        for (OrderingProduct OrderingProduct : shoppingCartList) {
            sumPrice += OrderingProduct.getProduct().getProductPrice() * OrderingProduct.getQuantity();
        }

        return sumPrice;
    }

    public int getShoppingCartSumPrice(Customer customer) {
        int sumPrice = 0;

        for (OrderingProduct OrderingProduct : shoppingCartList) {
            sumPrice += (customer.getGrade()
                                 .getDiscountedPrice(
                                         OrderingProduct
                                                 .getProduct()
                                                 .getProductPrice()
                                 ) * OrderingProduct.getQuantity());
        }

        return sumPrice;
    }

    public int getShoppingCartDiscountPrice(Customer customer) {
        return getShoppingCartOriginSumPrice() - getShoppingCartSumPrice(customer);
    }

    public void addProductToCart(Product product) {
        // 수량 설정이 없으니 1개씩 주문, 이미 보유한 상품이 있으면 보유 수량을 증가 시키도록
        List<String> productNameListInCart = shoppingCartList.stream()
                .map(OrderingProduct -> OrderingProduct.getProduct().getProductName()).toList();

        if (!productNameListInCart.contains(product.getProductName())) {
            shoppingCartList.add(new OrderingProduct(product, 1));
        } else {
            for (OrderingProduct OrderingProduct : shoppingCartList) {
                if (OrderingProduct.getProduct().getProductName().equals(product.getProductName())) {
                    OrderingProduct.setQuantity(OrderingProduct.getQuantity() + 1);
                }
            }
        }
    }

    public boolean removeProductToCartUsingProductName(String productName) {
        // 이름으로 찾아 상품 제거하기
        return shoppingCartList.removeIf(OrderingProduct -> OrderingProduct.getProduct().getProductName().equals(productName));
    }
}
