// AdminView.java 2514747 정유진

package view;

import controller.AdminController;
import model.Product;
import model.SalesRecord;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.List;

/**
 * 관리자 화면
 * 재고 조회, 품절 상품 조회,
 * 재고 보충, 판매 기록 조회 기능 제공
 */
public class AdminView extends JFrame {

    private AdminController controller;
    private JTextArea displayArea;

    // 메인 테마 색상 설정 (디자인 추가)
    private final Color bgColor = new Color(24, 30, 49);
    private final Color panelBgColor = new Color(34, 42, 69);
    private final Color textColor = Color.WHITE;
    private final Color buttonColor = new Color(66, 133, 244);

    public AdminView(
            AdminController controller) {

        this.controller = controller;

        setTitle("관리자 시스템");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // 관리자창만 닫히도록 수정

        // 전체 배경 패널 (디자인 추가)
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(bgColor);
        setContentPane(mainPanel);

        displayArea = new JTextArea();
        displayArea.setEditable(false);
        // JTextArea 디자인 적용
        displayArea.setFont(new Font("Monospaced", Font.PLAIN, 15));
        displayArea.setBackground(panelBgColor);
        displayArea.setForeground(textColor);
        displayArea.setCaretColor(textColor);

        // 스크롤 패널에 테두리 디자인 적용
        JPanel displayPanel = new JPanel(new BorderLayout());
        displayPanel.setBackground(bgColor);
        TitledBorder displayBorder = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY), "시스템 상태 및 기록 출력");
        displayBorder.setTitleColor(textColor);
        displayPanel.setBorder(displayBorder);
        
        JScrollPane scrollPane = new JScrollPane(displayArea);
        scrollPane.setBorder(null);
        displayPanel.add(scrollPane, BorderLayout.CENTER);

        // 버튼 디자인 적용을 위해 createStyledButton 메서드 사용
        JButton inventoryButton =
                createStyledButton("전체 재고 조회");

        JButton outOfStockButton =
                createStyledButton("품절 상품 조회");

        JButton restockButton =
                createStyledButton("재고 보충");

        JButton salesButton =
                createStyledButton("판매 기록 조회");

        JPanel buttonPanel =
                new JPanel();
        buttonPanel.setBackground(bgColor); // 패널 배경색 적용

        buttonPanel.add(inventoryButton);
        buttonPanel.add(outOfStockButton);
        buttonPanel.add(restockButton);
        buttonPanel.add(salesButton);

        mainPanel.add(buttonPanel, BorderLayout.NORTH);

        mainPanel.add(
                displayPanel,
                BorderLayout.CENTER);

        // 전체 재고 조회
        inventoryButton.addActionListener(
                e -> showAllProducts());

        // 품절 상품 조회
        outOfStockButton.addActionListener(
                e -> showOutOfStockProducts());

        // 재고 보충
        restockButton.addActionListener(
                e -> restockProduct());

        // 판매 기록 조회
        salesButton.addActionListener(
                e -> showSalesRecords());

        setLocationRelativeTo(null);
    }

    // 버튼 디자인 통일용 헬퍼 메서드 (디자인 추가)
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("맑은 고딕", Font.BOLD, 14));
        button.setBackground(buttonColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        return button;
    }

    /** 전체 상품 출력 */
    private void showAllProducts() {

        displayArea.setText("");

        List<Product> products =
                controller.getProducts();

        for (Product p : products) {

            displayArea.append(
                    p.getId()
                            + " / "
                            + p.getName()
                            + " / "
                            + p.getPrice()
                            + "원 / 재고 "
                            + p.getStock()
                            + "개\n");
        }
    }

    /** 품절 상품 출력 */
    private void showOutOfStockProducts() {

        displayArea.setText("");

        List<Product> products =
                controller.getOutOfStockProducts();

        if (products.isEmpty()) {

            displayArea.append(
                    "품절 상품이 없습니다.\n");

            return;
        }

        for (Product p : products) {

            displayArea.append(
                    p.getName()
                            + " (품절)\n");
        }
    }

    /** 재고 보충 */
    private void restockProduct() {

        String productId =
                JOptionPane.showInputDialog(
                        this,
                        "재고를 보충할 상품 ID를 입력하세요.");

        if (productId == null ||
                productId.trim().isEmpty()) {

            return;
        }

        String amountText =
                JOptionPane.showInputDialog(
                        this,
                        "보충할 수량을 입력하세요.");

        if (amountText == null ||
                amountText.trim().isEmpty()) {

            return;
        }

        try {

            int amount =
                    Integer.parseInt(amountText);

            controller.restockProduct(
                    productId,
                    amount);

            JOptionPane.showMessageDialog(
                    this,
                    "재고 보충 완료");

            // 보충 후 전체 재고 다시 표시
            showAllProducts();

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "유효한 숫자를 입력하세요.");
        }
    }

    /** 판매 기록 조회 */
    private void showSalesRecords() {

        List<SalesRecord> records =
                controller.getSalesRecords();

        SalesRecordView view =
                new SalesRecordView(records);

        view.setVisible(true);
    }
}