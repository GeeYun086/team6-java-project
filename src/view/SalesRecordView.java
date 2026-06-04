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
        setSize(500, 400);

        recordArea = new JTextArea();
        recordArea.setEditable(false);

        displayRecords(records);

        add(
                new JScrollPane(recordArea),
                BorderLayout.CENTER
        );

        setLocationRelativeTo(null);
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
                    record.getProductName()
                            + " / "
                            + record.getQuantity()
                            + "개 / "
                            + record.getSaleTime()
                            + "\n"
            );
        }
    }
}