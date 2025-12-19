package category;

import main.system.CommerceSystem;
import product.Product;

import java.util.List;
import java.util.function.Consumer;

public class Category {
    private final CategoryType categoryType;
    private final String categoryName;
    private final String categoryDescription;
    private final List<Product> productList;
    private final Consumer<CommerceSystem> menu;

    private Category(CategoryType categoryType, String categoryName, String categoryDescription, List<Product> productList, Consumer<CommerceSystem> menu) {
        this.categoryType = categoryType;
        this.categoryName = categoryName;
        this.categoryDescription = categoryDescription;
        this.productList = productList;
        this.menu = menu;
    }

    public CategoryType getCategoryType() {
        return categoryType;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getCategoryDescription() {
        return categoryDescription;
    }

    public List<Product> getProductList() {
        return productList;
    }

    // 카테고리에 따라 받는 생성자를 다르게 하기 위해 private 후 정적 메서드 생성
    public static Category createProductCategory(String categoryName, String categoryDescription, List<Product> productList) {
        return new Category(CategoryType.PRODUCT, categoryName, categoryDescription, productList, null);
    }

    public static Category createOrderCategory(String categoryName, String categoryDescription, Consumer<CommerceSystem> menu) {
        return new Category(CategoryType.ORDER, categoryName, categoryDescription, null, menu);
    }

    public static Category createAdminCategory(String categoryName, String categoryDescription, Consumer<CommerceSystem> menu) {
        return new Category(CategoryType.ADMIN, categoryName, categoryDescription, null, menu);
    }

    // 싱품 나열이 아닌 카테고리는 카테고리-내부 메뉴 형태로 수행이 필요, Consumer 인터페이스 사용하여 내부 기능 호출
    public void nonProductCategoryMenuExecute(CommerceSystem commerceSystem) {
        menu.accept(commerceSystem);
    }

    // 카테고리에 상품 추가하기, 다만 Product의 이름은 유니크 함을 지켜야함
    public boolean addProductInCategory(Product newProduct) {
        if (getProductList().stream().filter(product -> product.getProductName().equals(newProduct.getProductName())).toList().isEmpty()) {
            getProductList().add(newProduct);
            return true;
        } else {
            return false;
        }
    }
}
