package main.system;

import category.Category;
import category.CategoryType;
import customer.Customer;
import main.system.process.CategoryProcess;
import main.system.process.MainMenuProcess;
import main.system.process.ProductProcess;
import main.system.process.ShoppingCartProcess;
import product.OrderingProduct;
import main.system.action.SelectActionResult;
import product.Product;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;
import java.util.function.Predicate;

import static category.Category.*;
import static main.system.action.Actions.*;

public class CommerceSystem {
    private final Customer customer;
    private final Scanner scanner = new Scanner(System.in);

    private final ProductProcess productProcess;
    private final ShoppingCartProcess shoppingCartProcess;
    private final CategoryProcess categoryProcess;
    private final MainMenuProcess mainMenuProcess;

    public CommerceSystem(Customer customer) {
        this.customer = customer;
        this.productProcess = new ProductProcess();
        this.shoppingCartProcess = new ShoppingCartProcess(scanner, customer);
        this.categoryProcess = new CategoryProcess(scanner, customer, shoppingCartProcess);
        this.mainMenuProcess = new MainMenuProcess(scanner);
    }

    public void start() {
        //region 메뉴 출력 및 입력
        SelectActionResult menu;

        // 메뉴 시작 -> 카테고리 보여주기 -> 상품 선택시 장바구니 보여주기
        while (true) {
            System.out.println(customer.getName() + "님, 환영합니다!");

            List<Category> categoryList = categoryProcess.buildCategoryList(productProcess);

            menu = mainMenuProcess.menuProcess(categoryList);

            if (menu.getAction().equals(EXIT)) {
                break;
            } else if (menu.getAction().equals(ERROR)) {
                System.out.println(menu.getMessage());
                continue;
            }

            Category selectedCategory = categoryList.get(menu.getSelectIndex());

            switch (selectedCategory.getCategoryType()) {
                case PRODUCT -> categoryProcess.categoryStart(selectedCategory);
                case ORDER, ADMIN -> selectedCategory.nonProductCategoryMenuExecute(CommerceSystem.this);
            }
        }

        System.out.println("커머스 프로그램을 종료합니다");
        //endregion
    }
}
