package product;

import main.system.util.Util;

public class Product {
    /// ProductName은 유니크
    private String productName;
    private int productPrice;
    private String productDescription;
    private int productQuantity;

    public Product(String productName, int productPrice, String productDescription, int productQuantity) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.productDescription = productDescription;
        this.productQuantity = productQuantity;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }

    public String printInfoButNotQty() {
        return String.format("%-15s | %-15s | %s", productName, Util.formattingPrice(productPrice), productDescription);
    }

    public String printInfoForAddShoppingCart() {
        return String.format("%-15s | %-15s | %s", productName, Util.formattingPrice(productPrice), productDescription);
    }

    public String printInfo() {
        return String.format("%-15s | %-15s | %s | 재고 : %d개", productName, Util.formattingPrice(productPrice), productDescription, productQuantity);
    }
}
