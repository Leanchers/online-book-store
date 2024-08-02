package mate.academy.onlinebookstore.dto.shoppingcart;

import java.util.List;

public record ShoppingCartDto(
    Long id,
    Long userId,
    List<CartItemDto> items
) {

}
