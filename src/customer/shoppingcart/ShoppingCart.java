package customer.shoppingcart;

import customer.Customer;
import product.Product;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCart {
    // 주문하려는 상품, 수량
    private final List<ShoppingCartProduct> shoppingCartList = new ArrayList<>();

    public ShoppingCart() {}

    public List<ShoppingCartProduct> getShoppingCartProductList() {
        return shoppingCartList;
    }

    public int getShoppingCartSumPrice(Customer customer) {
        int sumPrice = 0;

        for (ShoppingCartProduct shoppingCartProduct : shoppingCartList) {
            sumPrice += (customer.getGrade()
                                 .getDiscountedPrice(
                                         shoppingCartProduct
                                         .getProduct()
                                         .getProductPrice()
                                 ) * shoppingCartProduct.getQuantity());
        }

        return sumPrice;
    }

    public void addProductToCart(Product product) {
        // 수량 설정이 없으니 1개씩 주문, 이미 보유한 상품이 있으면 보유 수량을 증가 시키도록
        List<String> productNameListInCart = shoppingCartList.stream()
                .map(shoppingCartProduct -> shoppingCartProduct.getProduct().getProductName()).toList();

        if(!productNameListInCart.contains(product.getProductName())) {
            shoppingCartList.add(new ShoppingCartProduct(product, 1));
        } else {
           for(ShoppingCartProduct shoppingCartProduct : shoppingCartList) {
               if(shoppingCartProduct.getProduct().getProductName().equals(product.getProductName())) {
                   shoppingCartProduct.setQuantity(shoppingCartProduct.getQuantity() + 1);
               }
           }
        }
    }
}
