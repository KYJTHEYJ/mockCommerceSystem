import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String selectMenu;
        StringBuilder menuStrBuilder = new StringBuilder();
        List<Product> products = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);

        Object[][] productInfoArr = {
                {"Galaxy S26", 1170000, "최신 Android 스마트폰", 100}
                , {"iPhone 18", 1300000, "최신 IOS 스마트폰", 100}
                , {"MacBook Pro", 1800000, "최신 MacBook Pro", 100}
                , {"MacBook Air", 1250000, "최신 MacBook Air", 100}
        };

        for (Object[] productInfo : productInfoArr) {
            products.add(
                    new Product(
                            (String) productInfo[0]
                            , (int) productInfo[1]
                            , (String) productInfo[2]
                            , (int) productInfo[3]
                    )
            );
        }

        menuStrBuilder.append("[ 실시간 커머스 플랫폼 - 전자제품 ]\n");
        int index = 1;
        for(Product product : products) {
            menuStrBuilder.append(index).append('.').append(product.printInfoButNotQty()).append("\n");
            if(index == products.size()) {
                menuStrBuilder.append("0.").append(String.format(" %-15s | %-10s", "exit", "프로그램 종료"));
            }
            index++;
        }
        System.out.println(menuStrBuilder);
        System.out.print("메뉴 번호를 입력해주세요 : ");
        selectMenu = scanner.nextLine();

        if(selectMenu.equals("0")) {
            return;
        }

    }
}
