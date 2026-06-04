//OutOfStockException.java 2516801 현진서
package model;

/**
 * [사용자 정의 예외] 상품 재고가 품절된 상황을 처리하기 위한 예외 클래스입니다.
 * 에러 발생 시 프로그램 강제 종료를 막고 안전하게 복구 흐름으로 유도하기 위해 사용됩니다.
 */
public class OutOfStockException extends Exception {
    public OutOfStockException(String message) {
        super(message);
    }
}