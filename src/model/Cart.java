// Cart.java 2216899 정지윤
package model;

import java.util.HashMap;
import java.util.Map;

/**
 * 사용자가 담은 상품과 수량을 관리하는 장바구니
 * HashMap으로 상품(키)과 수량(값)을 저장하고, 모든 메서드에 synchronized를 적용해 여러 스레드가 동시에 접근해도 데이터가 꼬이지 않도록 함
 */
public class Cart {

    private final Map<Product, Integer> items;

    public Cart() {
        items = new HashMap<>();
    }

    /** 상품 1개 추가, 이미 있으면 수량 +1 */
    public synchronized void addItem(Product product) {
        int current = items.getOrDefault(product, 0);
        items.put(product, current + 1);
    }

    /** 수량 1 감소, 0이 되면 항목 삭제 */
    public synchronized void removeItem(Product product) {
        if (!items.containsKey(product)) return;
        int qty = items.get(product);
        if (qty <= 1) {
            items.remove(product);
        } else {
            items.put(product, qty - 1);
        }
    }

    /** 전체 합계 금액 반환 */
    public synchronized int getTotalPrice() {
        int total = 0;
        for (Map.Entry<Product, Integer> entry : items.entrySet()) {
            total += entry.getKey().getPrice() * entry.getValue();
        }
        return total;
    }

    /** 장바구니 복사본 반환 (원본 보호) */
    public synchronized Map<Product, Integer> getItems() {
        return new HashMap<>(items);
    }

    /** 특정 상품의 담긴 수량 반환 */
    public synchronized int getQuantity(Product product) {
        return items.getOrDefault(product, 0);
    }

    /** 장바구니 전체 비우기 */
    public synchronized void clear() {
        items.clear();
    }

    public synchronized boolean isEmpty() {
        return items.isEmpty();
    }
}
