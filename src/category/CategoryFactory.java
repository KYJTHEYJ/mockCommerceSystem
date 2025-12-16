package category;

import product.Product;

import java.util.List;

public final class CategoryFactory {
    private CategoryFactory() {}

    // 카테고리의 생성은 카테고리 이름, 상품의 리스트로 받는다
    public static Category initCategory(String category, List<Product> productList) {
        return new Category(category, productList);
    }
}
