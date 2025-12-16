package main;

import main.system.CommerceSystem;

public class Main {
    public static void main(String[] args) {
        //region main.system.CommerceSystem 객체 생성 및 초기화 수행
        CommerceSystem commerceSystem = new CommerceSystem();
        //endregion

        commerceSystem.start();
    }
}
