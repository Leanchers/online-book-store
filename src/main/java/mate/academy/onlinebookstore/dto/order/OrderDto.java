package mate.academy.onlinebookstore.dto.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.Data;
import mate.academy.onlinebookstore.dto.book.BookDto;
import mate.academy.onlinebookstore.model.Order;

@Data
public class OrderDto {
    long id;
    Long userId;
    Set<OrderItemDto> books;
    LocalDateTime orderDate;
    BigDecimal total;
    Order.Status status;
}
