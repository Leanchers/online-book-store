package lada.alex.onlinebookstore.service.shoppingcart;

import lada.alex.onlinebookstore.dto.shoppingcart.CreateCartItemRequestDto;
import lada.alex.onlinebookstore.dto.shoppingcart.ShoppingCartDto;
import lada.alex.onlinebookstore.dto.shoppingcart.UpdateCartItemRequestDto;
import lada.alex.onlinebookstore.model.User;

public interface ShoppingCartService {
    void createShoppingCart(User user);

    ShoppingCartDto getByUserId(Long userId);

    ShoppingCartDto addCartItem(CreateCartItemRequestDto requestDto, Long userId);

    ShoppingCartDto updateCartItem(Long itemId, UpdateCartItemRequestDto requestDto, Long userId);

    void deleteCartItem(Long itemId, Long userId);
}
