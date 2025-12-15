package model;

public class Product {
    String productName;
    int productPrice;
    String productDescription;
    int productQuantity;

    public Product(String productName, int productPrice, String productDescription, int productQuantity) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.productDescription = productDescription;
        this.productQuantity = productQuantity;
    }

    public String printInfoButNotQty() {
        return String.format("%-15s | %-10d | %s", productName, productPrice, productDescription);
    }

    public String printInfo() {
        return String.format("%s | %d | %s | 재고 : %d개", productName, productPrice, productDescription, productQuantity);
    }
}
