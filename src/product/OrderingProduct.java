package product;

import main.system.util.Util;

public class OrderingProduct {
    /// 원본 Product 를 유지해야 재고 조정이 가능함
    private final Product product;
    private int quantity;

    public OrderingProduct(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public String printOrderingCart() {
        return String.format("%s | %s | %s | 주문 수량 : %d개", product.getProductName(), Util.formattingPrice(product.getProductPrice()), product.getProductDescription(), quantity);
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
