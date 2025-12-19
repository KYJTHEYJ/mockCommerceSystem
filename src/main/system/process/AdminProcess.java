package main.system.process;

// TODO 작성 중
// 관리자 기능 담당 클래스
public class AdminProcess {
    private void adminDisplay() {
        StringBuilder consoleStrBuilder = new StringBuilder();
        consoleStrBuilder.append("\n[ 관리자 모드 ]\n");

        consoleStrBuilder.append("1. 상품 추가\n")
                .append("2. 상품 수정\n")
                .append("3. 상품 삭제\n")
                .append("4. 전체 상품 현황\n");
        consoleStrBuilder.append("0.").append(String.format(" %-15s | %-10s", "exit", "프로그램 종료")).append("\n");

        System.out.println(consoleStrBuilder);
    }
}
