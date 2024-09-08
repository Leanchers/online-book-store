package mate.academy.onlinebookstore.service.order;

import java.util.List;
import mate.academy.onlinebookstore.dto.order.CreateOrderRequestDto;
import mate.academy.onlinebookstore.dto.order.OrderDto;
import mate.academy.onlinebookstore.dto.order.OrderItemDto;
import mate.academy.onlinebookstore.dto.order.UpdateOrderRequestDto;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    OrderDto createOrder(CreateOrderRequestDto requestDto, Long userId);

    List<OrderDto> getOrders(Pageable pageable, Long userId);

    OrderDto updateStatus(UpdateOrderRequestDto requestDto, Long orderId);

    List<OrderItemDto> getAllOrderItems(Long orderId, Long userId);

    OrderItemDto getOrderItem(Long orderItemId, Long orderId, Long userId);
}
