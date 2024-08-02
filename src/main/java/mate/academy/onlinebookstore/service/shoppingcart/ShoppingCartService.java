package mate.academy.onlinebookstore.service.shoppingcart;

import mate.academy.onlinebookstore.dto.shoppingcart.CreateCartItemRequestDto;
import mate.academy.onlinebookstore.dto.shoppingcart.ShoppingCartDto;
import mate.academy.onlinebookstore.dto.shoppingcart.UpdateCartItemRequestDto;
import mate.academy.onlinebookstore.model.ShoppingCart;
import mate.academy.onlinebookstore.model.User;

public interface ShoppingCartService{
    void createShoppingCart(User user);

    ShoppingCartDto getByUserId(Long userId);

    ShoppingCartDto addCartItem(CreateCartItemRequestDto requestDto, Long userId);

    ShoppingCartDto updateCartItem(Long itemId, UpdateCartItemRequestDto requestDto, Long userId);

    ShoppingCartDto deleteCartItem(Long itemId, Long userId);
}
