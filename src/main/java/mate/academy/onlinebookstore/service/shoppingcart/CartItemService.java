package mate.academy.onlinebookstore.service.shoppingcart;

import mate.academy.onlinebookstore.dto.shoppingcart.CreateCartItemRequestDto;
import mate.academy.onlinebookstore.dto.shoppingcart.UpdateCartItemRequestDto;
import mate.academy.onlinebookstore.model.ShoppingCart;

public interface CartItemService {
    void createCartItem(ShoppingCart shoppingCart, CreateCartItemRequestDto requestDto);

    void updateCartItem(ShoppingCart shoppingCart, UpdateCartItemRequestDto requestDto,
            Long itemId);

    void deleteCartItem(ShoppingCart shoppingCart, Long itemId);
}
