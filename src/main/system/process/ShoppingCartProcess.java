package main.system.process;

import customer.Customer;
import main.system.action.SelectActionResult;
import product.OrderingProduct;
import product.Product;

import java.text.DecimalFormat;
import java.util.InputMismatchException;
import java.util.Scanner;

import static main.system.action.Actions.*;

// 장바구니 프로세스 담당 클래스
public class ShoppingCartProcess {
    private final Scanner scanner;

    public ShoppingCartProcess (Scanner scanner) {
        this.scanner = scanner;
    }

    private void shoppingCartToOrderDisplay(Customer customer) {
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

    public SelectActionResult shoppingCartToOrderProcess(Customer customer) {
        int selectNum;

        try {
            shoppingCartToOrderDisplay(customer);

            System.out.print("메뉴 번호를 입력해주세요 : ");
            selectNum = scanner.nextInt();
            scanner.nextLine();

            switch (selectNum) {
                case 1 -> {
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

                    // 주문 했으니 장바구니 비우기
                    customer.getShoppingCart().getOrderingProductList().clear();

                    return SelectActionResult.selected(selectNum);
                }
                case 2 -> {
                    return SelectActionResult.exit();
                }
                default -> throw new IndexOutOfBoundsException();
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

    public void shoppingCartToOrderStart(Customer customer) {
        SelectActionResult result;
        do {
            result = shoppingCartToOrderProcess(customer);

            if (result.getAction().equals(ERROR)) {
                System.out.println(result.getMessage());
            }
        } while (!result.getAction().equals(SELECTED) && !result.getAction().equals(EXIT));
    }

    private void shoppingCartClearDisplay(Customer customer) {
        StringBuilder consoleStrBuilder = new StringBuilder();
        consoleStrBuilder.append("\n[ 장바구니 내역 ]\n");

        for (OrderingProduct OrderingProduct : customer.getShoppingCart().getOrderingProductList()) {
            consoleStrBuilder.append(OrderingProduct.printOrderingCart()).append("\n");
        }

        consoleStrBuilder.append("1. 상품 취소\n")
                .append("2. 전체 상품 취소\n")
                .append("3. 메인으로 돌아가기\n");

        System.out.println(consoleStrBuilder);
    }

    private SelectActionResult shoppingCartClearProcess(Customer customer) {
        int selectNum;

        try {
            shoppingCartClearDisplay(customer);

            System.out.print("메뉴 번호를 입력해주세요 : ");
            selectNum = scanner.nextInt();
            scanner.nextLine();

            switch (selectNum) {
                case 1 -> {
                    System.out.print("취소할 상품 이름을 입력해주세요 : ");
                    String productName = scanner.nextLine();

                    if (customer.getShoppingCart().removeProductToCartUsingProductName(productName)) {
                        System.out.println("< " + productName + " > 상품을 취소하였습니다");

                        if (customer.getShoppingCart().getOrderingProductList().isEmpty()) {
                            return SelectActionResult.exit();
                        } else {
                            return SelectActionResult.loop();
                        }
                    } else {
                        System.out.println("< " + productName + " > 상품은 장바구니에 없습니다!");
                        return SelectActionResult.exit();
                    }
                }
                case 2 -> {
                    System.out.println("장바구니 안 주문들을 전부 취소하였습니다!\n");
                    customer.getShoppingCart().getOrderingProductList().clear();
                    return SelectActionResult.exit();
                }
                case 3 -> {
                    return SelectActionResult.exit();
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

    public void shoppingCartClearStart(Customer customer) {
        SelectActionResult result;
        do {
            result = shoppingCartClearProcess(customer);

            if (result.getAction().equals(ERROR)) {
                System.out.println(result.getMessage());
            }
        } while (!result.getAction().equals(EXIT));
    }
}
