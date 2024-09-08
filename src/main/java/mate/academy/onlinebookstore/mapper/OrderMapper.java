package mate.academy.onlinebookstore.mapper;

import mate.academy.onlinebookstore.config.MapperConfig;
import mate.academy.onlinebookstore.dto.order.OrderDto;
import mate.academy.onlinebookstore.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = OrderItemMapper.class)
public interface OrderMapper {
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "orderItems", target = "books")
    OrderDto toDto(Order order);
}
