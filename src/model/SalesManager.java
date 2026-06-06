// SalesManager.java 2514747 정유진

package model;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 판매 기록 관리 클래스
 * 판매 기록 저장, 조회, 파일 저장 기능 제공
 * 판매 기록 관리 책임을 전담
 */

public class SalesManager {

    private List<SalesRecord> records;

    public SalesManager() {

        records = new ArrayList<>();
    }

    /** 판매 기록 추가 */
    public void addRecord(
            SalesRecord record) {

        records.add(record);
    }

    /** 전체 판매 기록 반환 */
    public List<SalesRecord> getRecords() {

        return records;
    }

    /** 판매 기록 파일 저장 */
    public void saveToFile() {

        try (BufferedWriter writer =
                     new BufferedWriter(
                             new FileWriter(
                                     "sales_record.txt"))) {

            for (SalesRecord record : records) {

                writer.write(
                        record.getProductName()
                                + ", "
                                + record.getQuantity()
                                + ", "
                                + record.getSaleTime());

                writer.newLine();
            }

        } catch (IOException e) {

            System.out.println(
                    "판매 기록 저장 중 오류 발생");

            e.printStackTrace();
        }
    }
}
