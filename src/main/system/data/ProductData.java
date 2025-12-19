package main.system.data;

import category.Category;
import product.Product;

import java.util.ArrayList;
import java.util.List;

import static category.Category.createProductCategory;

// 상품 데이터 담당 클래스
public class ProductData {
    // 상품 모음 - 전자, 의류, 음식
    private final List<Product> electricProductList = new ArrayList<>();
    private final List<Product> clothProductList = new ArrayList<>();
    private final List<Product> foodProductList = new ArrayList<>();

    public ProductData() {
        initProductsInCategories();
    }

    public List<Product> getElectricProducts() { return electricProductList; }
    public List<Product> getClothProducts() { return clothProductList; }
    public List<Product> getFoodProducts() { return foodProductList; }

    //region 카테고리 안 상품 초기화
    private void initProductsInCategories() {
        electricProductList.add(new Product("Galaxy S26", 1170000, "최신 Android 스마트폰", 2));
        electricProductList.add(new Product("iPhone 18", 1300000, "최신 IOS 스마트폰", 3));
        electricProductList.add(new Product("MacBook Pro", 1800000, "최신 MacBook Pro", 1));
        electricProductList.add(new Product("MacBook Air", 1250000, "최신 MacBook Air", 1));
        electricProductList.add(new Product("Sony WF1000XM5", 280000, "소니 무선 블루투스 이어폰 XM5", 3));
    }

    public List<Product> getAllProductList() {
        List<Product> allProductList = new ArrayList<>();
        allProductList.addAll(electricProductList);
        allProductList.addAll(clothProductList);
        allProductList.addAll(foodProductList);
        return allProductList;
    }

    public List<Category> getProductCategoryList() {
        List<Category> categories = new ArrayList<>();
        categories.add(createProductCategory("전자제품", "", electricProductList));
        categories.add(createProductCategory("의류", "", clothProductList));
        categories.add(createProductCategory("음식", "", foodProductList));
        return categories;
    }
    //endregion


}
