// PaymentController.java 2516801 현진서
package controller;

import model.Cart;
import model.Product;
import model.SalesManager;
import model.SalesRecord;
import model.Payment;
import model.Transaction;
import model.InsufficientBalanceException;
import model.OutOfStockException;
import view.MainView;
import view.PaymentView;
import view.ErrorMessageView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.UUID;

/**
 * 결제 및 오류 처리를 제어하는 컨트롤러 클래스입니다.
 * 사용자의 결제 금액 입력, 잔액 및 재고 검증, 결제 완료 후 영수증 출력 등 전체 결제 흐름을 담당합니다.
 */
public class PaymentController {
    
    private final SalesManager salesManager;
    private final ErrorMessageView errorView;

    /**
     * PaymentController 생성자.
     * * @param salesManager 판매 기록을 관리하는 매니저 객체
     * @param errorView    오류 발생 시 사용자에게 메시지를 띄우는 뷰 객체
     */
    public PaymentController(SalesManager salesManager, ErrorMessageView errorView) {
        this.salesManager = salesManager;
        this.errorView = errorView;
    }

    /**
     * 장바구니 데이터를 바탕으로 결제창을 띄우고 결제 프로세스를 진행합니다.
     * * @param cart     결제할 상품들이 담긴 장바구니 객체
     * @param mainView 결제 완료 후 새로고침을 수행할 메인 화면 객체
     */
    public void processPayment(Cart cart, MainView mainView) {
        Map<Product, Integer> items = cart.getItems();
        if (items.isEmpty()) return;

        // Step 1: 영수증에 출력할 대표 상품명 텍스트 조립 (예: "생수 외 2건")
        String tempProductName = "";
        int count = 0;
        
        for (Product p : items.keySet()) {
            if (count == 0) tempProductName = p.getName();
            count++;
        }
        
        if (count > 1) {
            tempProductName += " 외 " + (count - 1) + "건";
        }
        
        // 익명 내부 클래스(ActionListener)에서 안전하게 사용하기 위해 final로 고정
        final String finalProductName = tempProductName;
        final int totalPrice = cart.getTotalPrice();

        // Step 2: 금액 계산 모델(Payment) 및 결제 UI(PaymentView) 객체 생성
        Payment payment = new Payment(totalPrice);
        PaymentView paymentView = new PaymentView(finalProductName, totalPrice);

        // Step 3: 결제하기 버튼 클릭 이벤트 리스너 연동 (사용자 입력 시점부터 비동기 처리)
        paymentView.addPayButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // 1. 입력 금액 검증 및 데이터 연동
                    String inputStr = paymentView.getInputMoneyText();
                    if (inputStr.trim().isEmpty()) {
                        errorView.showError("투입할 금액을 입력해주세요.");
                        return;
                    }
                    
                    int inputMoney = Integer.parseInt(inputStr.trim());
                    payment.addMoney(inputMoney);

                    // 2. 금액 검증 (투입 금액이 부족하면 InsufficientBalanceException 발생)
                    payment.checkBalance();

                    // 3. 재고 검증 (결제 시도 시점에 재고가 떨어졌다면 OutOfStockException 발생)
                    for (Map.Entry<Product, Integer> entry : items.entrySet()) {
                        if (entry.getKey().getStock() < entry.getValue()) {
                            throw new OutOfStockException("[" + entry.getKey().getName() + "] 상품 재고가 부족합니다.");
                        }
                    }

                    // 4. 결제 성공 처리 (실제 재고 차감 및 관리자 시스템 판매 기록 추가)
                    for (Map.Entry<Product, Integer> entry : items.entrySet()) {
                        Product p = entry.getKey();
                        int qty = entry.getValue();
                        
                        p.reduceStock(qty); 
                        salesManager.addRecord(new SalesRecord(p.getName(), qty)); 
                    }

                    // 5. 거스름돈 계산 및 고유 거래번호(UUID) 기반 영수증 데이터(Transaction) 생성
                    int change = payment.calculateChange();
                    String txId = UUID.randomUUID().toString().substring(0, 8).toUpperCase(); 
                    Transaction tx = new Transaction(txId, finalProductName, totalPrice, change);

                    // 6. 뷰(View) 하단에 최종 영수증 텍스트 출력
                    paymentView.printReceipt(tx.getReceiptText());

                    // 7. 장바구니 초기화 및 메인 화면 새로고침
                    cart.clear();
                    mainView.refresh();
                    
                } catch (NumberFormatException ex) {
                    errorView.showError("숫자만 입력 가능합니다.");
                    payment.refund(); // 에러 시 투입금액 초기화 (안전장치)
                    paymentView.clearInput();
                } catch (InsufficientBalanceException | OutOfStockException ex) {
                    errorView.showError(ex.getMessage());
                    payment.refund(); 
                    paymentView.clearInput();
                } catch (Exception ex) {
                    errorView.showError("결제 처리 중 알 수 없는 오류가 발생했습니다.");
                    payment.refund();
                    paymentView.clearInput();
                }
            }
        });

        // Step 4: 세팅이 완료된 결제 창을 화면에 표시
        paymentView.setVisible(true);
    }
}
