// AdminController.java 2514747 정유진

package controller;

import model.InventoryManager;
import model.Product;
import model.SalesManager;
import model.SalesRecord;

import java.util.List;

/**
 * 관리자 기능을 제어하는 컨트롤러
 * 재고 조회, 재고 보충, 판매 기록 조회를 담당
 * View와 Model 사이의 중간 역할 수행
 */

public class AdminController {

    private InventoryManager inventoryManager;
    private SalesManager salesManager;

    public AdminController(
            InventoryManager inventoryManager,
            SalesManager salesManager) {

        this.inventoryManager = inventoryManager;
        this.salesManager = salesManager;
    }

    /** 전체 상품 목록 반환 */
    public List<Product> getProducts() {

        return inventoryManager.getProducts();
    }

    /** 품절 상품 목록 반환 */
    public List<Product> getOutOfStockProducts() {

        return inventoryManager.getOutOfStockProducts();
    }

    /** 특정 상품 재고 보충 */
    public void restockProduct(
            String productId,
            int amount) {

        inventoryManager.restockProduct(
                productId,
                amount);
    }

    /** 판매 기록 추가 */
    public void addSalesRecord(
            SalesRecord record) {

        salesManager.addRecord(record);
    }

    /** 전체 판매 기록 반환 */
    public List<SalesRecord> getSalesRecords() {

        return salesManager.getRecords();
    }

    /** 판매 기록 파일 저장 */
    public void saveSalesRecord() {

        salesManager.saveToFile();
    }
}
