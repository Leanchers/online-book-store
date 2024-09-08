package mate.academy.onlinebookstore.mapper;

import mate.academy.onlinebookstore.config.MapperConfig;
import mate.academy.onlinebookstore.dto.order.OrderItemDto;
import mate.academy.onlinebookstore.model.CartItem;
import mate.academy.onlinebookstore.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = BookMapper.class)
public interface OrderItemMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(source = "book.price", target = "price")
    OrderItem toOrderItem(CartItem cartItem);

    @Mapping(source = "book.id", target = "bookId")
    OrderItemDto toDto(OrderItem orderItem);
}
