package main.system;

import category.Category;
import customer.Customer;
import main.system.action.SelectActionResult;
import product.Product;

import java.util.*;

import static main.system.action.Actions.*;

public class CommerceSystem {
    private final Customer customer;
    private final Scanner scanner = new Scanner(System.in);

    public CommerceSystem(Customer customer) {
        this.customer = customer;
    }

    private void menuDisplay(List<Category> categoryList) {
        StringBuilder consoleStrBuilder = new StringBuilder();
        consoleStrBuilder.append("[ 실시간 커머스 플랫폼 ]\n");
        int index = 1;
        for (Category category : categoryList) {
            consoleStrBuilder.append(index).append(". ").append(category.getCategoryName()).append("\n");
            index++;
        }

        consoleStrBuilder.append("0.").append(String.format(" %-15s | %-10s", "exit", "프로그램 종료"));


        System.out.println(consoleStrBuilder);
    }

    private SelectActionResult menuStart(List<Category> categoryList) {
        int selectNum;

        try {
            menuDisplay(categoryList);

            System.out.print("메뉴 번호를 입력해주세요 : ");
            selectNum = scanner.nextInt();
            scanner.nextLine();

            if (selectNum == 0) {
                return SelectActionResult.exit();
            }

            if (selectNum < 0 || selectNum > categoryList.size()) {
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

    private void categoryDisplay(List<Category> categoryList, int selectIndex) {
        StringBuilder consoleStrBuilder = new StringBuilder();
        consoleStrBuilder.append(String.format("[ %s 카테고리 ]\n", categoryList.get(selectIndex).getCategoryName()));
        int index = 1;

        for (Product product : categoryList.get(selectIndex).getProductList()) {
            consoleStrBuilder.append(index).append(". ").append(product.printInfoButNotQty()).append("\n");
            index++;
        }

        consoleStrBuilder.append("0.").append(String.format(" %-15s | %-10s", "back", "뒤로가기"));

        System.out.println(consoleStrBuilder);
    }

    private SelectActionResult categoryStart(List<Category> categoryList, int selectIndex) {
        int selectNum;

        try {
            categoryDisplay(categoryList, selectIndex);

            System.out.print("상품 번호를 입력해주세요 : ");
            selectNum = scanner.nextInt();
            scanner.nextLine();

            if (selectNum == 0) {
                return SelectActionResult.exit();
            }

            if (selectNum < 0 || selectNum > categoryList.get(selectIndex).getProductList().size()) {
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

    private void addShoppingCartDisplay(Product product) {
        String consoleStrBuilder = "선택한 상품 : "
                                   + product.printInfo() + "\n\n"
                                   + product.printInfoForAddShoppingCart() + "\n"
                                   + "위 상품을 장바구니에 추가하시겠습니까?\n"
                                   + "1. 확인\n" + "2. 취소\n";

        System.out.println(consoleStrBuilder);
    }

    private SelectActionResult addShoppingCartStart(Product product) {
        int selectNum;

        try {
            addShoppingCartDisplay(product);

            System.out.print("메뉴 번호를 입력해주세요 : ");
            selectNum = scanner.nextInt();
            scanner.nextLine();

            if (selectNum == 2) {
                return SelectActionResult.exit();
            }

            if (selectNum != 1) {
                throw new IndexOutOfBoundsException();
            } else {
                if(product.getProductQuantity() < 0) {
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

    public void start() {
        List<Category> categoryList = new ArrayList<>();

        //region 전자 카테고리별 상품
        List<Product> electricProductList = new ArrayList<>();
        electricProductList.add(new Product("Galaxy S26", 117000, "최신 Android 스마트폰", 100));
        electricProductList.add(new Product("iPhone 18", 130000, "최신 IOS 스마트폰", 100));
        electricProductList.add(new Product("MacBook Pro", 180000, "최신 MacBook Pro", 100));
        electricProductList.add(new Product("MacBook Air", 125000, "최신 MacBook Air", 100));
        //endregion

        //region 의류 카테고리별 상품
        List<Product> clothProductList = new ArrayList<>();
        //endregion

        //region 식품 카테고리별 상품
        List<Product> foodProductList = new ArrayList<>();
        //endregion

        //region 카테고리 추가
        categoryList.add(new Category("전자제품", electricProductList));
        categoryList.add(new Category("의류", clothProductList));
        categoryList.add(new Category("식품", foodProductList));
        //endregion

        //region 장바구니 관련 카테고리 추가
        if(!customer.getShoppingCart().getShoppingCartProductList().isEmpty()) {
            categoryList.add(new Category("장바구니 확인", customer.getShoppingCart().getShoppingCartProductList()));
            categoryList.add(new Category("주문 취소", customer.getShoppingCart().getShoppingCartProductList()));
        }

        //region 메뉴 출력 및 입력
        SelectActionResult menu;
        SelectActionResult category;
        SelectActionResult shoppingCart;
        Product selectedProduct;

        // 메뉴 시작 -> 카테고리 보여주기 -> 상품 선택시 쇼핑카트 보여주기
        while (true) {
            System.out.println(customer.getName() + "님, 환영합니다!\n");

            menu = menuStart(categoryList);

            if (menu.getAction().equals(EXIT)) {
                break;
            } else if (menu.getAction().equals(ERROR)) {
                System.out.println(menu.getMessage());
                continue;
            }

            do {
                category = categoryStart(categoryList, menu.getSelectIndex());
                switch (category.getAction()) {
                    case ERROR -> {
                        System.out.println(category.getMessage());
                    }
                    case SELECTED -> {
                        selectedProduct = categoryList.get(menu.getSelectIndex()).getProductList().get(category.getSelectIndex());

                        do {
                            shoppingCart = addShoppingCartStart(selectedProduct);

                            if (shoppingCart.getAction().equals(ERROR)) {
                                System.out.println(shoppingCart.getMessage());
                            } else if (shoppingCart.getAction().equals(SOLDOUT)) {
                                System.out.println(shoppingCart.getMessage());
                            }
                        } while (shoppingCart.getAction().equals(ERROR));
                    }
                }
            } while (!category.getAction().equals(EXIT));
        }

        System.out.println("커머스 프로그램을 종료합니다");
        //endregion
    }
}
