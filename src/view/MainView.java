// MainView.java 2216899 정지윤
package view;

import controller.PurchaseController;
import model.Cart;
import model.Product;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 자판기 메인 화면
 * 상단 탭으로 카테고리를 전환하고, 상품 카드에서 장바구니에 담은 뒤 오른쪽 패널에서 결제 요청
 * 버튼 이벤트는 모두 PurchaseController에 위임하고, View는 출력만 담당
 */
public class MainView extends JFrame implements ViewInterface {

    private final PurchaseController controller;
    private final List<Product> products;
    private final Cart cart;

    // 갱신이 필요한 UI 컴포넌트
    private JPanel    productGridPanel;  // 상품 카드 그리드 영역
    private JButton[] tabButtons;        // 카테고리 탭 버튼 배열
    private JPanel    cartItemPanel;     // 장바구니 목록
    private JLabel    totalLabel;        // 합계 금액 표시

    // 상품 ID → 아이콘 이미지
    private final Map<String, ImageIcon> productIcons = new HashMap<>();

    // 현재 선택된 카테고리
    private String selectedCategory = CATEGORY_ORDER[0];

    // 카테고리 표시 순서
    private static final String[] CATEGORY_ORDER = { "음료", "간식", "생활용품" };

    // 색상 팔레트
    private static final Color C_BG       = new Color(24, 26, 40);
    private static final Color C_PANEL    = new Color(32, 35, 55);
    private static final Color C_CARD     = new Color(40, 44, 68);
    private static final Color C_BORDER   = new Color(60, 65, 95);
    private static final Color C_WHITE    = new Color(225, 228, 240);
    private static final Color C_YELLOW   = new Color(255, 200, 50);
    private static final Color C_GREEN    = new Color(72, 199, 116);
    private static final Color C_RED      = new Color(240, 80, 80);
    private static final Color C_BLUE     = new Color(60, 130, 220);
    private static final Color C_BLUE_H   = new Color(80, 155, 255);
    private static final Color C_GRAY     = new Color(90, 95, 115);
    private static final Color C_TABBAR   = new Color(18, 20, 34);
    private static final Color C_TAB_OFF  = new Color(140, 145, 170); // 비선택 탭 글자색

    // 카테고리별 헤더 색상
    private static final Color C_CAT_DRINK = new Color(35, 75, 130);
    private static final Color C_CAT_SNACK = new Color(110, 65, 25);
    private static final Color C_CAT_DAILY = new Color(35, 90, 60);

    public MainView(PurchaseController controller, List<Product> products, Cart cart) {
        this.controller = controller;
        this.products   = products;
        this.cart       = cart;
        loadProductIcons();
        initFrame();
        buildUI();
    }

    /** resources의 PNG 아이콘을 56x56으로 로드해 productIcons에 저장 */
    private void loadProductIcons() {
        for (Product p : products) {
            // 파일명 형식: P01_콜라.png (공백은 제거)
            String safeName = p.getName().replace(" ", "");
            String filename  = "/resources/" + p.getId() + "_" + safeName + ".png";
            URL url = getClass().getResource(filename);
            if (url != null) {
                Image scaled = new ImageIcon(url).getImage()
                        .getScaledInstance(56, 56, Image.SCALE_SMOOTH);
                productIcons.put(p.getId(), new ImageIcon(scaled));
            }
        }
    }

    /** 프레임 크기, 위치, 기본 설정 초기화 */
    private void initFrame() {
        setTitle("스마트 자판기");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 660);
        setMinimumSize(new Dimension(860, 560));
        setLocationRelativeTo(null);
        getContentPane().setBackground(C_BG);
    }

    /** 타이틀바, 상품 영역, 장바구니 패널 배치 */
    private void buildUI() {
        setLayout(new BorderLayout());
        add(buildTitleBar(),    BorderLayout.NORTH);
        add(buildProductArea(), BorderLayout.CENTER);
        add(buildCartPanel(),   BorderLayout.EAST);
    }

    /** 상단 타이틀 + 안내 문구 바 */
    private JPanel buildTitleBar() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(18, 20, 34));
        panel.setBorder(new EmptyBorder(14, 24, 14, 24));

        JLabel title = new JLabel("스마트 자판기");
        title.setFont(new Font("맑은 고딕", Font.BOLD, 22));
        title.setForeground(C_WHITE);

        JLabel subtitle = new JLabel("원하는 상품을 고르고 장바구니에 담으세요");
        subtitle.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
        subtitle.setForeground(new Color(140, 145, 170));

        JPanel text = new JPanel(new GridLayout(2, 1, 0, 3));
        text.setOpaque(false);
        text.add(title);
        text.add(subtitle);

        panel.add(text, BorderLayout.WEST);
        return panel;
    }

    /** 탭바 + 상품 그리드 영역 */
    private JPanel buildProductArea() {
        JPanel area = new JPanel(new BorderLayout());
        area.setBackground(C_BG);
        area.add(buildTabBar(),   BorderLayout.NORTH);
        area.add(buildGridArea(), BorderLayout.CENTER);
        return area;
    }

    /** 카테고리 탭 버튼 생성 및 클릭 이벤트 연결 */
    private JPanel buildTabBar() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        bar.setBackground(C_TABBAR);
        bar.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, C_BORDER));

        tabButtons = new JButton[CATEGORY_ORDER.length];
        for (int i = 0; i < CATEGORY_ORDER.length; i++) {
            String cat = CATEGORY_ORDER[i];
            JButton tab = new JButton(cat);
            tab.setFont(new Font("맑은 고딕", Font.BOLD, 14));
            tab.setPreferredSize(new Dimension(130, 44));
            tab.setFocusPainted(false);
            tab.setBorderPainted(false);
            tab.setCursor(new Cursor(Cursor.HAND_CURSOR));

            tab.addActionListener(e -> {
                selectedCategory = cat;
                updateTabStyles();
                refreshProductArea();
            });

            tabButtons[i] = tab;
            bar.add(tab);
        }

        updateTabStyles();
        return bar;
    }

    // 선택된 탭은 카테고리 색상으로, 나머지는 어둡게 표시
    private void updateTabStyles() {
        for (int i = 0; i < CATEGORY_ORDER.length; i++) {
            JButton tab = tabButtons[i];
            if (CATEGORY_ORDER[i].equals(selectedCategory)) {
                tab.setBackground(getCategoryColor(CATEGORY_ORDER[i]));
                tab.setForeground(Color.WHITE);
            } else {
                tab.setBackground(C_TABBAR);
                tab.setForeground(C_TAB_OFF);
            }
        }
    }

    /** 상품 그리드 컨테이너 (실제 카드는 refreshProductArea()에서 채움) */
    private JPanel buildGridArea() {
        productGridPanel = new JPanel(new BorderLayout());
        productGridPanel.setBackground(C_BG);
        return productGridPanel;
    }

    @Override
    public void refresh() {
        updateTabStyles();
        refreshProductArea();
        refreshCartItems();
        totalLabel.setText("합계: " + String.format("%,d", cart.getTotalPrice()) + "원");
    }

    @Override
    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "안내", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void showAlert(String message) {
        JOptionPane.showMessageDialog(this, message, "알림", JOptionPane.WARNING_MESSAGE);
    }

    /** 선택 카테고리 상품만 3열 그리드로 갱신 */
    private void refreshProductArea() {
        List<Product> items = new ArrayList<>();
        for (Product p : products) {
            if (p.getCategory().equals(selectedCategory)) {
                items.add(p);
            }
        }

        // rows=0으로 두고 Java가 행 수를 자동으로 계산하면서 열을 3으로 고정
        JPanel grid = new JPanel(new GridLayout(0, 3, 12, 12));
        grid.setBackground(C_BG);
        grid.setBorder(new EmptyBorder(16, 16, 16, 8));
        for (Product p : items) {
            grid.add(buildProductCard(p));
        }

        productGridPanel.removeAll();
        productGridPanel.add(grid, BorderLayout.CENTER);
        productGridPanel.revalidate();
        productGridPanel.repaint();
    }

    /** 상품 카드 (아이콘, 이름, 가격, 재고, 담기 버튼) */
    private JPanel buildProductCard(Product product) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(C_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(C_BORDER, 1, true),
            new EmptyBorder(14, 12, 14, 12)
        ));

        // 상품 아이콘 (resources 폴더에서 로드)
        ImageIcon icon = productIcons.get(product.getId());
        if (icon != null) {
            JLabel iconLabel = new JLabel(icon);
            iconLabel.setAlignmentX(CENTER_ALIGNMENT);
            card.add(iconLabel);
            card.add(Box.createVerticalStrut(8));
        }

        JLabel nameLabel = new JLabel(product.getName());
        nameLabel.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        nameLabel.setForeground(C_WHITE);
        nameLabel.setAlignmentX(CENTER_ALIGNMENT);

        JLabel priceLabel = new JLabel(String.format("%,d원", product.getPrice()));
        priceLabel.setFont(new Font("맑은 고딕", Font.BOLD, 15));
        priceLabel.setForeground(C_YELLOW);
        priceLabel.setAlignmentX(CENTER_ALIGNMENT);

        JLabel stockLabel;
        if (product.isAvailable()) {
            stockLabel = new JLabel("재고 " + product.getStock() + "개");
            stockLabel.setForeground(C_GREEN);
        } else {
            stockLabel = new JLabel("품절");
            stockLabel.setForeground(C_RED);
        }
        stockLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
        stockLabel.setAlignmentX(CENTER_ALIGNMENT);

        JButton addBtn = buildAddButton(product);

        card.add(Box.createVerticalGlue());
        card.add(nameLabel);
        card.add(Box.createVerticalStrut(6));
        card.add(priceLabel);
        card.add(Box.createVerticalStrut(5));
        card.add(stockLabel);
        card.add(Box.createVerticalStrut(14));
        card.add(addBtn);
        card.add(Box.createVerticalGlue());

        return card;
    }

    /** 재고 있으면 담기 버튼, 없으면 품절 버튼 */
    private JButton buildAddButton(Product product) {
        JButton btn = new JButton();
        btn.setAlignmentX(CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(120, 38));
        btn.setPreferredSize(new Dimension(120, 38));
        btn.setFont(new Font("맑은 고딕", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        if (product.isAvailable()) {
            btn.setText("+ 담기");
            btn.setBackground(C_BLUE);
            btn.setForeground(Color.WHITE);

            btn.addMouseListener(new MouseAdapter() {
                @Override public void mouseEntered(MouseEvent e) { btn.setBackground(C_BLUE_H); }
                @Override public void mouseExited(MouseEvent e)  { btn.setBackground(C_BLUE);   }
            });

            btn.addActionListener(e -> controller.addToCart(product));

        } else {
            btn.setText("품절");
            btn.setBackground(C_GRAY);
            btn.setForeground(new Color(150, 155, 170));
            btn.setEnabled(false);
        }

        return btn;
    }

    /** 장바구니 패널 (아이템 목록, 합계, 결제/취소) */
    private JPanel buildCartPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 0));
        panel.setBackground(C_PANEL);
        panel.setPreferredSize(new Dimension(230, 0));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 1, 0, 0, C_BORDER),
            new EmptyBorder(18, 14, 18, 14)
        ));

        JLabel title = new JLabel("장바구니");
        title.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        title.setForeground(C_WHITE);
        title.setBorder(new EmptyBorder(0, 0, 12, 0));

        cartItemPanel = new JPanel();
        cartItemPanel.setLayout(new BoxLayout(cartItemPanel, BoxLayout.Y_AXIS));
        cartItemPanel.setBackground(C_PANEL);

        JScrollPane scroll = new JScrollPane(cartItemPanel);
        scroll.setBackground(C_PANEL);
        scroll.getViewport().setBackground(C_PANEL);
        scroll.setBorder(null);

        JPanel bottom = new JPanel();
        bottom.setLayout(new BoxLayout(bottom, BoxLayout.Y_AXIS));
        bottom.setBackground(C_PANEL);

        JSeparator sep = new JSeparator(SwingConstants.HORIZONTAL);
        sep.setForeground(C_BORDER);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));

        totalLabel = new JLabel("합계: 0원");
        totalLabel.setFont(new Font("맑은 고딕", Font.BOLD, 17));
        totalLabel.setForeground(C_YELLOW);
        totalLabel.setBorder(new EmptyBorder(10, 0, 10, 0));

        JButton purchaseBtn = buildActionButton("결제하기", C_BLUE);
        JButton cancelBtn   = buildActionButton("전체 취소", C_RED);

        purchaseBtn.addActionListener(e -> controller.requestPurchase());
        cancelBtn.addActionListener(e -> controller.cancelCart());

        bottom.add(sep);
        bottom.add(totalLabel);
        bottom.add(purchaseBtn);
        bottom.add(Box.createVerticalStrut(6));
        bottom.add(cancelBtn);

        panel.add(title,  BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        panel.add(bottom, BorderLayout.SOUTH);

        return panel;
    }

    /** 장바구니 아이템 목록 갱신 */
    private void refreshCartItems() {
        cartItemPanel.removeAll();
        Map<Product, Integer> items = cart.getItems();

        if (items.isEmpty()) {
            JLabel empty = new JLabel("담긴 상품이 없습니다");
            empty.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
            empty.setForeground(new Color(120, 125, 150));
            cartItemPanel.add(empty);
        } else {
            for (Map.Entry<Product, Integer> entry : items.entrySet()) {
                cartItemPanel.add(buildCartRow(entry.getKey(), entry.getValue()));
                cartItemPanel.add(Box.createVerticalStrut(6));
            }
        }

        cartItemPanel.revalidate();
        cartItemPanel.repaint();
    }

    /** 장바구니 상품명, 금액, 수량 조절 버튼 */
    private JPanel buildCartRow(Product product, int qty) {
        JPanel row = new JPanel(new BorderLayout(4, 0));
        row.setBackground(C_PANEL);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 52));

        JLabel nameLabel  = new JLabel(product.getName());
        nameLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
        nameLabel.setForeground(C_WHITE);

        JLabel priceLabel = new JLabel(String.format("%,d원", product.getPrice() * qty));
        priceLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 11));
        priceLabel.setForeground(C_YELLOW);

        JPanel info = new JPanel(new GridLayout(2, 1));
        info.setBackground(C_PANEL);
        info.add(nameLabel);
        info.add(priceLabel);

        JButton minus  = buildQtyButton("-");
        JLabel  qtyLbl = new JLabel(String.valueOf(qty), SwingConstants.CENTER);
        qtyLbl.setFont(new Font("맑은 고딕", Font.BOLD, 13));
        qtyLbl.setForeground(C_WHITE);
        qtyLbl.setPreferredSize(new Dimension(22, 24));
        JButton plus   = buildQtyButton("+");

        minus.addActionListener(e -> controller.decreaseFromCart(product));
        plus.addActionListener(e -> controller.addToCart(product));

        JPanel qtyPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 2, 4));
        qtyPanel.setBackground(C_PANEL);
        qtyPanel.add(minus);
        qtyPanel.add(qtyLbl);
        qtyPanel.add(plus);

        row.add(info,     BorderLayout.CENTER);
        row.add(qtyPanel, BorderLayout.EAST);
        return row;
    }

    /** 주요 액션 버튼 (결제하기, 전체취소 등) */
    private JButton buildActionButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("맑은 고딕", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setAlignmentX(LEFT_ALIGNMENT);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        return btn;
    }

    /** 수량 조절 +,- 버튼 */
    private JButton buildQtyButton(String text) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(34, 26));
        btn.setMinimumSize(new Dimension(34, 26));
        btn.setBackground(C_BORDER);
        btn.setForeground(C_WHITE);
        btn.setFont(new Font("맑은 고딕", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setMargin(new Insets(0, 0, 0, 0));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    /** 카테고리별 탭 배경색 반환 */
    private Color getCategoryColor(String category) {
        switch (category) {
            case "음료":     return C_CAT_DRINK;
            case "간식":     return C_CAT_SNACK;
            case "생활용품": return C_CAT_DAILY;
            default:         return C_BORDER;
        }
    }
}
