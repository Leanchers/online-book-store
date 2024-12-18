package lada.alex.onlinebookstore.service.order.impl;

import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lada.alex.onlinebookstore.dto.order.CreateOrderRequestDto;
import lada.alex.onlinebookstore.dto.order.OrderDto;
import lada.alex.onlinebookstore.dto.order.OrderItemDto;
import lada.alex.onlinebookstore.dto.order.UpdateOrderRequestDto;
import lada.alex.onlinebookstore.exception.EntityNotFoundException;
import lada.alex.onlinebookstore.exception.OrderProcessingException;
import lada.alex.onlinebookstore.mapper.OrderItemMapper;
import lada.alex.onlinebookstore.mapper.OrderMapper;
import lada.alex.onlinebookstore.model.Order;
import lada.alex.onlinebookstore.model.ShoppingCart;
import lada.alex.onlinebookstore.repository.order.OrderItemRepository;
import lada.alex.onlinebookstore.repository.order.OrderRepository;
import lada.alex.onlinebookstore.repository.shoppingcart.ShoppingCartRepository;
import lada.alex.onlinebookstore.service.order.OrderService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final OrderRepository orderRepository;
    private final OrderItemMapper orderItemMapper;
    private final OrderItemRepository orderItemRepository;
    private final OrderMapper orderMapper;

    @Transactional
    @Override
    public OrderDto createOrder(CreateOrderRequestDto requestDto, Long userId) {
        ShoppingCart shoppingCart = findShoppingCartByUserId(userId);
        if (shoppingCart.getCartItems().isEmpty()) {
            throw new OrderProcessingException("Shopping cart is empty");
        }
        Order order = orderMapper.toModel(requestDto, shoppingCart);
        orderRepository.save(order);
        orderItemRepository.saveAll(order.getOrderItems());
        shoppingCart.getCartItems().clear();
        shoppingCartRepository.save(shoppingCart);
        return orderMapper.toDto(order);
    }

    @Override
    public List<OrderDto> getOrders(Pageable pageable, Long userId) {
        return orderRepository.findAllByUserId(pageable, userId).stream()
            .map(orderMapper::toDto)
            .toList();
    }

    @Override
    public OrderDto updateStatus(UpdateOrderRequestDto requestDto, Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(()
                -> new OrderProcessingException("Order not found"));
        order.setStatus(requestDto.status());
        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public List<OrderItemDto> getAllOrderItems(Long orderId, Long userId) {
        return orderRepository.findByIdAndUserId(orderId, userId).orElseThrow(()
                -> new OrderProcessingException("Order not found")).getOrderItems().stream()
            .map(orderItemMapper::toDto)
            .toList();
    }

    @Override
    public OrderItemDto getOrderItem(Long orderItemId, Long orderId, Long userId) {
        return orderItemMapper.toDto(orderItemRepository
            .findByIdAndOrderIdAndOrderUserId(orderItemId, orderId, userId)
            .orElseThrow(() -> new OrderProcessingException("Order's item not found")));
    }

    private ShoppingCart findShoppingCartByUserId(Long userId) {
        return shoppingCartRepository.findByUserId(userId).orElseThrow(
            () -> new EntityNotFoundException("Can't find shopping cart by userId: " + userId)
        );
    }
}
