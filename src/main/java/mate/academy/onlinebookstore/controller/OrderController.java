package mate.academy.onlinebookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.onlinebookstore.dto.order.CreateOrderRequestDto;
import mate.academy.onlinebookstore.dto.order.OrderDto;
import mate.academy.onlinebookstore.dto.order.OrderItemDto;
import mate.academy.onlinebookstore.dto.order.UpdateOrderRequestDto;
import mate.academy.onlinebookstore.model.User;
import mate.academy.onlinebookstore.service.order.OrderService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Orders management", description = "Endpoints for managing product")
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @Operation(summary = "Create order", description = "Create order from shopping cart")
    public OrderDto createOrder(Authentication authentication,
                                @RequestBody CreateOrderRequestDto requestDto) {
        User user = getUser(authentication);
        return orderService.createOrder(requestDto, user.getId());
    }

    @GetMapping
    @Operation(summary = "Get all orders", description = "Get a list of orders for authorized user")
    public List<OrderDto> getOrders(Pageable pageable, Authentication authentication) {
        User user = getUser(authentication);
        return orderService.getOrders(pageable, user.getId());
    }

    @PatchMapping("/{id}/")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update status of order", description = "Update status of order by Admin")
    public OrderDto updateOrder(@RequestBody UpdateOrderRequestDto requestDto,
                                @PathVariable Long id) {
        return orderService.updateStatus(requestDto, id);
    }

    @GetMapping("/{id}/items")
    @Operation(summary = "Get items of order",
        description = "Get a list of items for order of authorized user")
    public List<OrderItemDto> getOrder(Authentication authentication, @PathVariable Long id) {
        User user = getUser(authentication);
        return orderService.getAllOrderItems(id, user.getId());
    }

    @GetMapping("/{orderId}/items/{itemId}")
    @Operation(summary = "Get item",
        description = "Get an item by id")
    public OrderItemDto getOrderItem(Authentication authentication, @PathVariable Long orderId,
                                     @PathVariable Long itemId) {
        User user = getUser(authentication);
        return orderService.getOrderItem(itemId, orderId, user.getId());
    }

    private static User getUser(Authentication authentication) {
        return (User) authentication.getPrincipal();
    }
}
