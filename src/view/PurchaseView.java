// PurchaseView.java 2216899 정지윤
package view;

import model.Cart;
import model.Product;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Map;

/**
 * 결제 확인 창
 * 장바구니 목록과 합계를 보여주고 사용자에게 결제 여부를 확인
 * 결제하기 버튼을 누르면 confirmed = true로 설정하고 닫힘
 * Controller가 isConfirmed()로 결과를 확인한 뒤 실제 결제 처리
 */
public class PurchaseView extends JDialog implements ViewInterface {

    private final Cart cart;

    private JPanel  itemsPanel;
    private JLabel  totalLabel;
    private JLabel  resultLabel;
    private JButton confirmBtn;
    private JButton cancelBtn;

    // 사용자가 결제하기를 눌렀는지 Controller가 확인하는 플래그
    private boolean confirmed = false;

    private static final Color C_BG     = new Color(24, 26, 40);
    private static final Color C_CARD   = new Color(40, 44, 68);
    private static final Color C_BORDER = new Color(60, 65, 95);
    private static final Color C_WHITE  = new Color(225, 228, 240);
    private static final Color C_YELLOW = new Color(255, 200, 50);
    private static final Color C_GREEN  = new Color(72, 199, 116);
    private static final Color C_RED    = new Color(240, 80, 80);
    private static final Color C_BLUE   = new Color(60, 130, 220);

    public PurchaseView(JFrame parent, Cart cart) {
        super(parent, "결제 확인", true); // modal: 이 창이 닫힐 때까지 부모 창 조작 불가
        this.cart = cart;
        initDialog();
        buildUI();
        refresh();
    }

    /** 다이얼로그 크기, 위치, 레이아웃 초기 설정 */
    private void initDialog() {
        setSize(360, 460);
        setResizable(false);
        setLocationRelativeTo(getParent());
        getContentPane().setBackground(C_BG);
        setLayout(new BorderLayout(0, 0));
    }

    /** 타이틀, 주문 목록, 버튼 조립 */
    private void buildUI() {
        add(buildHeader(),  BorderLayout.NORTH);
        add(buildCenter(),  BorderLayout.CENTER);
        add(buildButtons(), BorderLayout.SOUTH);
    }

    /** 상단 타이틀 라벨 */
    private JLabel buildHeader() {
        JLabel title = new JLabel("결제 확인", SwingConstants.CENTER);
        title.setFont(new Font("맑은 고딕", Font.BOLD, 18));
        title.setForeground(C_WHITE);
        title.setOpaque(true);
        title.setBackground(new Color(18, 20, 34));
        title.setBorder(new EmptyBorder(18, 0, 18, 0));
        return title;
    }

    /** 주문 목록, 합계, 결과 메시지 영역 */
    private JPanel buildCenter() {
        JPanel center = new JPanel(new BorderLayout(0, 8));
        center.setBackground(C_BG);
        center.setBorder(new EmptyBorder(14, 20, 10, 20));

        JLabel listTitle = new JLabel("주문 내역");
        listTitle.setFont(new Font("맑은 고딕", Font.BOLD, 13));
        listTitle.setForeground(new Color(140, 145, 170));

        itemsPanel = new JPanel();
        itemsPanel.setLayout(new BoxLayout(itemsPanel, BoxLayout.Y_AXIS));
        itemsPanel.setBackground(C_CARD);
        itemsPanel.setBorder(new EmptyBorder(10, 12, 10, 12));

        JScrollPane scroll = new JScrollPane(itemsPanel);
        scroll.setBorder(BorderFactory.createLineBorder(C_BORDER, 1));
        scroll.getViewport().setBackground(C_CARD);

        JPanel bottomInfo = new JPanel(new GridLayout(2, 1, 0, 4));
        bottomInfo.setBackground(C_BG);
        bottomInfo.setBorder(new EmptyBorder(10, 0, 0, 0));

        totalLabel = new JLabel("합계: 0원");
        totalLabel.setFont(new Font("맑은 고딕", Font.BOLD, 17));
        totalLabel.setForeground(C_YELLOW);

        // 결제 처리 후 성공/실패 메시지 표시
        resultLabel = new JLabel(" ");
        resultLabel.setFont(new Font("맑은 고딕", Font.BOLD, 13));
        resultLabel.setForeground(C_GREEN);
        resultLabel.setHorizontalAlignment(SwingConstants.CENTER);

        bottomInfo.add(totalLabel);
        bottomInfo.add(resultLabel);

        center.add(listTitle,  BorderLayout.NORTH);
        center.add(scroll,     BorderLayout.CENTER);
        center.add(bottomInfo, BorderLayout.SOUTH);

        return center;
    }

    /** 결제하기 / 취소 버튼 패널 */
    private JPanel buildButtons() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 10, 0));
        panel.setBackground(C_BG);
        panel.setBorder(new EmptyBorder(6, 20, 20, 20));

        confirmBtn = buildDialogButton("결제하기", C_BLUE);
        cancelBtn  = buildDialogButton("취소",    C_RED);

        // 결제하기: confirmed = true로 설정 후 닫힘, Controller가 이 값을 읽어 결제 처리
        confirmBtn.addActionListener(e -> {
            confirmed = true;
            dispose();
        });
        cancelBtn.addActionListener(e -> {
            confirmed = false;
            dispose();
        });

        panel.add(confirmBtn);
        panel.add(cancelBtn);
        return panel;
    }

    /** 장바구니 내용으로 주문 목록, 합계 갱신 */
    @Override
    public void refresh() {
        itemsPanel.removeAll();
        Map<Product, Integer> items = cart.getItems();
        for (Map.Entry<Product, Integer> entry : items.entrySet()) {
            itemsPanel.add(buildItemRow(entry.getKey(), entry.getValue()));
            itemsPanel.add(Box.createVerticalStrut(5));
        }
        totalLabel.setText("합계: " + String.format("%,d", cart.getTotalPrice()) + "원");
        itemsPanel.revalidate();
        itemsPanel.repaint();
    }

    /** 주문 (상품명 x 수량 / 금액) */
    private JPanel buildItemRow(Product product, int qty) {
        JPanel row = new JPanel(new BorderLayout(8, 0));
        row.setBackground(C_CARD);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));

        JLabel name = new JLabel(product.getName() + " x " + qty);
        name.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
        name.setForeground(C_WHITE);

        JLabel price = new JLabel(String.format("%,d원", product.getPrice() * qty));
        price.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
        price.setForeground(C_YELLOW);

        row.add(name,  BorderLayout.WEST);
        row.add(price, BorderLayout.EAST);
        return row;
    }

    /** 안내 메시지를 초록색으로 표시 */
    @Override
    public void showMessage(String message) {
        resultLabel.setForeground(C_GREEN);
        resultLabel.setText(message);
    }

    /** 경고 메시지를 빨간색으로 표시 */
    @Override
    public void showAlert(String message) {
        resultLabel.setForeground(C_RED);
        resultLabel.setText(message);
    }

    public boolean isConfirmed() { return confirmed; }

    private JButton buildDialogButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("맑은 고딕", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(0, 44));
        return btn;
    }
}
