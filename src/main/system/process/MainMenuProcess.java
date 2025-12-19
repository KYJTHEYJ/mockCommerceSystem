package main.system.process;

import category.Category;
import category.CategoryType;
import main.system.action.SelectActionResult;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

// 초기 메인 메뉴 담당 클래스
// 카테고리를 출력하여 메인 메뉴 -> 카테고리로 이어지도록 연결
public class MainMenuProcess {
    Scanner scanner;

    public MainMenuProcess(Scanner scanner) {
        this.scanner = scanner;
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

    private void menuDisplay(List<Category> categoryList) {
        List<Category> productCategoryList = categoryList.stream().filter(category -> category.getCategoryType().equals(CategoryType.PRODUCT)).toList();
        List<Category> orderManageList = categoryList.stream().filter(category -> category.getCategoryType().equals(CategoryType.ORDER)).toList();
        List<Category> adminList = categoryList.stream().filter(category -> category.getCategoryType().equals(CategoryType.ADMIN)).toList();
        StringBuilder consoleStrBuilder = new StringBuilder();

        // 메인 메뉴 디스플레이
        consoleStrBuilder.append("\n[ 실시간 커머스 플랫폼 ]\n");
        int index = 1;
        index = printCategory(productCategoryList, consoleStrBuilder, index);

        consoleStrBuilder.append("0.").append(String.format(" %-15s | %-10s", "exit", "프로그램 종료")).append("\n");

        // 관리자 기능 디스플레이
        if (!adminList.isEmpty()) {
            index = printCategory(adminList, consoleStrBuilder, index);
        }

        // 주문 관련 디스플레이
        if (!orderManageList.isEmpty()) {
            consoleStrBuilder.append("\n[ 주문 관리 ]\n");
        }

        if (!orderManageList.isEmpty()) {
            index = printCategory(orderManageList, consoleStrBuilder, index);
        }
        //endregion

        System.out.println(consoleStrBuilder);
    }

    public SelectActionResult menuProcess(List<Category> categoryList) {
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
            scanner.nextLine();
            return SelectActionResult.error("메뉴에 올바른 숫자를 입력해주세요\n");
        } catch (Exception e) {
            return SelectActionResult.error("오류 발생 : " + e.getLocalizedMessage() + "\n");
        }
    }
}
