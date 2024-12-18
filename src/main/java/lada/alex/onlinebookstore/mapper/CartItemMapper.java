package lada.alex.onlinebookstore.mapper;

import lada.alex.onlinebookstore.config.MapperConfig;
import lada.alex.onlinebookstore.dto.shoppingcart.CartItemDto;
import lada.alex.onlinebookstore.dto.shoppingcart.CreateCartItemRequestDto;
import lada.alex.onlinebookstore.model.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = BookMapper.class)
public interface CartItemMapper {
    @Mapping(source = "book.id", target = "bookId")
    @Mapping(source = "book.title", target = "bookTitle")
    CartItemDto toDto(CartItem cartItem);

    @Mapping(source = "bookId", target = "book", qualifiedByName = "bookFromId")
    CartItem toModel(CreateCartItemRequestDto cartItemDto);
}
