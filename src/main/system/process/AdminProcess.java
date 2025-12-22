package main.system.process;

import category.Category;
import category.CategoryType;
import customer.Customer;
import main.system.data.ProductData;
import main.system.action.Actions;
import main.system.action.SelectActionResult;
import main.system.util.Util;
import product.Product;

import java.util.*;

import static main.system.action.Actions.*;

// 관리자 기능 담당 클래스
public class AdminProcess {
    private final Scanner scanner;
    private final ProductData productData;
    private final Customer customer;
    private final String adminPw = "admin123";
    private final int LOGIN_REMAIN_TIME = 3;

    public AdminProcess(Scanner scanner, ProductData productData, Customer customer) {
        this.scanner = scanner;
        this.productData = productData;
        this.customer = customer;
    }

    //region 상품 추가 관련
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

        consoleStrBuilder.append("0.").append(String.format(" %-15s | %-10s", "back", "메인으로 돌아가기"));

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
                        return SelectActionResult.exit();
                    } else {
                        return SelectActionResult.error("중복된 상품명이 존재합니다!");
                    }
                }
                case 2 -> {
                    return SelectActionResult.loop();
                }

                default -> {
                    throw new IndexOutOfBoundsException();
                }
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

    public SelectActionResult addProductInCategoryStart() {
        SelectActionResult result;

        do {
            result = addProductInCategoryProcess();

            if (result.getAction().equals(Actions.ERROR)) {
                System.out.println(result.getMessage());
            }
        } while (!result.getAction().equals(EXIT));

        return result;
    }
    //endregion

    //region 상품 수정 관련
    private Product modifyProductInCategorySearchDisplay() {
        System.out.print("수정할 상품명을 입력해주세요 : ");
        String productName = scanner.nextLine();

        Product searchProduct = productData.getAllProductList().stream()
                .filter(product -> product.getProductName().equals(productName))
                .findFirst().orElse(null);

        if (searchProduct == null) {
            return null;
        } else {
            System.out.println("현재 상품 정보 : " + searchProduct.printInfo());
            String conoleStrBuilder = "\n수정할 항목을 선택해주세요 : \n"
                                      + "1. 가격\n"
                                      + "2. 설명\n"
                                      + "3. 재고수량\n"
                                      + "0." + String.format(" %-15s | %-10s", "exit", "프로그램 종료") + "\n";
            System.out.println(conoleStrBuilder);
            return searchProduct;
        }
    }

    private SelectActionResult modifyProductInCategoryProcess() {
        int selectNum;

        try {
            Product modifyProduct = modifyProductInCategorySearchDisplay();

            if (modifyProduct == null) {
                return SelectActionResult.error("수정할 상품을 찾지 못했습니다!");
            }

            System.out.print("메뉴 번호를 입력해주세요 : ");
            selectNum = scanner.nextInt();
            scanner.nextLine();

            if (selectNum == 0) {
                return SelectActionResult.exit();
            }

            switch (selectNum) {
                // 가격 수정
                case 1 -> {
                    String formatModifyProductPrice = Util.formattingPrice(modifyProduct.getProductPrice());
                    System.out.println("현재 가격 : " + formatModifyProductPrice);
                    System.out.print("새로운 가격을 입력해주세요 : ");
                    int newPrice = scanner.nextInt();
                    scanner.nextLine();
                    String formatNewPrice = Util.formattingPrice(newPrice);

                    modifyProduct.setProductPrice(newPrice);
                    System.out.printf("\n< %s > 의 가격이 %s -> %s으로 수정되었습니다\n", modifyProduct.getProductName(), formatModifyProductPrice, formatNewPrice);
                    return SelectActionResult.exit();
                }
                case 2 -> {
                    System.out.println("현재 설명 : " + modifyProduct.getProductDescription());
                    System.out.print("새로운 설명을 입력해주세요 : ");
                    String newDescription = scanner.nextLine();

                    modifyProduct.setProductDescription(newDescription);
                    System.out.println("\n설명이 수정되었습니다\n");
                    return SelectActionResult.exit();
                }
                case 3 -> {
                    int modifyProductQuantity = modifyProduct.getProductQuantity();
                    System.out.println("현재 재고 : " + modifyProductQuantity + "개");
                    System.out.print("새로운 재고을 입력해주세요 : ");
                    int newQuantity = scanner.nextInt();
                    scanner.nextLine();

                    modifyProduct.setProductQuantity(newQuantity);
                    System.out.printf("\n< %s >의 재고가 %d개 -> %d개로 수정되었습니다\n", modifyProduct.getProductName(), modifyProductQuantity, newQuantity);
                    return SelectActionResult.exit();
                }
                default -> {
                    throw new IndexOutOfBoundsException();
                }
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

    public SelectActionResult modifyProductInCategoryStart() {
        SelectActionResult result;

        do {
            result = modifyProductInCategoryProcess();

            if (result.getAction().equals(Actions.ERROR)) {
                System.out.println(result.getMessage());
            }
        } while (!result.getAction().equals(EXIT));

        return result;
    }
    //endregion

    //region 상품 삭제 관련
    private void deleteProductInCategorySearchPreDisplay() {
        StringBuilder consoleStrBuilder = new StringBuilder();
        consoleStrBuilder.append("\n어느 카테고리에 상품을 제거하시겠습니까?\n");

        int index = 1;
        for (Category category : productData.getProductCategoryList()) {
            if (category.getCategoryType().equals(CategoryType.PRODUCT)) {
                consoleStrBuilder.append(index).append(". ").append(category.getCategoryName()).append("\n");
                index++;
            }
        }

        consoleStrBuilder.append("0.").append(String.format(" %-15s | %-10s", "back", "메인으로 돌아가기"));

        System.out.println(consoleStrBuilder);
    }

    private SelectActionResult deleteProductInCategoryPreProcess() {
        int selectNum;

        try {
            deleteProductInCategorySearchPreDisplay();

            System.out.print("\n상품을 제거할 카테고리 : ");
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

    private void deleteProductInCategoryDisplay(Category category) {
        System.out.printf("\n[ %s 카테고리에 상품 제거 ]\n", category.getCategoryName());
        StringBuilder consoleStrBuilder = new StringBuilder();

        int index = 1;
        for (Product product : category.getProductList()) {
            consoleStrBuilder.append(index).append(". ").append(product.printInfo()).append("\n");
            index++;
        }

        consoleStrBuilder.append("0.").append(String.format(" %-15s | %-10s", "back", "메인으로 돌아가기"));
        System.out.println(consoleStrBuilder);
    }

    private boolean askDelete(Product product) {
        System.out.printf("정말 < %s > 상품을 삭제하시겠습니까?\n", product.getProductName());
        System.out.println("1. 확인");
        System.out.println("2. 취소");
        System.out.println("\n메뉴 번호를 입력해주세요 : ");
        int selectNum = scanner.nextInt();
        scanner.nextLine();

        if (selectNum == 1) {
            return true;
        } else if (selectNum == 2) {
            return false;
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    private SelectActionResult deleteProductInCategoryProcess() {
        int selectNum;
        SelectActionResult result;
        Category selectCategory;

        try {
            result = deleteProductInCategoryPreProcess();

            if (result.getAction().equals(SELECTED)) {
                selectCategory = productData.getProductCategoryList().get(result.getSelectIndex());
            } else {
                return result;
            }

            deleteProductInCategoryDisplay(selectCategory);

            System.out.print("제거할 상품 번호를 입력해주세요 : ");
            selectNum = scanner.nextInt();
            scanner.nextLine();

            if (selectNum == 0) {
                return SelectActionResult.exit();
            }

            if (selectNum < 0 || selectNum > selectCategory.getProductList().size()) {
                throw new IndexOutOfBoundsException();
            }

            Product productToDelete = selectCategory.getProductList().get(selectNum - 1);
            if (!askDelete(productToDelete)) {
                return SelectActionResult.loop();
            }

            // 카테고리는 불변이라서 그냥 지울 수 있음
            if (!selectCategory.getProductList().removeIf(product ->
                    product.equals(productToDelete)
            )) {
                return SelectActionResult.error("< " + productToDelete.getProductName() + " > 상품 삭제에 실패하였습니다! 다시 한번 시도해주세요");
            } else {
                // 장바구니에서도 해당 상품 제거, 이름으로 비교해야함
                customer.getShoppingCart().removeProductToCartUsingProductName(productToDelete.getProductName());
                System.out.println("< " + productToDelete.getProductName() + " > 상품을 삭제하였습니다");
                return SelectActionResult.loop();
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

    public SelectActionResult deleteProductInCategoryStart() {
        SelectActionResult result;

        do {
            result = deleteProductInCategoryProcess();

            if (result.getAction().equals(Actions.ERROR)) {
                System.out.println(result.getMessage());
            }
        } while (!result.getAction().equals(EXIT));

        return result;
    }
    //endregion

    //region 전체 상품 현황
    private SelectActionResult showAllProductInCategoryProcess() {
        System.out.println("\n[ 전체 상품 현황 ]");

        for (Product product : productData.getAllProductList()) {
            System.out.println(product.printInfo());
        }

        return SelectActionResult.exit();
    }
    //endregion

    //region 관리자 메뉴 제어 관련
    private boolean authAdmin() {
        int loginTime = LOGIN_REMAIN_TIME;
        while (loginTime > 0) {
            System.out.print("관리자 비밀번호를 입력해주세요 : ");
            String inputPw = scanner.nextLine();

            if (inputPw.equals(adminPw)) {
                return true;
            }

            loginTime--;

            if (loginTime > 0) {
                System.out.println("비밀번호가 일치하지 않습니다");
            }
        }

        return false;
    }

    private void adminMenuDisplay() {
        String consoleStrBuilder = "\n[ 관리자 모드 ]\n"
                                   + "1. 상품 추가\n"
                                   + "2. 상품 수정\n"
                                   + "3. 상품 삭제\n"
                                   + "4. 전체 상품 현황\n"
                                   + "0." + String.format(" %-15s | %-10s", "exit", "프로그램 종료") + "\n";

        System.out.println(consoleStrBuilder);
    }

    private SelectActionResult adminMenuProcess() {
        int selectNum;

        try {
            if (!authAdmin()) {
                System.out.println("관리자 비밀번호를 확인 후 다시 시도하세요");
                return SelectActionResult.exit();
            }

            adminMenuDisplay();

            System.out.print("메뉴 번호를 입력해주세요 : ");
            selectNum = scanner.nextInt();
            scanner.nextLine();

            switch (selectNum) {
                case 1 -> {
                    return addProductInCategoryStart();
                }
                case 2 -> {
                    return modifyProductInCategoryStart();
                }
                case 3 -> {
                    return deleteProductInCategoryStart();
                }
                case 4 -> {
                    return showAllProductInCategoryProcess();
                }
                default -> throw new IndexOutOfBoundsException();
            }
        } catch (IndexOutOfBoundsException e) {
            return SelectActionResult.error("없는 번호를 입력하셨습니다\n");
        } catch (InputMismatchException e) {
            scanner.nextLine();
            return SelectActionResult.error("메뉴에 올바른 숫자로 입력해주세요\n");
        } catch (Exception e) {
            return SelectActionResult.error("오류 발생 : " + e.getLocalizedMessage() + "\n");
        }
    }

    public void adminMenuStart() {
        SelectActionResult result;
        do {
            result = adminMenuProcess();

            if (result.getAction().equals(ERROR)) {
                System.out.println(result.getMessage());
            }
        } while (!result.getAction().equals(EXIT));
    }
    //endregion
}
