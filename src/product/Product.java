package product;

public class Product {
    /// ProductName은 유니크
    private final String productName;
    private final int productPrice;
    private final String productDescription;
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

    public String getProductDescription() {
        return productDescription;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }

    public String printInfoButNotQty() {
        return String.format("%-15s | %-10d | %s", productName, productPrice, productDescription);
    }

    public String printInfoForAddShoppingCart() {
        return String.format("%s | %d | %s", productName, productPrice, productDescription);
    }

    public String printInfo() {
        return String.format("%s | %d | %s | 재고 : %d개", productName, productPrice, productDescription, productQuantity);
    }
}
