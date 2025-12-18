package main.system;

import category.Category;
import category.CategoryType;
import customer.Customer;
import product.OrderingProduct;
import main.system.action.SelectActionResult;
import product.Product;

import java.text.DecimalFormat;
import java.util.*;

import static category.Category.createCommandCategory;
import static category.Category.createProductCategory;
import static main.system.action.Actions.*;

public class CommerceSystem {
    private final Customer customer;
    private final Scanner scanner = new Scanner(System.in);

    //region 상품 카테고리 설정
    // 생성할 카테고리
    List<Product> electricProductList = new ArrayList<>();
    List<Product> clothProductList = new ArrayList<>();
    List<Product> foodProductList = new ArrayList<>();

    // 상품 카테고리 초기 설정
    private void initProductsInCategories() {
        electricProductList.add(new Product("Galaxy S26", 1170000, "최신 Android 스마트폰", 100));
        electricProductList.add(new Product("iPhone 18", 1300000, "최신 IOS 스마트폰", 100));
        electricProductList.add(new Product("MacBook Pro", 1800000, "최신 MacBook Pro", 100));
        electricProductList.add(new Product("MacBook Air", 1250000, "최신 MacBook Air", 100));
    }
    //endregion

    public CommerceSystem(Customer customer) {
        this.customer = customer;
        initProductsInCategories();
    }

    //region 메뉴 생성 관련
    public int printCategory(List<Category> categoryList, StringBuilder consoleStrBuilder, int index) {
        for (Category category : categoryList) {
            if(!category.getCategoryDescription().isEmpty()) {
                consoleStrBuilder.append(index).append(". ").append(String.format(" %-15s | %-10s", category.getCategoryName(), category.getCategoryDescription())).append("\n");
            } else {
                consoleStrBuilder.append(index).append(". ").append(String.format(" %-15s", category.getCategoryName())).append("\n");
            }

            index++;
        }
        return index;
    }

    private void menuDisplay(List<Category> categoryList) {
        List<Category> productCategoryList = categoryList.stream().filter(category -> category.getCategoryType().equals(CategoryType.PRODUCT)).toList();
        List<Category> shoppingCartCategoryList = categoryList.stream().filter(category -> category.getCategoryType().equals(CategoryType.COMMAND)).toList();
        StringBuilder consoleStrBuilder = new StringBuilder();

        // 메인 메뉴 디스플레이
        consoleStrBuilder.append("\n[ 실시간 커머스 플랫폼 ]\n");
        int index = 1;
        index = printCategory(productCategoryList, consoleStrBuilder, index);

        consoleStrBuilder.append("0.").append(String.format(" %-15s | %-10s", "exit", "프로그램 종료")).append("\n");

        // 주문 관련 쪽 디스플레이
        if (!shoppingCartCategoryList.isEmpty()) {
            consoleStrBuilder.append("\n[ 주문 관리 ]\n");
        }

        if (!shoppingCartCategoryList.isEmpty()) {
            index = printCategory(shoppingCartCategoryList, consoleStrBuilder, index);
        }
        //endregion

        System.out.println(consoleStrBuilder);
    }

    private SelectActionResult menuProcess(List<Category> categoryList) {
        int selectNum;

        try {
            menuDisplay(categoryList);

            System.out.print("메뉴 번호를 입력해주세요 : ");
            selectNum = scanner.nextInt();
            scanner.nextLine();

            if (selectNum == 0) {
                return SelectActionResult.exit();
            }

            if (selectNum < 0 || selectNum > (categoryList.size())) {
                throw new IndexOutOfBoundsException();
            } else {
                return SelectActionResult.selected(selectNum);
            }
        } catch (IndexOutOfBoundsException e) {
            return SelectActionResult.error("없는 번호를 입력하셨습니다\n");
        } catch (InputMismatchException e) {
            return SelectActionResult.error("메뉴 번호에 맞는 숫자를 입력해주세요\n");
        } catch (Exception e) {
            return SelectActionResult.error("오류 발생 : " + e.getLocalizedMessage() + "\n");
        }
    }

    private void categoryDisplay(Category category) {
        StringBuilder consoleStrBuilder = new StringBuilder();
        consoleStrBuilder.append(String.format("\n[ %s 카테고리 ]\n", category.getCategoryName()));
        int index = 1;

        for (Product product : category.getProductList()) {
            consoleStrBuilder.append(index).append(". ").append(product.printInfoButNotQty()).append("\n");
            index++;
        }

        consoleStrBuilder.append("0.").append(String.format(" %-15s | %-10s", "back", "뒤로가기"));

        System.out.println(consoleStrBuilder);
    }

    private SelectActionResult categoryProcess(Category category) {
        int selectNum;

        try {
            categoryDisplay(category);

            System.out.print("메뉴 번호를 입력해주세요 : ");
            selectNum = scanner.nextInt();
            scanner.nextLine();

            if (selectNum == 0) {
                return SelectActionResult.exit();
            }

            if (selectNum < 0 || selectNum > category.getProductList().size()) {
                throw new IndexOutOfBoundsException();
            } else {
                return SelectActionResult.selected(selectNum);
            }
        } catch (IndexOutOfBoundsException e) {
            return SelectActionResult.error("없는 번호를 입력하셨습니다\n");
        } catch (InputMismatchException e) {
            return SelectActionResult.error("메뉴 번호에 맞는 숫자를 입력해주세요\n");
        } catch (Exception e) {
            return SelectActionResult.error("오류 발생 : " + e.getLocalizedMessage() + "\n");
        }
    }

    private void categoryStart(Category category) {
        SelectActionResult result;

        do {
            result = categoryProcess(category);

            switch (result.getAction()) {
                case ERROR -> {
                    System.out.println(result.getMessage());
                }
                case SELECTED -> {
                    addProductShoppingCartStart(category.getProductList().get(result.getSelectIndex()));
                }
            }
        } while (!result.getAction().equals(EXIT));
    }
    //endregion

    //region 쇼핑카트 관련
    //region 쇼핑카트 상품 추가 기능 관련
    private void addProductShoppingCartDisplay(Product product) {
        String consoleStrBuilder = "선택한 상품 : "
                                   + product.printInfo() + "\n\n"
                                   + product.printInfoForAddShoppingCart() + "\n"
                                   + "위 상품을 장바구니에 추가하시겠습니까?\n"
                                   + "1. 확인\n" + "2. 취소\n";

        System.out.println(consoleStrBuilder);
    }

    private SelectActionResult addProductShoppingCartProcess(Product product) {
        int selectNum;

        try {
            addProductShoppingCartDisplay(product);

            System.out.print("메뉴 번호를 입력해주세요 : ");
            selectNum = scanner.nextInt();
            scanner.nextLine();

            if (selectNum == 2) {
                return SelectActionResult.exit();
            }

            if (selectNum != 1) {
                throw new IndexOutOfBoundsException();
            } else {
                if (product.getProductQuantity() > 0) {
                    customer.getShoppingCart().addProductToCart(product);
                    System.out.print(product.getProductName() + "이(가) 장바구니에 추가되었습니다\n");
                    return SelectActionResult.selected(selectNum);
                } else {
                    return SelectActionResult.soldOut(product.getProductName() + " 상품의 재고가 없습니다!\n");
                }
            }
        } catch (IndexOutOfBoundsException e) {
            return SelectActionResult.error("없는 번호를 입력하셨습니다\n");
        } catch (InputMismatchException e) {
            return SelectActionResult.error("메뉴 번호에 맞는 숫자를 입력해주세요\n");
        } catch (Exception e) {
            return SelectActionResult.error("오류 발생 : " + e.getLocalizedMessage() + "\n");
        }
    }

    private void addProductShoppingCartStart(Product product) {
        SelectActionResult result;

        do {
            result = addProductShoppingCartProcess(product);

            if (result.getAction().equals(ERROR) || result.getAction().equals(SOLDOUT)) {
                System.out.println(result.getMessage());
            }
        } while (!result.getAction().equals(SELECTED) && !result.getAction().equals(EXIT));
    }
    //endregion

    //region 쇼핑카트 상품 주문 관련
    private void shoppingCartToOrderDisplay() {
        StringBuilder consoleStrBuilder = new StringBuilder();
        consoleStrBuilder.append("\n[ 장바구니 내역 주문하기 ]\n");
        consoleStrBuilder.append("[ 장바구니 내역 ]\n");

        for (OrderingProduct OrderingProduct : customer.getShoppingCart().getOrderingProductList()) {
            consoleStrBuilder.append(OrderingProduct.printOrderingCart()).append("\n");
        }

        consoleStrBuilder.append("[ 총 주문 금액 ]\n");
        consoleStrBuilder.append("할인 전 금액 : ").append(new DecimalFormat("###,###")
                .format(customer.getShoppingCart().getShoppingCartOriginSumPrice())).append("원\n");
        consoleStrBuilder.append(String.format("%s 고객님의 등급 %s 할인율 (%d%%) 할인된 금액: %s"
                , customer.getName()
                , customer.getGrade().name()
                , customer.getGrade().discountRate
                , new DecimalFormat("###,###").format(customer.getShoppingCart().getShoppingCartDiscountPrice(customer)))).append("원\n");
        consoleStrBuilder.append("< 최종 주문 금액 > ").append(new DecimalFormat("###,###")
                        .format(customer.getShoppingCart().getShoppingCartSumPrice(customer)))
                .append("원\n");
        consoleStrBuilder.append("1. 주문 확정\n")
                .append("2. 메인으로 돌아가기\n");

        System.out.println(consoleStrBuilder);
    }

    private SelectActionResult shoppingCartToOrderProcess() {
        int selectNum;

        try {
            shoppingCartToOrderDisplay();

            System.out.print("메뉴 번호를 입력해주세요 : ");
            selectNum = scanner.nextInt();
            scanner.nextLine();

            if (selectNum == 2) {
                return SelectActionResult.exit();
            }

            if (selectNum != 1) {
                throw new IndexOutOfBoundsException();
            } else {
                System.out.println("주문이 완료 되었습니다!\n < 결제 금액 > " + new DecimalFormat("###,###")
                        .format(customer.getShoppingCart().getShoppingCartSumPrice(customer)) + "원\n");

                for (OrderingProduct OrderingProduct : customer.getShoppingCart().getOrderingProductList()) {
                    Product product = OrderingProduct.getProduct();

                   /*
                   System.out.printf(String.format("%s 재고가 %d -> %d 개로 업데이트 됩니다\n"
                           , product.getProductName()
                           , product.getProductQuantity()
                           , product.getProductQuantity() - OrderingProduct.getQuantity()));
                   */
                    product.setProductQuantity(product.getProductQuantity() - OrderingProduct.getQuantity());
                }

                // 주문 했으니 장바구니 안 비우기
                customer.getShoppingCart().getOrderingProductList().clear();

                return SelectActionResult.selected(selectNum);
            }
        } catch (IndexOutOfBoundsException e) {
            return SelectActionResult.error("없는 번호를 입력하셨습니다\n");
        } catch (InputMismatchException e) {
            return SelectActionResult.error("메뉴 번호에 맞는 숫자를 입력해주세요\n");
        } catch (Exception e) {
            return SelectActionResult.error("오류 발생 : " + e.getLocalizedMessage() + "\n");
        }
    }

    private void shoppingCartToOrderStart() {
        SelectActionResult result;
        do {
            result = shoppingCartToOrderProcess();

            if (result.getAction().equals(ERROR)) {
                System.out.println(result.getMessage());
            }
        } while (!result.getAction().equals(SELECTED) && !result.getAction().equals(EXIT));
    }

    private void shoppingCartClear() {
        System.out.println("장바구니 안 주문들을 전부 취소하였습니다!\n");
        customer.getShoppingCart().getOrderingProductList().clear();
    }
    //endregion

    //region "카테고리 총합"
    private List<Category> buildCategoryList() {
        List<Category> categoryList = new ArrayList<>();

        //region 상품 관련 카테고리 추가
        categoryList.add(createProductCategory("전자제품", "", electricProductList));
        categoryList.add(createProductCategory("의류", "", clothProductList));
        categoryList.add(createProductCategory("식품", "", foodProductList));
        //endregion

        //region 장바구니 관련 카테고리 추가
        if (!customer.getShoppingCart().getOrderingProductList().isEmpty()) {
            categoryList.add(createCommandCategory("장바구니 확인", "장바구니를 확인 후 주문합니다", menu -> shoppingCartToOrderStart()));
            categoryList.add(createCommandCategory("주문 취소", "진행 중인 주문을 취소합니다 (장바구니에 담긴 것들을 비웁니다)", menu -> shoppingCartClear()));
        }
        //endregion

        return categoryList;
    }
    //endregion

    public void start() {
        //region 메뉴 출력 및 입력
        SelectActionResult menu;
        Product selectedProduct;

        // 메뉴 시작 -> 카테고리 보여주기 -> 상품 선택시 쇼핑카트 보여주기
        while (true) {
            System.out.println(customer.getName() + "님, 환영합니다!");

            List<Category> categoryList = buildCategoryList();

            menu = menuProcess(categoryList);

            if (menu.getAction().equals(EXIT)) {
                break;
            } else if (menu.getAction().equals(ERROR)) {
                System.out.println(menu.getMessage());
                continue;
            }

            Category selectedCategory = categoryList.get(menu.getSelectIndex());

            switch (selectedCategory.getCategoryType()) {
                case PRODUCT -> categoryStart(selectedCategory);
                case COMMAND -> selectedCategory.menuExecute(CommerceSystem.this);
            }
        }

        System.out.println("커머스 프로그램을 종료합니다");
        //endregion
    }
}
