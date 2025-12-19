package main.system.util;

import java.text.DecimalFormat;

public class Util {

    private Util() {}

    public static String formattingPrice(int price) {
        return new DecimalFormat("###,###").format(price) + "원";
    }
}
