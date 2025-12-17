package customer.shoppingcart;

import product.Product;

public class ShoppingCartProduct {
    /// 원본 Product 를 유지해야 재고 조정이 가능함
    private final Product product;
    private int quantity;

    public ShoppingCartProduct(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public String printInfoInShoppingCart() {
        return String.format("%s | %d | %s | 주문 수량 : %d개", product.getProductName(), product.getProductPrice(), product.getProductDescription(), quantity);
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
