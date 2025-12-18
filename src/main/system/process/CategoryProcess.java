package main.system.process;

import category.Category;
import customer.Customer;
import main.system.action.SelectActionResult;
import product.Product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.function.Predicate;

import static category.Category.*;
import static main.system.action.Actions.EXIT;
import static main.system.action.Actions.SELECTED;

// 카테고리 프로세스 담당 클래스
// 카테고리 생성 담당
public class CategoryProcess {
    Scanner scanner;

    CategoryProcess(Scanner scanner) {
        this.scanner = scanner;
    }

    private void categoryPreDisplay(Category category) {
        String consoleStrBuilder = String.format("\n[ %s 카테고리 ]\n", category.getCategoryName()) +
                                   "1. 전체 상품 보기\n" +
                                   "2. 가격 설정 후 가격 이상 상품 보기\n" +
                                   "3. 가격 설정 후 가격 이하 상품 보기\n" +
                                   "0." + String.format(" %-15s | %-10s", "back", "뒤로가기");

        System.out.println(consoleStrBuilder);
    }

    private SelectActionResult categoryPreProcess(Category category) {
        int selectNum;

        try {
            categoryPreDisplay(category);

            System.out.print("메뉴 번호를 입력해주세요 : ");
            selectNum = scanner.nextInt();
            scanner.nextLine();

            return switch (selectNum) {
                case 1, 2, 3 -> SelectActionResult.selected(selectNum);
                case 0 -> SelectActionResult.exit();
                default -> throw new IndexOutOfBoundsException();
            };

        } catch (IndexOutOfBoundsException e) {
            return SelectActionResult.error("없는 번호를 입력하셨습니다\n");
        } catch (InputMismatchException e) {
            scanner.nextLine();
            return SelectActionResult.error("메뉴에 올바른 숫자를 입력해주세요\n");
        } catch (Exception e) {
            return SelectActionResult.error("오류 발생 : " + e.getLocalizedMessage() + "\n");
        }
    }

    private void categoryDisplay(Category category, Predicate<Product> pricePredicate) {
        StringBuilder consoleStrBuilder = new StringBuilder();
        List<Product> filteredProductList;
        consoleStrBuilder.append(String.format("\n[ %s 카테고리 ]\n", category.getCategoryName()));

        int index = 1;

        if (pricePredicate == null) {
            filteredProductList = category.getProductList();
        } else {
            filteredProductList = category.getProductList().stream().filter(pricePredicate).toList();
        }

        for (Product product : filteredProductList) {
            consoleStrBuilder.append(index).append(". ").append(product.printInfoButNotQty()).append("\n");
            index++;
        }

        consoleStrBuilder.append("0.").append(String.format(" %-15s | %-10s", "back", "뒤로가기"));

        System.out.println(consoleStrBuilder);
    }

    private SelectActionResult categoryProcess(Category category) {
        int selectNum;
        Predicate<Product> pricePredicate = null;
        List<Product> filteredProductList;
        BigDecimal inputPrice;
        SelectActionResult result;

        try {
            result = categoryPreProcess(category);

            if (result.getAction().equals(SELECTED)) {
                switch (result.getSelectNumber()) {
                    case 1 -> {
                        // 기본 출력
                        filteredProductList = category.getProductList();
                    }
                    case 2 -> {
                        System.out.print("이상 조회의 기준이 될 가격을 입력해주세요 : ");
                        inputPrice = scanner.nextBigDecimal();
                        scanner.nextLine();

                        // 입력 받은 가격 이상 조회
                        pricePredicate = product -> BigDecimal.valueOf(product.getProductPrice()).compareTo(inputPrice) >= 0;
                        filteredProductList = category.getProductList().stream().filter(pricePredicate).toList();
                    }
                    case 3 -> {
                        System.out.print("이하 조회의 기준이 될 가격을 입력해주세요 : ");
                        inputPrice = scanner.nextBigDecimal();
                        scanner.nextLine();

                        // 입력 받은 가격 이하 조회
                        pricePredicate = product -> BigDecimal.valueOf(product.getProductPrice()).compareTo(inputPrice) <= 0;
                        filteredProductList = category.getProductList().stream().filter(pricePredicate).toList();
                    }
                    default -> throw new IllegalStateException();
                }

                categoryDisplay(category, pricePredicate);

                System.out.print("메뉴 번호를 입력해주세요 : ");
                selectNum = scanner.nextInt();
                scanner.nextLine();

                if (selectNum == 0) {
                    return SelectActionResult.exit();
                }

                if (selectNum < 0 || selectNum > filteredProductList.size()) {
                    throw new IndexOutOfBoundsException();
                } else {
                    Product selectProduct = filteredProductList.get(selectNum - 1);
                    return SelectActionResult.selected(category.getProductList().indexOf(selectProduct));
                }
            } else {
                return result;
            }
        } catch (IndexOutOfBoundsException e) {
            return SelectActionResult.error("없는 번호를 입력하셨습니다\n");
        } catch (InputMismatchException e) {
            scanner.nextLine();
            return SelectActionResult.error("메뉴에 올바른 숫자를 입력해주세요\n");
        } catch (IllegalStateException e) {
            return SelectActionResult.error("이전 수행에 오류가 있습니다, 다시 수행해주세요\n");
        } catch (Exception e) {
            return SelectActionResult.error("오류 발생 : " + e.getLocalizedMessage() + "\n");
        }
    }

    public void categoryStart(Category category) {
        SelectActionResult result;

        do {
            result = categoryProcess(category);

            switch (result.getAction()) {
                case ERROR -> {
                    System.out.println(result.getMessage());
                }
                case SELECTED -> {
                    //addProductShoppingCartStart(category.getProductList().get(result.getSelectNumber()));
                }
            }
        } while (!result.getAction().equals(EXIT));
    }

    public List<Category> buildCategoryList(ProductProcess productProcess, Customer customer) {
        List<Category> categoryList = new ArrayList<>();

        //region 상품 관련 카테고리 추가
        categoryList.add(createProductCategory("전자제품", "", productProcess.getElectricProductList()));
        categoryList.add(createProductCategory("의류", "", productProcess.getClothProductList()));
        categoryList.add(createProductCategory("식품", "", productProcess.getFoodProductList()));
        //endregion

        //region 장바구니 관련 카테고리 추가
        if (!customer.getShoppingCart().getOrderingProductList().isEmpty()) {
            //categoryList.add(createOrderCategory("장바구니 확인", "장바구니를 확인 후 주문합니다", menu -> shoppingCartToOrderStart()));
            //categoryList.add(createOrderCategory("주문 취소", "주문 대기 중인 상품을 취소합니다", menu -> shoppingCartClearStart()));
        }
        //endregion

        //region 관리자 기능 추가
        //categoryList.add(createAdminCategory("관리자 기능", "", menu -> adminStart()));
        //endregion

        return categoryList;
    }

    public int printCategory(List<Category> categoryList, StringBuilder consoleStrBuilder, int index) {
        for (Category category : categoryList) {
            if (!category.getCategoryDescription().isEmpty()) {
                consoleStrBuilder.append(index).append(". ").append(String.format(" %-15s | %-10s", category.getCategoryName(), category.getCategoryDescription())).append("\n");
            } else {
                consoleStrBuilder.append(index).append(". ").append(String.format(" %-15s", category.getCategoryName())).append("\n");
            }

            index++;
        }
        return index;
    }
}
