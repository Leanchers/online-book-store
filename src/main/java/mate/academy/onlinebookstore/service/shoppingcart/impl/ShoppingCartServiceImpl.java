package mate.academy.onlinebookstore.service.shoppingcart.impl;

import lombok.RequiredArgsConstructor;
import mate.academy.onlinebookstore.dto.shoppingcart.CreateCartItemRequestDto;
import mate.academy.onlinebookstore.dto.shoppingcart.ShoppingCartDto;
import mate.academy.onlinebookstore.dto.shoppingcart.UpdateCartItemRequestDto;
import mate.academy.onlinebookstore.exception.EntityNotFoundException;
import mate.academy.onlinebookstore.mapper.ShoppingCartMapper;
import mate.academy.onlinebookstore.model.ShoppingCart;
import mate.academy.onlinebookstore.model.User;
import mate.academy.onlinebookstore.repository.shoppingcart.ShoppingCartRepository;
import mate.academy.onlinebookstore.service.shoppingcart.CartItemService;
import mate.academy.onlinebookstore.service.shoppingcart.ShoppingCartService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private ShoppingCartRepository shoppingCartRepository;
    private ShoppingCartMapper shoppingCartMapper;
    private CartItemService cartItemService;

    @Override
    public void createShoppingCart(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public ShoppingCartDto getByUserId(Long userId) {
        return shoppingCartMapper.toDto(findShoppingCartByUserId(userId));
    }

    @Override
    public ShoppingCartDto addCartItem(CreateCartItemRequestDto requestDto, Long userId) {
        ShoppingCart shoppingCart = findShoppingCartByUserId(userId);
        cartItemService.createCartItem(shoppingCart, requestDto);
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    public ShoppingCartDto updateCartItem(Long itemId, UpdateCartItemRequestDto requestDto,
        Long userId) {
        ShoppingCart shoppingCart = findShoppingCartByUserId(userId);
        cartItemService.updateCartItem(shoppingCart, requestDto, itemId);
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    public ShoppingCartDto deleteCartItem(Long itemId, Long userId) {
        ShoppingCart shoppingCart = findShoppingCartByUserId(userId);
        cartItemService.deleteCartItem(shoppingCart, itemId);
        return shoppingCartMapper.toDto(shoppingCart);
    }


    private ShoppingCart findShoppingCartByUserId(Long userId) {
        return shoppingCartRepository.findByUserId(userId).orElseThrow(
            () -> new EntityNotFoundException("Can't find shopping cart by userId: " + userId)
        );
    }

}
