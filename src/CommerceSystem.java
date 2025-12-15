import java.util.List;
import java.util.Scanner;

public class CommerceSystem {
    List<Product> products;

    public CommerceSystem(List<Product> products) {
        this.products = products;
    }

    public void start() {
        String selectMenu;
        StringBuilder menuStrBuilder = new StringBuilder();
        Scanner scanner = new Scanner(System.in);

        //region 메뉴 출력 및 입력
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
        //endregion
    }
}
