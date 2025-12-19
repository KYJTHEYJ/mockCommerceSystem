package main.system.process;

import customer.Customer;
import main.system.action.SelectActionResult;
import main.system.util.Util;
import product.OrderingProduct;
import product.Product;

import java.util.InputMismatchException;
import java.util.Scanner;

import static main.system.action.Actions.*;

// 장바구니 기능 담당 클래스
public class ShoppingCartProcess {
    private final Scanner scanner;
    private final Customer customer;

    public ShoppingCartProcess(Scanner scanner, Customer customer) {
        this.scanner = scanner;
        this.customer = customer;
    }

    //region 장바구니 상품 추가 기능 관련
    private void addProductShoppingCartDisplay(Product product) {
        String consoleStrBuilder = "선택한 상품 : "
                                   + product.printInfo() + "\n\n"
                                   + product.printInfoForAddShoppingCart() + "\n"
                                   + "위 상품을 장바구니에 추가하시겠습니까?\n"
                                   + "1. 확인\n" + "2. 취소\n";

        System.out.println(consoleStrBuilder);
    }

    private SelectActionResult addProductShoppingCartProcess(Product product) {
        int selectNum;

        try {
            addProductShoppingCartDisplay(product);

            System.out.print("메뉴 번호를 입력해주세요 : ");
            selectNum = scanner.nextInt();
            scanner.nextLine();

            if (selectNum == 2) {
                return SelectActionResult.exit();
            }

            if (selectNum != 1) {
                throw new IndexOutOfBoundsException();
            } else {
                if (product.getProductQuantity() > 0) {
                    OrderingProduct inCartProduct = customer.getShoppingCart().getOrderingProductList().stream()
                            .filter(orderingProduct ->
                                    orderingProduct.getProduct().equals(product)
                            ).toList().stream().findFirst().orElse(null);

                    if (inCartProduct != null) {
                        if (inCartProduct.getQuantity() >= product.getProductQuantity()) {
                            return SelectActionResult.error(product.getProductName() + " 상품의 재고가 부족합니다!\n");
                        }
                    }

                    customer.getShoppingCart().addProductToCart(product);
                    System.out.print(product.getProductName() + "이(가) 장바구니에 추가되었습니다\n");
                    return SelectActionResult.selected(selectNum);
                } else {
                    return SelectActionResult.error(product.getProductName() + " 상품의 재고가 없습니다!\n");
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

    public void addProductShoppingCartStart(Product product) {
        SelectActionResult result;

        do {
            result = addProductShoppingCartProcess(product);

            if (result.getAction().equals(ERROR)) {
                System.out.println(result.getMessage());
            }
        } while (!result.getAction().equals(SELECTED) && !result.getAction().equals(EXIT));
    }
    //endregion

    //region 장바구니 상품 주문 관련
    //region 장바구니에서 주문 기능
    private void shoppingCartToOrderDisplay() {
        StringBuilder consoleStrBuilder = new StringBuilder();
        consoleStrBuilder.append("\n[ 장바구니 내역 주문하기 ]\n");
        consoleStrBuilder.append("[ 장바구니 내역 ]\n");

        for (OrderingProduct OrderingProduct : customer.getShoppingCart().getOrderingProductList()) {
            consoleStrBuilder.append(OrderingProduct.printOrderingCart()).append("\n");
        }

        consoleStrBuilder.append("[ 총 주문 금액 ]\n");
        consoleStrBuilder.append("할인 전 금액 : ").append(Util.formattingPrice(customer.getShoppingCart().getShoppingCartOriginSumPrice())).append("\n");
        consoleStrBuilder.append(String.format("%s 고객님의 등급 %s 할인율 (%d%%) 할인된 금액: %s\n"
                , customer.getName()
                , customer.getGrade().name()
                , customer.getGrade().discountRate
                , Util.formattingPrice(customer.getShoppingCart().getShoppingCartDiscountPrice(customer))));
        consoleStrBuilder.append("\n< 최종 주문 금액 > ").append(Util.formattingPrice(customer.getShoppingCart().getShoppingCartSumPrice(customer))).append("\n");
        consoleStrBuilder.append("1. 주문 확정\n")
                .append("2. 메인으로 돌아가기\n");

        System.out.println(consoleStrBuilder);
    }

    public SelectActionResult shoppingCartToOrderProcess() {
        int selectNum;

        try {
            shoppingCartToOrderDisplay();

            System.out.print("메뉴 번호를 입력해주세요 : ");
            selectNum = scanner.nextInt();
            scanner.nextLine();

            switch (selectNum) {
                case 1 -> {
                    // 장바구니 수량이 상품 재고보다 많으면 주문 불가 추가
                    for (OrderingProduct OrderingProduct : customer.getShoppingCart().getOrderingProductList()) {
                        if (OrderingProduct.getQuantity() > OrderingProduct.getProduct().getProductQuantity()) {
                            return SelectActionResult.error(
                                    String.format("< %s > 상품의 재고가 부족합니다! (장바구니 수량: %d개 재고: %d개)\n",
                                            OrderingProduct.getProduct().getProductName()
                                            , OrderingProduct.getQuantity()
                                            , OrderingProduct.getProduct().getProductQuantity())
                            );
                        }
                    }

                    System.out.println("주문이 완료 되었습니다!\n < 결제 금액 > " + Util.formattingPrice(customer.getShoppingCart().getShoppingCartSumPrice(customer)) + "원\n");

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

    public void shoppingCartToOrderStart() {
        SelectActionResult result;
        do {
            result = shoppingCartToOrderProcess();

            if (result.getAction().equals(ERROR)) {
                System.out.println(result.getMessage());
            }
        } while (!result.getAction().equals(SELECTED) && !result.getAction().equals(EXIT));
    }
    //endregion

    //region 장바구니 상품 취소 기능
    private void shoppingCartClearDisplay() {
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

    private SelectActionResult shoppingCartClearProcess() {
        int selectNum;

        try {
            shoppingCartClearDisplay();

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

    public void shoppingCartClearStart() {
        SelectActionResult result;
        do {
            result = shoppingCartClearProcess();

            if (result.getAction().equals(ERROR)) {
                System.out.println(result.getMessage());
            }
        } while (!result.getAction().equals(EXIT));
    }
    //endregion
    //endregion
}
