package main.system.process;

import product.Product;

import java.util.ArrayList;
import java.util.List;

// 상품 리스트 프로세스 담당 클래스
// 상품 초기 데이터 설정 담당
public class ProductProcess {
    private final List<Product> electricProductList = new ArrayList<>();
    private final List<Product> clothProductList = new ArrayList<>();
    private final List<Product> foodProductList = new ArrayList<>();

    public ProductProcess() {
        initProductsInCategories();
    }

    public List<Product> getElectricProductList() {
        return electricProductList;
    }

    public List<Product> getClothProductList() {
        return clothProductList;
    }

    public List<Product> getFoodProductList() {
        return foodProductList;
    }

    // 상품 카테고리 초기 설정
    private void initProductsInCategories() {
        electricProductList.add(new Product("Galaxy S26", 1170000, "최신 Android 스마트폰", 2));
        electricProductList.add(new Product("iPhone 18", 1300000, "최신 IOS 스마트폰", 3));
        electricProductList.add(new Product("MacBook Pro", 1800000, "최신 MacBook Pro", 1));
        electricProductList.add(new Product("MacBook Air", 1250000, "최신 MacBook Air", 1));
        electricProductList.add(new Product("Sony WF1000XM5", 280000, "소니 무선 블루투스 이어폰 XM5", 3));
    }
}
