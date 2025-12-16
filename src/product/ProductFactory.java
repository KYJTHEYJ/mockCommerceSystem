package product;

import java.util.ArrayList;
import java.util.List;

public final class ProductFactory {
    public static List<Product> createProductListFromProductInfo(Object[][] productData) {
        List<Product> products = new ArrayList<>();

        for (Object[] data : productData) {
            products.add(new Product(
                    (String) data[0],
                    (int) data[1],
                    (String) data[2],
                    (int) data[3]
            ));
        }

        return products;
    }
}
