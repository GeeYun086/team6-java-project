//InsufficientBalanceException.java 2516801 현진서
package model;

/**
 * [사용자 정의 예외] 잔액 부족 상황을 처리하기 위한 예외 클래스입니다.
 */
public class InsufficientBalanceException extends Exception {
    public InsufficientBalanceException(String message) {
        super(message);
    }
}