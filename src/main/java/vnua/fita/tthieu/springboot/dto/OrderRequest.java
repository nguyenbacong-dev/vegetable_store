package vnua.fita.tthieu.springboot.dto;

import java.math.BigDecimal;
import java.util.List;

public class OrderRequest {

    private Long userId; // có thể null nếu khách không đăng nhập
    private String status;
    private BigDecimal total;
    private List<Item> items;

    // class con mô tả từng sản phẩm trong giỏ
    public static class Item {
        private Long productId;
        private int quantity;
        private BigDecimal price;

        // getter, setter
        public Long getProductId() { return productId; }
        public void setProductId(Long productId) { this.productId = productId; }

        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }

        public BigDecimal getPrice() { return price; }
        public void setPrice(BigDecimal price) { this.price = price; }
    }

    // getter, setter
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }

    public List<Item> getItems() { return items; }
    public void setItems(List<Item> items) { this.items = items; }
}
