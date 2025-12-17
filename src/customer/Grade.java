package customer;

import java.math.BigDecimal;
import java.math.RoundingMode;

public enum Grade {
    BRONZE(0)
    , SILVER(5)
    , GOLD(10)
    , PLATINUM(15);

    public final int discountRate;

    Grade(int discountRate) {
        this.discountRate = discountRate;
    }

    // 할인 가격을 출력
    public int getDiscountPrice(int price) {
        return BigDecimal.valueOf(price)
                .multiply(BigDecimal.valueOf(this.discountRate))
                .divide(BigDecimal.valueOf(100), 0, RoundingMode.DOWN).intValue();
    }

    // 최종 할인 가격 (혹시 모를 100% 넘는 할인시 0 출력)
    public int getDiscountedPrice(int price) {
        return Math.max(0, price - getDiscountPrice(price));
    }
}
