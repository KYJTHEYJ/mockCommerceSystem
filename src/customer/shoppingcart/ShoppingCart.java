package customer.shoppingcart;

import product.Product;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCart {
    // 주문하려는 상품, 수량
    List<Product> shoppingCartList = new ArrayList<>();

    public ShoppingCart() {}

    // 장바구니에 담은 상품을 반환
    public List<Product> getShoppingCartProductList() {
        return shoppingCartList;
    }

    public void addProductToCart(Product product) {
        // 수량 설정이 없으니 1개씩 주문, 이미 보유한 상품이 있으면 보유 수량을 증가 시키도록
        List<String> productListInCart = shoppingCartList.stream().map(name -> product.getProductName()).toList();

        if(!productListInCart.contains(product.getProductName())) {
            shoppingCartList.add(new Product(product.getProductName(), product.getProductPrice(), product.getProductDescription(), 1));
        } else {
           for(Product productInCart : shoppingCartList) {
               if(productInCart.getProductName().equals(product.getProductName())) {
                   productInCart.setProductQuantity(productInCart.getProductQuantity() + 1);
               }
           }
        }
    }

    public void removeProductToCart(Product product) {
        /*
        for(Product productInCart : shoppingCartList) {
            if(productInCart.getProductName().equals(product.getProductName())) {
                shoppingCartList.remove(productInCart);
            }
        }
        */

        shoppingCartList.removeIf(productInCart -> productInCart.getProductName().equals(product.getProductName()));
    }
}
