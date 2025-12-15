package model;

import java.util.List;

public class Category {
    private List<Product> productList;
    private String CategoryName;

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
