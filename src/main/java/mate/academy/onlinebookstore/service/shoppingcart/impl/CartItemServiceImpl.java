package mate.academy.onlinebookstore.service.shoppingcart.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import mate.academy.onlinebookstore.dto.shoppingcart.CreateCartItemRequestDto;
import mate.academy.onlinebookstore.dto.shoppingcart.UpdateCartItemRequestDto;
import mate.academy.onlinebookstore.exception.EntityNotFoundException;
import mate.academy.onlinebookstore.mapper.CartItemMapper;
import mate.academy.onlinebookstore.model.CartItem;
import mate.academy.onlinebookstore.model.ShoppingCart;
import mate.academy.onlinebookstore.repository.book.BookRepository;
import mate.academy.onlinebookstore.repository.shoppingcart.CartItemRepository;
import mate.academy.onlinebookstore.service.shoppingcart.CartItemService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {
    private final CartItemRepository cartItemRepository;
    private final CartItemMapper cartItemMapper;
    private final BookRepository bookRepository;

    @Transactional
    @Override
    public void createCartItem(ShoppingCart shoppingCart,
            CreateCartItemRequestDto requestDto) {
        if (!bookRepository.existsById(requestDto.bookId())) {
            throw new EntityNotFoundException("Book with id: " + requestDto.bookId()
                + " not found");
        }
        CartItem cartItem = cartItemRepository.findByShoppingCartIdAndBookId(shoppingCart.getId(),
            requestDto.bookId()).orElse(null);
        if (cartItem != null) {
            cartItem.setQuantity(cartItem.getQuantity() + requestDto.quantity());
        } else {
            cartItem = cartItemMapper.toModel(requestDto);
            cartItem.setShoppingCart(shoppingCart);
            shoppingCart.getCartItems().add(cartItem);
        }
        cartItemRepository.save(cartItem);
    }

    @Transactional
    @Override
    public void updateCartItem(ShoppingCart shoppingCart, UpdateCartItemRequestDto requestDto,
            Long itemId) {
        CartItem cartItem = getCartItem(shoppingCart, itemId);
        cartItem.setQuantity(requestDto.quantity());
        cartItemRepository.save(cartItem);
    }

    @Transactional
    @Override
    public void deleteCartItem(ShoppingCart shoppingCart, Long itemId) {
        CartItem cartItem = getCartItem(shoppingCart, itemId);
        cartItemRepository.deleteById(cartItem.getId());
    }

    private CartItem getCartItem(ShoppingCart shoppingCart, Long itemId) {
        return cartItemRepository.findByIdAndShoppingCartId(itemId,
                shoppingCart.getId())
                .orElseThrow(
                    () -> new EntityNotFoundException("Can't find item with id: " + itemId
                        + " in shopping cart with id: " + shoppingCart.getId()));
    }
}
