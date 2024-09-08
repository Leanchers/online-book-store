package mate.academy.onlinebookstore.service.order.impl;

import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import mate.academy.onlinebookstore.dto.order.CreateOrderRequestDto;
import mate.academy.onlinebookstore.dto.order.OrderDto;
import mate.academy.onlinebookstore.dto.order.OrderItemDto;
import mate.academy.onlinebookstore.dto.order.UpdateOrderRequestDto;
import mate.academy.onlinebookstore.exception.EntityNotFoundException;
import mate.academy.onlinebookstore.mapper.OrderItemMapper;
import mate.academy.onlinebookstore.mapper.OrderMapper;
import mate.academy.onlinebookstore.model.Order;
import mate.academy.onlinebookstore.model.Order.Status;
import mate.academy.onlinebookstore.model.OrderItem;
import mate.academy.onlinebookstore.model.ShoppingCart;
import mate.academy.onlinebookstore.repository.order.OrderItemRepository;
import mate.academy.onlinebookstore.repository.order.OrderRepository;
import mate.academy.onlinebookstore.repository.shoppingcart.ShoppingCartRepository;
import mate.academy.onlinebookstore.service.order.OrderService;
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
            throw new EntityNotFoundException("Shopping cart is empty");
        }
        Order order = new Order();
        order.setUser(shoppingCart.getUser());
        order.setStatus(Status.PENDING);
        order.setOrderItems(shoppingCart.getCartItems().stream()
                .map(orderItemMapper::toOrderItem)
                .collect(Collectors.toSet()));
        order.getOrderItems().forEach(orderItem -> orderItem.setOrder(order));
        order.setTotal(order.getOrderItems().stream()
                .map(OrderItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        order.setOrderDate(LocalDateTime.now());
        order.setShippingAddress(requestDto.shippingAddress());
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
                -> new EntityNotFoundException("Order not found"));
        order.setStatus(requestDto.status());
        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public List<OrderItemDto> getAllOrderItems(Long orderId, Long userId) {
        return orderRepository.findByIdAndUserId(orderId, userId).orElseThrow(()
                -> new EntityNotFoundException("Order not found")).getOrderItems().stream()
            .map(orderItemMapper::toDto)
            .toList();
    }

    @Override
    public OrderItemDto getOrderItem(Long orderItemId, Long orderId, Long userId) {
        return orderItemMapper.toDto(orderItemRepository
            .findByIdAndOrderIdAndOrderUserId(orderItemId, orderId, userId)
            .orElseThrow(() -> new EntityNotFoundException("Order's item not found")));
    }

    private ShoppingCart findShoppingCartByUserId(Long userId) {
        return shoppingCartRepository.findByUserId(userId).orElseThrow(
            () -> new EntityNotFoundException("Can't find shopping cart by userId: " + userId)
        );
    }
}
