package lada.alex.onlinebookstore.mapper;

import lada.alex.onlinebookstore.config.MapperConfig;
import lada.alex.onlinebookstore.dto.order.OrderItemDto;
import lada.alex.onlinebookstore.model.CartItem;
import lada.alex.onlinebookstore.model.OrderItem;
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
