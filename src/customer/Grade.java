package customer;

public enum Grade {
    BRONZE(0) {
        @Override
        public int discountPrice(int price) {
            return price;
        }
    }
    , SILVER(5) {
        @Override
        public int discountPrice(int price) {
            return (int) (price * 0.05);
        }
    }
    , GOLD(10) {
        @Override
        public int discountPrice(int price) {
            return (int) (price * 0.1);
        }
    }
    , PLANTINUM(15) {
        @Override
        public int discountPrice(int price) {
            return (int) (price * 0.15);
        }
    };

    public final int discountRate;

    Grade(int discountRate) {
        this.discountRate = discountRate;
    }

    public abstract int discountPrice(int price);
}
