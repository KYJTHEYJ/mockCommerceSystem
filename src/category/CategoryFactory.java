package category;

import static product.ProductFactory.createProductListFromProductInfo;

public final class CategoryFactory {
    public static Category initCategory(String category, Object[][] productData) {
        return new Category(category, createProductListFromProductInfo(productData));
    }
}
