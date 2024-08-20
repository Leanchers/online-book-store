package mate.academy.onlinebookstore.service.shoppingcart.impl;

import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import mate.academy.onlinebookstore.dto.shoppingcart.CreateCartItemRequestDto;
import mate.academy.onlinebookstore.dto.shoppingcart.ShoppingCartDto;
import mate.academy.onlinebookstore.dto.shoppingcart.UpdateCartItemRequestDto;
import mate.academy.onlinebookstore.exception.EntityNotFoundException;
import mate.academy.onlinebookstore.mapper.CartItemMapper;
import mate.academy.onlinebookstore.mapper.ShoppingCartMapper;
import mate.academy.onlinebookstore.model.CartItem;
import mate.academy.onlinebookstore.model.ShoppingCart;
import mate.academy.onlinebookstore.model.User;
import mate.academy.onlinebookstore.repository.book.BookRepository;
import mate.academy.onlinebookstore.repository.shoppingcart.CartItemRepository;
import mate.academy.onlinebookstore.repository.shoppingcart.ShoppingCartRepository;
import mate.academy.onlinebookstore.service.shoppingcart.ShoppingCartService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final CartItemRepository cartItemRepository;
    private final CartItemMapper cartItemMapper;
    private final BookRepository bookRepository;

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

    @Transactional
    @Override
    public ShoppingCartDto addCartItem(CreateCartItemRequestDto requestDto, Long userId) {
        ShoppingCart shoppingCart = findShoppingCartByUserId(userId);
        if (!bookRepository.existsById(requestDto.bookId())) {
            throw new EntityNotFoundException("Book with id: " + requestDto.bookId()
                + " not found");
        }
        Optional<CartItem> optionalCartItem = cartItemRepository.findByShoppingCartIdAndBookId(
                shoppingCart.getId(), requestDto.bookId());
        CartItem cartItem;
        if (optionalCartItem.isPresent()) {
            cartItem = optionalCartItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + requestDto.quantity());
        } else {
            cartItem = cartItemMapper.toModel(requestDto);
            cartItem.setShoppingCart(shoppingCart);
            shoppingCart.getCartItems().add(cartItem);
        }
        cartItemRepository.save(cartItem);
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Transactional
    @Override
    public ShoppingCartDto updateCartItem(Long itemId, UpdateCartItemRequestDto requestDto,
            Long userId) {
        ShoppingCart shoppingCart = findShoppingCartByUserId(userId);
        CartItem cartItem = getCartItem(shoppingCart, itemId);
        cartItem.setQuantity(requestDto.quantity());
        cartItemRepository.save(cartItem);
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Transactional
    @Override
    public void deleteCartItem(Long itemId, Long userId) {
        ShoppingCart shoppingCart = findShoppingCartByUserId(userId);
        CartItem cartItem = getCartItem(shoppingCart, itemId);
        cartItemRepository.delete(cartItem);
    }

    private ShoppingCart findShoppingCartByUserId(Long userId) {
        return shoppingCartRepository.findByUserId(userId).orElseThrow(
            () -> new EntityNotFoundException("Can't find shopping cart by userId: " + userId)
        );
    }

    private CartItem getCartItem(ShoppingCart shoppingCart, Long itemId) {
        return cartItemRepository.findByIdAndShoppingCartId(itemId,
                shoppingCart.getId())
            .orElseThrow(
                () -> new EntityNotFoundException("Can't find item with id: " + itemId
                    + " in shopping cart with id: " + shoppingCart.getId()));
    }
}
