package lada.alex.onlinebookstore.mapper;

import java.math.BigDecimal;
import lada.alex.onlinebookstore.config.MapperConfig;
import lada.alex.onlinebookstore.dto.order.CreateOrderRequestDto;
import lada.alex.onlinebookstore.dto.order.OrderDto;
import lada.alex.onlinebookstore.model.Order;
import lada.alex.onlinebookstore.model.OrderItem;
import lada.alex.onlinebookstore.model.ShoppingCart;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class, uses = OrderItemMapper.class)
public interface OrderMapper {
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "orderItems", target = "books")
    OrderDto toDto(Order order);

    @Mapping(source = "shoppingCart.user", target = "user")
    @Mapping(source = "shoppingCart.cartItems", target = "orderItems")
    Order toModel(CreateOrderRequestDto requestDto, ShoppingCart shoppingCart);

    @AfterMapping
    default void calculateTotal(@MappingTarget Order order) {
        BigDecimal total = order.getOrderItems().stream()
                .map(OrderItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotal(total);
    }
}
