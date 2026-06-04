//Transaction.java 2516801 현진서
package model;

import java.time.LocalDateTime;

/**
 * [Model] 결제가 완료된 후 영수증 생성을 위한 거래 내역 데이터를 담당합니다.
 * 한 번 생성된 거래 내역은 변경될 필요가 없으므로 Setter를 제외하여 불변성을 유지합니다.
 */
public class Transaction {
    private String transactionId;
    private String productName;
    private int amount;
    private int change;
    private LocalDateTime time;

    public Transaction(String transactionId, String productName, int amount, int change) {
        this.transactionId = transactionId;
        this.productName = productName;
        this.amount = amount;
        this.change = change;
        this.time = LocalDateTime.now();
    }

    // 외부 UI에 출력할 영수증 텍스트 폼 생성
    public String getReceiptText() {
        return "☑ 결제 성공!\n\n" +
               "거래 ID: " + transactionId + "\n" +
               "상품명: " + productName + "\n" +
               "결제금액: " + amount + "원\n" +
               "거스름돈: " + change + "원\n" +
               "결제시간: " + time.toString();
    }
}