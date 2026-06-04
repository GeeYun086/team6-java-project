import model.Inventory;
import model.Payment;
import view.PaymentView;
import controller.PaymentController;

public class App {
    public static void main(String[] args) {
        // 1. 데이터와 비즈니스 로직을 담당하는 Model 생성
        // (예시로 자판기 상품 가격을 1500원으로 설정)
        int productPrice = 1500;
        String productName = "콜라";
        
        Inventory inventoryModel = new Inventory();
        Payment paymentModel = new Payment(productPrice);

        // 2. 화면 출력을 담당하는 View 생성 (화면에 보여줄 상품명과 가격 전달)
        PaymentView paymentView = new PaymentView(productName, productPrice);

        // 3. 이들을 중재하고 통제할 지휘자 Controller 생성
        // 생성자를 통해 Model과 View를 주입(Injection)하여 서로 연결해 줍니다.
        PaymentController paymentController = new PaymentController(
            paymentModel, 
            inventoryModel, 
            paymentView, 
            productName
        );

        // 4. 자판기 결제 화면창을 모니터에 띄웁니다.
        paymentView.setVisible(true);
    }
}