//PaymentView.java 2516801 현진서
package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * [View] 사용자가 결제 금액을 입력하고 결과를 확인하는 화면 컴포넌트입니다.
 * 비즈니스 로직(연산, 데이터 수정 등)을 전혀 포함하지 않으며 오로지 '화면 출력'만 담당합니다.
 */
public class PaymentView extends JFrame {
    private JLabel productLabel;      
    private JLabel totalAmountLabel;
    private JTextField inputMoneyField;
    private JButton payButton;
    private JTextArea receiptArea;

    // 생성자를 통해 의존성을 주입받아 초기 화면을 동적으로 구성합니다.
    public PaymentView(String productName, int totalAmount) {
        setTitle("자판기 결제 시스템");
        setSize(400, 530); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        ((JPanel)getContentPane()).setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // 1. 최상단 선택 상품 라벨
        productLabel = new JLabel("선택한 상품: " + productName, SwingConstants.CENTER);
        productLabel.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        productLabel.setForeground(Color.DARK_GRAY);
        productLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // 2. 결제할 금액 라벨
        totalAmountLabel = new JLabel("결제할 금액: " + totalAmount + "원", SwingConstants.CENTER);
        totalAmountLabel.setFont(new Font("맑은 고딕", Font.BOLD, 22));
        totalAmountLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // 3. 투입할 금액 입력 폼
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY), "투입할 금액(숫자만 입력)"));
        inputMoneyField = new JTextField();
        inputMoneyField.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        inputPanel.add(inputMoneyField, BorderLayout.CENTER);

        // 4. 결제하기 버튼
        payButton = new JButton("결제하기");
        payButton.setFont(new Font("맑은 고딕", Font.BOLD, 18));
        payButton.setBackground(new Color(70, 130, 180));
        payButton.setForeground(Color.WHITE);
        payButton.setFocusPainted(false);
        payButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        // 5. 하단 영수증 출력 폼
        JPanel receiptPanel = new JPanel(new BorderLayout());
        receiptPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY), "결제 내역 / 영수증"));
        receiptArea = new JTextArea(10, 20);
        receiptArea.setFont(new Font("Monospaced", Font.PLAIN, 16));
        receiptArea.setEditable(false); // 사용자가 임의로 영수증을 조작할 수 없도록 방어
        receiptPanel.add(new JScrollPane(receiptArea), BorderLayout.CENTER);

        // 컴포넌트 조립
        add(productLabel);
        add(Box.createRigidArea(new Dimension(0, 5))); 
        add(totalAmountLabel);
        add(Box.createRigidArea(new Dimension(0, 15)));
        add(inputPanel);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(payButton);
        add(Box.createRigidArea(new Dimension(0, 15)));
        add(receiptPanel);

        setLocationRelativeTo(null);
    }

    // Controller가 View의 값을 읽고 쓸 수 있도록 열어둔 메서드들
    public String getInputMoneyText() { return inputMoneyField.getText(); }
    public void clearInput() { inputMoneyField.setText(""); }
    public void printReceipt(String text) { receiptArea.setText(text); }
    
    // 버튼 이벤트를 Controller가 통제할 수 있도록 리스너 연결 메서드 제공
    public void addPayButtonListener(ActionListener listener) { 
        payButton.addActionListener(listener); 
    }
}