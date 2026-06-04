// PaymentView.java 2516801 현진서
package view;

import javax.swing.*;
import javax.swing.border.TitledBorder;
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

    
    private final Color bgColor = new Color(24, 30, 49); 
    private final Color panelBgColor = new Color(34, 42, 69); 
    private final Color textColor = Color.WHITE; 
    private final Color accentColor = new Color(255, 215, 0); 
    private final Color buttonColor = new Color(66, 133, 244); 

    // 생성자를 통해 의존성을 주입받아 초기 화면을 동적으로 구성합니다.
    public PaymentView(String productName, int totalAmount) {
        setTitle("자판기 결제 시스템");
        setSize(400, 530); 
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // 메인 화면 종료 방지
        
        // 전체 배경 패널 설정
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(bgColor);
        setContentPane(mainPanel);

        // 1. 최상단 선택 상품 라벨
        productLabel = new JLabel("선택한 상품: " + productName, SwingConstants.CENTER);
        productLabel.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        productLabel.setForeground(textColor);
        productLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // 2. 결제할 금액 라벨
        totalAmountLabel = new JLabel("결제할 금액: " + totalAmount + "원", SwingConstants.CENTER);
        totalAmountLabel.setFont(new Font("맑은 고딕", Font.BOLD, 22));
        totalAmountLabel.setForeground(accentColor); // 강조 색상 적용
        totalAmountLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // 3. 투입할 금액 입력 폼
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBackground(bgColor);
        TitledBorder inputBorder = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY), "투입할 금액(숫자만 입력)");
        inputBorder.setTitleColor(textColor);
        inputPanel.setBorder(inputBorder);
        
        inputMoneyField = new JTextField();
        inputMoneyField.setFont(new Font("맑은 고딕", Font.PLAIN, 18));
        inputMoneyField.setBackground(panelBgColor);
        inputMoneyField.setForeground(textColor);
        inputMoneyField.setCaretColor(textColor); // 커서 색상도 흰색으로
        inputMoneyField.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        inputPanel.add(inputMoneyField, BorderLayout.CENTER);

        // 4. 결제하기 버튼
        payButton = new JButton("결제하기");
        payButton.setFont(new Font("맑은 고딕", Font.BOLD, 18));
        payButton.setBackground(buttonColor);
        payButton.setForeground(Color.WHITE);
        payButton.setFocusPainted(false);
        payButton.setBorderPainted(false); // 테두리 제거로 깔끔하게
        payButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        payButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 5. 하단 영수증 출력 폼
        JPanel receiptPanel = new JPanel(new BorderLayout());
        receiptPanel.setBackground(bgColor);
        TitledBorder receiptBorder = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY), "결제 내역 / 영수증");
        receiptBorder.setTitleColor(textColor);
        receiptPanel.setBorder(receiptBorder);
        
        receiptArea = new JTextArea(10, 20);
        receiptArea.setFont(new Font("Monospaced", Font.PLAIN, 15));
        receiptArea.setBackground(panelBgColor);
        receiptArea.setForeground(textColor);
        receiptArea.setEditable(false); // 사용자가 임의로 영수증을 조작할 수 없도록 방어
        receiptArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JScrollPane scrollPane = new JScrollPane(receiptArea);
        scrollPane.setBorder(null); // 스크롤 안쪽 테두리 제거
        receiptPanel.add(scrollPane, BorderLayout.CENTER);

        // 컴포넌트 조립
        mainPanel.add(productLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10))); 
        mainPanel.add(totalAmountLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(inputPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        mainPanel.add(payButton);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(receiptPanel);

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
