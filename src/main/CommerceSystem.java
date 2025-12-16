package main;

import category.Category;
import product.Product;
import product.ProductFactory;

import java.util.*;

public class CommerceSystem {
    private final Scanner scanner = new Scanner(System.in);

    private int menuStart(List<Category> categoryList) {
        int cmdNumber = 0;

        try {
            StringBuilder consoleStrBuilder = new StringBuilder();
            consoleStrBuilder.append("[ 실시간 커머스 플랫폼 ]\n");
            int index = 1;
            for (Category category : categoryList) {
                consoleStrBuilder.append(index).append(". ").append(category.getCategoryName()).append("\n");
                index++;
            }

            consoleStrBuilder.append("0.").append(String.format(" %-15s | %-10s", "exit", "프로그램 종료"));
            System.out.println(consoleStrBuilder);
            System.out.print("메뉴 번호를 입력해주세요 : ");
            cmdNumber = scanner.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("메뉴 번호에 맞는 숫자를 입력해주세요");
        } catch (Exception e) {
            System.out.println("오류 발생 : ");
            System.out.println(e.getLocalizedMessage());
        }

        if (cmdNumber == 0) {
            return -1;
        } else {
            return cmdNumber;
        }
    }

    private int categoryStart(List<Category> categoryList, int selectMenuOrCategory) {
        int cmdNumber = 0;

        try {
            StringBuilder consoleStrBuilder = new StringBuilder();
            consoleStrBuilder.append(String.format("[ %s 카테고리 ]\n", categoryList.get(selectMenuOrCategory - 1).getCategoryName()));
            int index = 1;

            for (Product product : categoryList.get(selectMenuOrCategory - 1).getProductList()) {
                consoleStrBuilder.append(index).append(". ").append(product.printInfoButNotQty()).append("\n");
                index++;
            }

            consoleStrBuilder.append("0.").append(String.format(" %-15s | %-10s", "back", "뒤로가기"));

            System.out.println(consoleStrBuilder);
            System.out.print("상품 번호를 입력해주세요 : ");
            cmdNumber = scanner.nextInt();
        } catch (IndexOutOfBoundsException e) {
            System.out.println("등록된 카테고리가 없거나 없는 번호를 입력하셨습니다");
        } catch (InputMismatchException e) {
            System.out.println("메뉴 번호에 맞는 숫자를 입력해주세요");
        } catch (Exception e) {
            System.out.println("오류 발생 : ");
            System.out.println(e.getLocalizedMessage());
        }

        if (cmdNumber == 0) {
            return -1;
        } else {
            return cmdNumber;
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
        categoryList.add(ProductFactory.initCategory("전자제품", electricProductInfoArr));
        categoryList.add(ProductFactory.initCategory("의류", clothProductInfoArr));
        categoryList.add(ProductFactory.initCategory("식품", foodProductInfoArr));
        //endregion

        //region 메뉴 출력 및 입력
        while (true) {
            int selectMenuOrCategory;
            int selectItem;

            selectMenuOrCategory = menuStart(categoryList);

            if (selectMenuOrCategory == -1) {
                break;
            }

            selectItem = categoryStart(categoryList, selectMenuOrCategory);

            if (selectItem != -1) {
                System.out.print("선택한 상품 : ");
                System.out.println(categoryList.get(selectMenuOrCategory-1).getProductList().get(selectItem-1).printInfo());
            }
        }

        System.out.println("커머스 플랫폼을 종료합니다");
        //endregion
    }
}
