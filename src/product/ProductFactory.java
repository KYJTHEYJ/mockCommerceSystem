package product;

import java.util.ArrayList;
import java.util.List;

public final class ProductFactory {
    private ProductFactory() {}

    // 상품 생성 형태는 {"Galaxy S26", 1170000, "최신 Android 스마트폰", 100} 의 배열 구조로 받는다
    public static Product createProductFromProductInfo(Object[] productInfoArr) {
        String productName;
        int productPrice;
        String productDescription;
        int productQuantity;

        if(!(productInfoArr[0] instanceof String)) {
            throw new IllegalArgumentException("생성하려는 상품 정보의 이름은 문자열이여야 합니다");
        } else {
            productName = (String) productInfoArr[0];
        }

        if(!(productInfoArr[1] instanceof Integer)) {
            throw new IllegalArgumentException("생성하려는 상품 정보의 가격은 정수형이여야 합니다");
        } else {
            productPrice = (int) productInfoArr[1];
        }

        if(!(productInfoArr[2] instanceof String)) {
            throw new IllegalArgumentException("생성하려는 상품 정보의 상세 정보는 정수형이여야 합니다");
        } else {
            productDescription = (String) productInfoArr[2];
        }

        if(!(productInfoArr[3] instanceof Integer)) {
            throw new IllegalArgumentException("생성하려는 상품 정보의 재고는 정수형이여야 합니다");
        } else {
            productQuantity = (int) productInfoArr[3];
        }

        return (new Product(productName, productPrice, productDescription, productQuantity));
    }

    // 상품 리스트의 생성 형태는 {"Galaxy S26", 1170000, "최신 Android 스마트폰", 100} 가 중첩된 배열구조로 받는다
    public static List<Product> createProductListFromProductInfo(Object[][] productListArr) {
        List<Product> products = new ArrayList<>();

        for (Object[] data : productListArr) {
            products.add(createProductFromProductInfo(data));
        }

        return products;
    }
}
