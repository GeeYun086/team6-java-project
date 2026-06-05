// InventoryManager.java 2514747 정유진

package model;

import java.util.ArrayList;
import java.util.List;

/**
 * 관리자용 재고 관리 클래스
 * 전체 상품 목록을 관리하며 재고 보충 및 품절 상품 조회 기능 제공
 * Product 객체를 직접 공유하여 구매 시스템과 동일한 재고 데이터를 사용
 */

public class InventoryManager {

    private List<Product> products;

    public InventoryManager(List<Product> products) {
        this.products = products;
    }

    /** 전체 상품 목록 반환 */
    public List<Product> getProducts() {
        return products;
    }

    /** 특정 상품의 재고를 보충 */
    public void restockProduct(
            String productId,
            int amount) {

        for (Product p : products) {

            // 상품 ID가 일치하면 재고 보충
            if (p.getId().equals(productId)) {

                p.restoreStock(amount);
                return;
            }
        }
    }

    /**
     * 재고 부족 상품 조회
     * 기준: 재고 2개 이하
     */
    public List<Product> getLowStockProducts() {

        List<Product> lowStockProducts =
                new ArrayList<>();

        // 전체 상품을 순회하며 재고 부족 상품 검색
        for (Product p : products) {

            if (p.getStock() <= 2 && p.getStock() > 0) {
                lowStockProducts.add(p);
            }
        }

        return lowStockProducts;
    }
    /** 품절 상품 목록 반환 */
    public List<Product> getOutOfStockProducts() {

        List<Product> outOfStockProducts =
                new ArrayList<>();

        // 전체 상품을 순회하며 품절 상품 검색
        for (Product p : products) {

            if (!p.isAvailable()) {
                outOfStockProducts.add(p);
            }
        }

        return outOfStockProducts;
    }
}
