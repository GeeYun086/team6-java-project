// PurchaseController.java 2216899 정지윤
package controller;

import model.Cart;
import model.Product;
import view.MainView;
import view.PurchaseView;

// [결제 및 오류 처리 파트] 2516801 현진서
import controller.PaymentController;
import model.Payment;
import model.Inventory;
import view.PaymentView;

import javax.swing.*;
import java.util.Map;

/**
 * 사용자 구매 흐름을 제어하는 컨트롤러
 * 상품 선택, 장바구니 담기/빼기, 결제 확인, 재고 차감 순서로 동작
 * View는 setter로 주입받아 결합도를 낮춤
 */
public class PurchaseController {

    private final Cart cart;
    private MainView mainView;

    public PurchaseController(Cart cart) {
        this.cart = cart;
    }

    /** MainView 주입. 생성자 대신 setter를 쓰는 이유: 순환 참조 방지 */
    public void setMainView(MainView mainView) {
        this.mainView = mainView;
    }

    private PaymentController paymentController;
    public void setPaymentController(PaymentController pc) {
        this.paymentController = pc;
    }

    /** 상품 1개 담기. 품절/재고 초과 시 경고 후 중단 */
    public void addToCart(Product product) {
        if (!product.isAvailable()) {
            mainView.showAlert("해당 상품은 현재 품절입니다.");
            return;
        }
        // 장바구니에 이미 담긴 수량이 실제 재고를 넘지 않도록 확인
        int inCart = cart.getQuantity(product);
        if (inCart >= product.getStock()) {
            mainView.showAlert("재고가 부족합니다. (현재 재고: " + product.getStock() + "개)");
            return;
        }
        cart.addItem(product);
        mainView.refresh();
    }

    /** 장바구니 수량 1 감소. 0이 되면 삭제 */
    public void decreaseFromCart(Product product) {
        cart.removeItem(product);
        mainView.refresh();
    }

    /** 장바구니 전체 취소. 확인 창 먼저 표시 */
    public void cancelCart() {
        if (cart.isEmpty()) return;
        int choice = JOptionPane.showConfirmDialog(
            mainView,
            "장바구니를 모두 비우시겠습니까?",
            "취소 확인",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );
        if (choice == JOptionPane.YES_OPTION) {
            cart.clear();
            mainView.refresh();
        }
    }

    /** 결제 확인 다이얼로그 열기. 확인 시 processPurchase 실행 */
    public void requestPurchase() {
        if (cart.isEmpty()) {
            mainView.showAlert("장바구니가 비어 있습니다.\n상품을 먼저 담아주세요.");
            return;
        }
        PurchaseView purchaseView = new PurchaseView(mainView, cart);
        purchaseView.setVisible(true);

        if (purchaseView.isConfirmed()) {
            processPurchase();
        }
    }

    /** 재고 최종 검증 → 차감 → 장바구니 초기화 → 완료 메시지 */
    private void processPurchase() {
        Map<Product, Integer> items = cart.getItems();

        // 하나라도 재고가 부족하면 전체 결제 취소
        for (Map.Entry<Product, Integer> entry : items.entrySet()) {
            Product p = entry.getKey();
            int qty   = entry.getValue();
            if (p.getStock() < qty) {
                mainView.showAlert("[" + p.getName() + "] 재고가 부족하여 결제할 수 없습니다.\n"
                        + "(현재 재고: " + p.getStock() + "개, 요청: " + qty + "개)");
                return;
            }
        }

        // [결제 및 오류 처리 파트 연결]
        int totalPrice = 0;
        String summaryName = "";
        int typeCount = 0;

        for (Map.Entry<Product, Integer> entry : items.entrySet()) {
            Product p = entry.getKey();
            int qty = entry.getValue();
            
            totalPrice += p.getPrice() * qty; // 총액 계산
            if (typeCount == 0) {
                summaryName = p.getName();
            }
            typeCount++;
            
            entry.getKey().reduceStock(entry.getValue()); 
        }
        
        if (typeCount > 1) {
            summaryName += " 외 " + (typeCount - 1) + "건";
        }

        // 결제창 화면 띄우기
        Payment paymentModel = new Payment(totalPrice);
        Inventory inventoryModel = new Inventory(); // 재고 검증은 위에서 끝났으므로 더미 값 전달
        PaymentView paymentView = new PaymentView(summaryName, totalPrice);
        
        this.paymentController = new PaymentController(paymentModel, inventoryModel, paymentView, summaryName);
        paymentView.setVisible(true);

        cart.clear();
        mainView.refresh();
    }
}