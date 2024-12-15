package lada.alex.onlinebookstore.mapper;

import lada.alex.onlinebookstore.config.MapperConfig;
import lada.alex.onlinebookstore.dto.shoppingcart.ShoppingCartDto;
import lada.alex.onlinebookstore.model.ShoppingCart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = CartItemMapper.class)
public interface ShoppingCartMapper {
    @Mapping(source = "user.id", target = "userId")
    ShoppingCartDto toDto(ShoppingCart shoppingCart);
}
