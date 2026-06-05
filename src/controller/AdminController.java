// AdminController.java 2514747 정유진

package controller;

import model.InventoryManager;
import model.Product;
import model.SalesManager;
import model.SalesRecord;

import java.util.List;
import javax.swing.JOptionPane;

/**
 * 관리자 기능을 제어하는 컨트롤러
 * 재고 조회, 재고 부족 상품 조회, 품절 상품 조회 재고 보충, 판매 기록 조회
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

    /**재고 부족 상품 조회
     * InventoryManager에 요청
     */
    public List<Product> getLowStockProducts() {

        return inventoryManager.getLowStockProducts();
    }

    /** 품절 상품 목록 반환 */
    public List<Product> getOutOfStockProducts() {

        return inventoryManager.getOutOfStockProducts();
    }

    /** 상품명으로 재고 보충 가능 여부 확인 */
    public boolean existsProductName(String productName) {

        for (Product p : inventoryManager.getProducts()) {

            if (p.getName().equals(productName)) {
                return true;
            }
        }

        return false;
    }
    
    /**
    * 상품명으로 재고 보충
    * 성공 시 true
    * 실패 시 false
    */
    public boolean restockProductByName(
            String productName,
            int amount) {

        productName = productName.trim();

        for (Product p : inventoryManager.getProducts()) {

            if (p.getName().equals(productName)) {
                p.restoreStock(amount);           
                return true;
            }
        }

        return false;
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
