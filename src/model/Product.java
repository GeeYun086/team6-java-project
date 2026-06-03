// Product.java 2216899 정지윤
package model;

/**
 * 자판기 상품 하나를 나타내는 클래스
 * 재고 변경은 내부 메서드로만 가능하고, synchronized로 동시 접근 제한
 */
public class Product {

    private final String id;
    private final String name;
    private final int    price;
    private final String category;
    private int stock;

    public Product(String id, String name, int price, String category, int stock) {
        this.id       = id;
        this.name     = name;
        this.price    = price;
        this.category = category;
        this.stock    = stock;
    }

    /** 재고 차감, 부족하면 false 반환 */
    public synchronized boolean reduceStock(int qty) {
        if (stock < qty) return false;
        stock -= qty;
        return true;
    }

    /** 결제 취소 시 재고 복구 */
    public synchronized void restoreStock(int qty) {
        stock += qty;
    }

    /** 재고 1개 이상이면 true */
    public synchronized boolean isAvailable() {
        return stock > 0;
    }

    public String getId()       { return id; }
    public String getName()     { return name; }
    public int    getPrice()    { return price; }
    public String getCategory() { return category; }
    public synchronized int getStock() { return stock; }

    @Override
    public String toString() {
        return name + " (" + String.format("%,d", price) + "원)";
    }
}
