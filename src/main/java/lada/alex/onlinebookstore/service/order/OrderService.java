package lada.alex.onlinebookstore.service.order;

import java.util.List;
import lada.alex.onlinebookstore.dto.order.CreateOrderRequestDto;
import lada.alex.onlinebookstore.dto.order.OrderDto;
import lada.alex.onlinebookstore.dto.order.OrderItemDto;
import lada.alex.onlinebookstore.dto.order.UpdateOrderRequestDto;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    OrderDto createOrder(CreateOrderRequestDto requestDto, Long userId);

    List<OrderDto> getOrders(Pageable pageable, Long userId);

    OrderDto updateStatus(UpdateOrderRequestDto requestDto, Long orderId);

    List<OrderItemDto> getAllOrderItems(Long orderId, Long userId);

    OrderItemDto getOrderItem(Long orderItemId, Long orderId, Long userId);
}
