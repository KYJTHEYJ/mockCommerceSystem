package main.system;

import category.Category;
import category.CategoryFactory;
import main.system.action.SelectActionResult;
import product.Product;
import product.ProductFactory;

import java.util.*;

import static main.system.action.Actions.*;

public class CommerceSystem {
    private final Scanner scanner = new Scanner(System.in);

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
            return SelectActionResult.error("없는 번호를 입력하셨습니다");
        } catch (InputMismatchException e) {
            return SelectActionResult.error("메뉴 번호에 맞는 숫자를 입력해주세요");
        } catch (Exception e) {
            return SelectActionResult.error("오류 발생 : " + e.getLocalizedMessage());
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
            return SelectActionResult.error("없는 번호를 입력하셨습니다");
        } catch (InputMismatchException e) {
            return SelectActionResult.error("메뉴 번호에 맞는 숫자를 입력해주세요");
        } catch (Exception e) {
            return SelectActionResult.error("오류 발생 : " + e.getLocalizedMessage());
        }
    }

    public void start() {
        List<Category> categoryList = new ArrayList<>();

        //region 전자 카테고리별 상품
        Object[][] electricProductInfoArr = {
                {"Galaxy S26", 1170000, "최신 Android 스마트폰", 100}
                , {"iPhone 18", 1300000, "최신 IOS 스마트폰", 100}
                , {"MacBook Pro", 1800000, "최신 MacBook Pro", 100}
                , {"MacBook Air", 1250000, "최신 MacBook Air", 100}
        };
        //endregion

        //region 의류 카테고리별 상품
        Object[][] clothProductInfoArr = {};
        //endregion

        //region 식품 카테고리별 상품
        Object[][] foodProductInfoArr = {};
        //endregion

        //region 카테고리 추가
        categoryList.add(CategoryFactory.initCategory("전자제품", ProductFactory.createProductListFromProductInfo(electricProductInfoArr)));
        categoryList.add(CategoryFactory.initCategory("의류", ProductFactory.createProductListFromProductInfo(clothProductInfoArr)));
        categoryList.add(CategoryFactory.initCategory("식품", ProductFactory.createProductListFromProductInfo(foodProductInfoArr)));
        //endregion

        //region 메뉴 출력 및 입력
        SelectActionResult menu;
        SelectActionResult category;

        while (true) {
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
                        try {
                            System.out.print("선택한 상품 : ");
                            System.out.println(categoryList.get(menu.getSelectIndex()).getProductList().get(category.getSelectIndex()).printInfo());
                        } catch (IndexOutOfBoundsException e) {
                            System.out.println(category.getMessage());
                        }
                    }
                }
            } while (category.getAction().equals(ERROR));
        }

        System.out.println("커머스 프로그램을 종료합니다");
        //endregion
    }
}
