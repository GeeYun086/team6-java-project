// SalesRecord.java 2514747 정유진

package model;

import java.time.LocalDateTime;

/**
 * 판매 기록 객체
 * 상품명, 판매 수량, 판매 시각을 저장
 * 판매 기록 1건을 객체로 표현
 */

public class SalesRecord {

    private String productName;
    private int quantity;
    private LocalDateTime saleTime;

    public SalesRecord(
            String productName,
            int quantity) {

        this.productName = productName;
        this.quantity = quantity;
        this.saleTime = LocalDateTime.now();
    }

    /** 판매된 상품명 반환 */
    public String getProductName() {
        return productName;
    }

    /** 판매 수량 반환 */
    public int getQuantity() {
        return quantity;
    }

    /** 판매 시각 반환 */
    public LocalDateTime getSaleTime() {
        return saleTime;
    }

    @Override
    public String toString() {

        return productName
                + " / "
                + quantity
                + "개 / "
                + saleTime;
    }
}
