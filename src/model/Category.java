package model;

import java.util.List;

public class Category {
    List<Product> productList;
    String CategoryName;

    public String getCategoryName() {
        return CategoryName;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public Category(String CategoryName, List<Product> productList) {
        this.CategoryName = CategoryName;
        this.productList = productList;
    }
}
