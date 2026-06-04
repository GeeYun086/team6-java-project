<<<<<<< HEAD
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
=======
// App.java 2216899 정지윤
import controller.PurchaseController;
import model.Cart;
import model.Product;
import view.MainView;
// 관리자 시스템 관련 클래스 추가 2514747 정유진
import controller.AdminController;
import model.InventoryManager;
import model.SalesManager;
import view.AdminView;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

// 프로그램 진입점 (Model, Controller, View를 생성하고 연결)
public class App {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            List<Product> products = createProducts();
            Cart cart = new Cart();

            PurchaseController controller = new PurchaseController(cart);

            // [결제 및 오류 처리 파트 연결]
            // PaymentController를 생성하고 PurchaseController에 전달
            // PaymentController paymentController = new PaymentController(cart, products);
            // controller.setPaymentController(paymentController);

            // [관리자 시스템 파트 연결] 2514747 정유진
            // products 리스트를 그대로 넘겨야 세 파트가 같은 재고 데이터를 공유
            InventoryManager inventoryManager = new InventoryManager(products);
            SalesManager salesManager = new SalesManager();
            AdminController adminController = new AdminController(inventoryManager, salesManager);
               
            MainView mainView = new MainView(controller, products, cart);
            controller.setMainView(mainView);

            // [관리자 시스템 파트 연결] 2514747 정유진
            // MainView에 관리자 버튼을 추가하고 AdminController를 연결
            mainView.setAdminController(adminController);
           
            mainView.refresh();
            mainView.setVisible(true);

        });
    }

    private static List<Product> createProducts() {
        List<Product> list = new ArrayList<>();

        // 음료
        list.add(new Product("P01", "콜라",       1500, "음료", 5));
        list.add(new Product("P06", "커피",        1500, "음료", 4));
        list.add(new Product("P03", "생수",        1000, "음료", 8));
        list.add(new Product("P05", "녹차",        1000, "음료", 4));
        list.add(new Product("P02", "과일주스",    1200, "음료", 3));
        list.add(new Product("P04", "비타민음료",  1800, "음료", 5));

        // 간식
        list.add(new Product("P07", "초코바",      800,  "간식", 6));
        list.add(new Product("P08", "덴탈껌",      900,  "간식", 4));
        list.add(new Product("P09", "젤리",        700,  "간식", 5));
        list.add(new Product("P10", "쿠키",        1200, "간식", 3));
        list.add(new Product("P11", "사탕",        500,  "간식", 8));
        list.add(new Product("P12", "프레첼",      1000, "간식", 4));

        // 생활용품
        list.add(new Product("P13", "마스크",      1500, "생활용품", 10));
        list.add(new Product("P14", "핫팩",        2000, "생활용품", 5));
        list.add(new Product("P15", "손소독제",    2500, "생활용품", 3));
        list.add(new Product("P16", "반창고",      1000, "생활용품", 7));
        list.add(new Product("P17", "가글",        2000, "생활용품", 5));
        list.add(new Product("P18", "휴대용 티슈", 800,  "생활용품", 9));

        return list;
    }
}
>>>>>>> 6fea2e6501972f57a98464cc8fbfb10887253589
