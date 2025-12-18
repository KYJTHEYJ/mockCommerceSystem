package main.system.action;

public class SelectActionResult {
    protected Actions action;
    protected int selectNumber;
    protected String message;

    // SELECTED
    public SelectActionResult(Actions action, int selectNumber) {
        this.action = action;
        this.selectNumber = selectNumber;
        this.message = null;
    }

    // ERROR
    public SelectActionResult(Actions action, String message) {
        this.action = action;
        this.selectNumber = -1;
        this.message = message;
    }

    // EXIT
    public SelectActionResult(Actions action) {
        this.action = action;
        this.selectNumber = -1;
        this.message = null;
    }

    // LOOP 반복으로 진행해야할 구문이 있다는 것을 안내

    public Actions getAction() {
        return action;
    }

    public int getSelectNumber() {
        return selectNumber;
    }

    public int getSelectIndex() {
        return selectNumber - 1;
    }

    public String getMessage() {
        return message;
    }

    // 메뉴 선택기능 관련 생성
    public static SelectActionResult error(String message) {
        return new SelectActionResult(Actions.ERROR, message);
    }

    public static SelectActionResult selected(int selectNumber) {
        return new SelectActionResult(Actions.SELECTED, selectNumber);
    }

    public static SelectActionResult exit() {
        return new SelectActionResult(Actions.EXIT);
    }

    public static SelectActionResult loop() {
        return new SelectActionResult(Actions.LOOP);
    }
}
