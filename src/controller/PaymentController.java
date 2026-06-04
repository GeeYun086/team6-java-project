//PaymentController.java 2516801 현진서
package controller;

// Model 패키지의 파일들 import
import model.InsufficientBalanceException;
import model.OutOfStockException;
import model.Inventory;
import model.Payment;
import model.Transaction;

// View 패키지의 파일들 import
import view.ErrorMessageView;
import view.PaymentView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.UUID;

/**
 * [Controller] View(화면)와 Model(데이터) 사이를 중재하는 시스템의 지휘자입니다.
 * 사용자의 액션을 감지하여 적절한 Model 로직을 호출하고, 그 결과로 View를 업데이트합니다.
 */
public class PaymentController {
    private Payment paymentModel;
    private Inventory inventoryModel;
    private PaymentView paymentView;
    private ErrorMessageView errorView;
    private String productName;

    // 의존성 주입(Dependency Injection)을 통해 결합도를 낮추고 테스트 유연성을 확보합니다.
    public PaymentController(Payment paymentModel, Inventory inventoryModel, PaymentView paymentView, String productName) {
        this.paymentModel = paymentModel;
        this.inventoryModel = inventoryModel;
        this.paymentView = paymentView;
        this.productName = productName;
        this.errorView = new ErrorMessageView();

        // View의 버튼에 리스너를 달아 사용자의 액션을 감지합니다.
        this.paymentView.addPayButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                processPayment();
            }
        });
    }

    /**
     * 결제 요청을 처리하는 메인 비즈니스 로직 파이프라인입니다.
     */
    private void processPayment() {
        try {
            // 1. 사용자 입력값 가져오기 (문자가 들어오면 NumberFormatException 발생)
            int money = Integer.parseInt(paymentView.getInputMoneyText());
            paymentModel.addMoney(money);

            // 2. [핵심: 동기화(Synchronization) 블록 적용]
            // 여러 스레드가 동시에 결제를 시도할 때 Inventory 모델의 상태가 꼬이는 것을 방어합니다.
            synchronized (inventoryModel) {
                // 재고 확인 로직 수행
                inventoryModel.checkStock();
                
                // 잔액 확인 로직 수행
                paymentModel.checkBalance();

                // 3. 검증 성공 시: 재고 차감 및 거스름돈 계산 진행
                inventoryModel.decreaseStock();
                int change = paymentModel.calculateChange();

                // 4. 결제 완료 데이터(영수증) 생성 및 화면 반영
                String txId = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
                Transaction tx = new Transaction(txId, productName, paymentModel.getTotalAmount(), change);
                
                paymentView.printReceipt(tx.getReceiptText()); // 영수증 출력
            }

        } catch (NumberFormatException ex) {
            // 기본 예외: 입력란에 문자를 입력한 경우를 방어
            errorView.showError("금액은 숫자로만 입력해주세요.");
            paymentModel.refund();
        } catch (InsufficientBalanceException | OutOfStockException ex) {
            // 사용자 정의 예외 복구: 프로그램 강제 종료 대신 팝업 띄우고 상태 초기화
            errorView.showError(ex.getMessage());
            paymentModel.refund();
            paymentView.clearInput();
        } catch (Exception ex) {
            // 그 외 예측 불가능한 시스템 예외 안전 방어
            errorView.showError("시스템 오류가 발생했습니다.");
            paymentModel.refund();
        }
    }
}