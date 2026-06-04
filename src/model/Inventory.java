//Inventory.java 2516801 현진서
package model;

// 같은 model 패키지 안에 OutOfStockException이 있으므로 import 문이 필요 없습니다.

/**
 * [Model] 상품의 재고 상태를 관리하는 클래스입니다.
 * 다중 스레드(멀티스레드) 환경에서 동시 접근으로 인한 데이터 무결성 훼손을 방지합니다.
 */
public class Inventory {
    private int stock = 5; // 임시 재고 세팅

    /**
     * 재고 상태를 확인합니다. 0 이하일 경우 예외를 던집니다.
     */
    public void checkStock() throws OutOfStockException {
        if (stock <= 0) {
            throw new OutOfStockException("선택하신 상품은 현재 품절입니다.");
        }
    }

    /**
     * [핵심 기술: 동기화(Synchronization)]
     * 여러 사용자가 동시에 결제를 시도할 때 재고가 중복 차감되는 것을 막기 위해
     * synchronized 키워드를 적용하여 임계 구역(Critical Section)을 보호합니다.
     */
    public synchronized void decreaseStock() {
        if (stock > 0) stock--;
    }
}