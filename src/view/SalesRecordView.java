// SalesRecordView.java 2514747 정유진

package view;

import model.SalesRecord;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * 판매 기록 조회 화면
 * 판매된 상품 내역을 관리자에게 표시
 */
public class SalesRecordView extends JFrame {

    private JTextArea recordArea;

    public SalesRecordView(
            List<SalesRecord> records) {

        setTitle("판매 기록 조회");
        setSize(650, 450);
        setLocationRelativeTo(null);

        Color bgColor = new Color(24, 26, 40);
        Color panelColor = new Color(32, 35, 55);
        Color textColor = new Color(225, 228, 240);

        getContentPane().setBackground(bgColor);
        recordArea = new JTextArea();
        recordArea.setEditable(false);
        recordArea.setFont(new Font("맑은 고딕",Font.PLAIN, 14));
        recordArea.setBackground(panelColor);
        recordArea.setForeground(textColor);

        displayRecords(records);
        JScrollPane scrollPane = new JScrollPane(recordArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(scrollPane, BorderLayout.CENTER);
    }

    /** 판매 기록 출력 */
    private void displayRecords(
            List<SalesRecord> records) {

        recordArea.setText("");

        if (records.isEmpty()) {

            recordArea.append(
                    "판매 기록이 없습니다.\n");

            return;
        }

        for (SalesRecord record : records) {

            recordArea.append(
                record.toString()
                        + "\n");
        }
    }
}
