package category;

import product.Product;

import java.util.List;

public class Category {
    private final CategoryType categoryType;
    private final String categoryName;
    private final List<Product> productList;

    public Category(CategoryType categoryType, String categoryName, List<Product> productList) {
        this.categoryType = categoryType;
        this.categoryName = categoryName;
        this.productList = productList;
    }

    public CategoryType getCategoryType() {
        return categoryType;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public List<Product> getProductList() {
        return productList;
    }
}
