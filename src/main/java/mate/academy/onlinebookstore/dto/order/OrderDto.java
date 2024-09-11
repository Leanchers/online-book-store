package mate.academy.onlinebookstore.dto.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.Data;
import mate.academy.onlinebookstore.model.Order;

@Data
public class OrderDto {
    private Long id;
    private Long userId;
    private Set<OrderItemDto> books;
    private LocalDateTime orderDate;
    private BigDecimal total;
    private Order.Status status;
}
