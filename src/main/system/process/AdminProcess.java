package main.system.process;

import category.Category;
import category.CategoryType;
import main.system.data.ProductData;
import main.system.action.Actions;
import main.system.action.SelectActionResult;
import product.Product;

import java.util.InputMismatchException;
import java.util.Objects;
import java.util.Scanner;

import static main.system.action.Actions.EXIT;
import static main.system.action.Actions.SELECTED;

// TODO 작성 중
// 관리자 기능 담당 클래스
public class AdminProcess {
    private final Scanner scanner;
    private final ProductData productData;

    public AdminProcess(Scanner scanner, ProductData productData) {
        this.scanner = scanner;
        this.productData = productData;
    }

    private void adminMenuDisplay() {
        String consoleStrBuilder = "\n[ 관리자 모드 ]\n" +
                                   "1. 상품 추가\n" +
                                   "2. 상품 수정\n" +
                                   "3. 상품 삭제\n" +
                                   "4. 전체 상품 현황\n" +
                                   "0." + String.format(" %-15s | %-10s", "exit", "프로그램 종료") + "\n";

        System.out.println(consoleStrBuilder);
    }

    private void addProductInCategoryPreDisplay() {
        StringBuilder consoleStrBuilder = new StringBuilder();
        consoleStrBuilder.append("\n어느 카테고리에 상품을 추가하시겠습니까?\n");

        int index = 1;
        for (Category category : productData.getProductCategoryList()) {
            if (category.getCategoryType().equals(CategoryType.PRODUCT)) {
                consoleStrBuilder.append(index).append(". ").append(category.getCategoryName()).append("\n");
                index++;
            }
        }

        consoleStrBuilder.append("0.").append(String.format(" %-15s | %-10s", "back", "뒤로가기"));

        System.out.println(consoleStrBuilder);
    }

    private SelectActionResult addProductInCategoryPreProcess() {
        int selectNum;

        try {
            addProductInCategoryPreDisplay();

            System.out.print("\n상품을 추가할 카테고리 : ");
            selectNum = scanner.nextInt();
            scanner.nextLine();

            if (selectNum == 0) {
                return SelectActionResult.exit();
            }

            if (selectNum < 0 || selectNum > productData.getProductCategoryList().size()) {
                throw new IndexOutOfBoundsException();
            } else {
                return SelectActionResult.selected(selectNum);
            }
        } catch (IndexOutOfBoundsException e) {
            return SelectActionResult.error("없는 번호를 입력하셨습니다\n");
        } catch (InputMismatchException e) {
            scanner.nextLine();
            return SelectActionResult.error("메뉴에 올바른 숫자를 입력해주세요\n");
        } catch (Exception e) {
            return SelectActionResult.error("오류 발생 : " + e.getLocalizedMessage() + "\n");
        }
    }

    private Product addProductInCategoryDisplay(Category category) {
        System.out.printf("\n[ %s 카테고리에 상품 추가 ]\n", category.getCategoryName());
        System.out.print("상품명을 입력해주세요 : ");
        String newProductName = scanner.nextLine();

        System.out.print("가격을 입력해주세요 : ");
        int newProductPrice = scanner.nextInt();
        scanner.nextLine();

        System.out.print("상품 설명을 입력해주세요 : ");
        String newProductDescription = scanner.nextLine();

        System.out.print("재고를 입력해주세요 : ");
        int newProductQuantity = scanner.nextInt();
        scanner.nextLine();

        System.out.printf("\n%s | %d | %s | 재고 : %d개\n", newProductName, newProductPrice, newProductDescription, newProductQuantity);
        System.out.println("위 정보로 상품을 입력하시겠습니까? : ");

        System.out.println("1. 확인");
        System.out.println("2. 취소");

        return new Product(newProductName, newProductPrice, newProductDescription, newProductQuantity);
    }

    private SelectActionResult addProductInCategoryProcess() {
        int selectNum;
        SelectActionResult result;
        Category selectCategory;

        try {
            result = addProductInCategoryPreProcess();

            if (result.getAction().equals(SELECTED)) {
                selectCategory = productData.getProductCategoryList().get(result.getSelectIndex());
            } else {
                return result;
            }

            Product newProduct = addProductInCategoryDisplay(selectCategory);

            System.out.print("메뉴 번호를 입력해주세요 : ");
            selectNum = scanner.nextInt();
            scanner.nextLine();

            switch (selectNum) {
                case 1 -> {
                    if (selectCategory.addProductInCategory(newProduct)) {
                        System.out.println("상품이 성공적으로 추가되었습니다!");
                    } else {
                        return SelectActionResult.error("중복된 상품명이 존재합니다!");
                    }
                }
                case 2 -> {
                    return SelectActionResult.exit();
                }
            }

            if (selectNum < 0 || selectNum > productData.getProductCategoryList().size()) {
                throw new IndexOutOfBoundsException();
            } else {
                return SelectActionResult.selected(selectNum);
            }
        } catch (IndexOutOfBoundsException e) {
            return SelectActionResult.error("없는 번호를 입력하셨습니다\n");
        } catch (InputMismatchException e) {
            scanner.nextLine();
            return SelectActionResult.error("메뉴에 올바른 숫자를 입력해주세요\n");
        } catch (Exception e) {
            return SelectActionResult.error("오류 발생 : " + e.getLocalizedMessage() + "\n");
        }
    }

    public void addProductInCategoryStart() {
        SelectActionResult result;

        do {
            result = addProductInCategoryProcess();

            if (Objects.requireNonNull(result.getAction()) == Actions.ERROR) {
                System.out.println(result.getMessage());
            }
        } while (!result.getAction().equals(EXIT) && !result.getAction().equals(SELECTED));
    }
}
