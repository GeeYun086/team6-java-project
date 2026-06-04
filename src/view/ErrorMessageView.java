//ErrorMessageView.java 2516801 현진서
package view;

import javax.swing.JOptionPane;

/**
 * [View] 에러 상황 발생 시 사용자에게 팝업을 띄워주는 공통 컴포넌트입니다.
 */
public class ErrorMessageView {
    public void showError(String message) {
        JOptionPane.showMessageDialog(null, message, "오류", JOptionPane.ERROR_MESSAGE);
    }
}