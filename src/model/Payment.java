//Payment.java 2516801 현진서
package model;


/**
 * [Model] 결제 금액과 투입 금액 데이터를 관리하는 클래스입니다.
 * 정보 은닉(Encapsulation) 원칙에 따라 필드를 private으로 보호하며,
 * 계산의 책임(거스름돈 등)을 외부가 아닌 Payment 객체 스스로가 지도록 설계했습니다.
 */
public class Payment {
    private int totalAmount; // 결제해야 할 총 금액 (정보 은닉)
    private int inputMoney;  // 사용자가 투입한 금액 (정보 은닉)

    public Payment(int totalAmount) {
        this.totalAmount = totalAmount;
        this.inputMoney = 0;
    }

    // 외부에서 데이터에 안전하게 접근할 수 있도록 Getter 제공
    public int getTotalAmount() { return totalAmount; }
    public int getInputMoney() { return inputMoney; }

    public void addMoney(int money) {
        this.inputMoney += money;
    }

    /**
     * 투입 금액이 총 결제 금액 이상인지 검증합니다.
     * 부족할 경우 InsufficientBalanceException을 발생시켜 Controller로 처리를 위임합니다.
     */
    public void checkBalance() throws InsufficientBalanceException {
        if (inputMoney < totalAmount) {
            throw new InsufficientBalanceException(
                "잔액이 부족합니다. (상품 가격: " + totalAmount + "원 / 투입 금액: " + inputMoney + "원)"
            );
        }
    }

    // 객체 스스로 책임을 지는 구조 (View나 Controller가 직접 계산하지 않음)
    public int calculateChange() {
        return inputMoney - totalAmount;
    }

    // 결제 오류 시 투입 금액 초기화 (안전한 시스템 복구)
    public void refund() {
        this.inputMoney = 0;
    }
}