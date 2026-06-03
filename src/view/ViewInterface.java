// ViewInterface.java 2216899 정지윤
package view;

/**
 * View 클래스들이 반드시 구현해야 하는 인터페이스
 * MainView와 PurchaseView가 구현하며, Controller는 이 인터페이스를 통해 View에 지시만 내리고 구체적인 표시 방식은 View가 결정
 */
public interface ViewInterface {
    void refresh();                  // 화면 데이터 갱신
    void showMessage(String message); // 일반 안내 메시지 표시
    void showAlert(String message);   // 경고 메시지 표시
}
