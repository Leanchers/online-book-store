package mate.academy.onlinebookstore.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mate.academy.onlinebookstore.dto.shoppingcart.CreateCartItemRequestDto;
import mate.academy.onlinebookstore.dto.shoppingcart.ShoppingCartDto;
import mate.academy.onlinebookstore.dto.shoppingcart.UpdateCartItemRequestDto;
import mate.academy.onlinebookstore.model.User;
import mate.academy.onlinebookstore.service.shoppingcart.ShoppingCartService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/cart")
public class ShoppingController {
    private ShoppingCartService shoppingCartService;


    @GetMapping
    public ShoppingCartDto getShoppingCart(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return shoppingCartService.getByUserId(user.getId());
    }

    @PostMapping
    public ShoppingCartDto addItemToShoppingCart(Authentication authentication, @RequestBody
    @Valid CreateCartItemRequestDto requestDto) {
        User user = (User) authentication.getPrincipal();
        return shoppingCartService.addCartItem(requestDto, user.getId());
    }

    @PutMapping("/items/{id}")
    public ShoppingCartDto updateItemToShoppingCart(Authentication authentication,
        @PathVariable Long id, @RequestBody @Valid UpdateCartItemRequestDto requestDto) {
        User user = (User) authentication.getPrincipal();
        return shoppingCartService.updateCartItem(id, requestDto, user.getId());
    }

    @DeleteMapping("/items/{id}")
    public ShoppingCartDto deleteItemFromShoppingCart(Authentication authentication,
        @PathVariable Long id) {
        User user = (User) authentication.getPrincipal();
        return shoppingCartService.deleteCartItem(id, user.getId());
    }
}
