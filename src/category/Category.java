package category;

import product.Product;

import java.util.List;

public class Category {
    private final String CategoryName;
    private final List<Product> productList;

    public Category(String CategoryName, List<Product> productList) {
        this.CategoryName = CategoryName;
        this.productList = productList;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public List<Product> getProductList() {
        return productList;
    }
}
