import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        //region CommerceSystem 객체 생성 및 초기화 수행
        List<Product> products = new ArrayList<>();
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

        CommerceSystem commerceSystem = new CommerceSystem(products);
        //endregion

        commerceSystem.start();
    }
}
