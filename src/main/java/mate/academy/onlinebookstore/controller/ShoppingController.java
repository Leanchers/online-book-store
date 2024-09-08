package mate.academy.onlinebookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mate.academy.onlinebookstore.dto.shoppingcart.CreateCartItemRequestDto;
import mate.academy.onlinebookstore.dto.shoppingcart.ShoppingCartDto;
import mate.academy.onlinebookstore.dto.shoppingcart.UpdateCartItemRequestDto;
import mate.academy.onlinebookstore.model.User;
import mate.academy.onlinebookstore.service.shoppingcart.ShoppingCartService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Shopping cart management", description = "Endpoints for managing product")
@RequiredArgsConstructor
@RestController
@RequestMapping("/cart")
public class ShoppingController {
    private final ShoppingCartService shoppingCartService;

    @GetMapping
    @Operation(summary = "Get shoppingCart", description = "Get a shoppingCart for authorized user")
    public ShoppingCartDto getShoppingCart(Authentication authentication) {
        User user = getUser(authentication);
        return shoppingCartService.getByUserId(user.getId());
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @Operation(summary = "Add book to cart", description = "Add a book to shopping cart")
    public ShoppingCartDto addItemToShoppingCart(Authentication authentication, @RequestBody
            @Valid CreateCartItemRequestDto requestDto) {
        User user = getUser(authentication);
        return shoppingCartService.addCartItem(requestDto, user.getId());
    }

    @PutMapping("/items/{id}")
    @Operation(summary = "Update book to cart",
            description = "Update quantity books in shopping cart")
    public ShoppingCartDto updateItemToShoppingCart(Authentication authentication,
            @PathVariable Long id, @RequestBody @Valid UpdateCartItemRequestDto requestDto) {
        User user = getUser(authentication);
        return shoppingCartService.updateCartItem(id, requestDto, user.getId());
    }

    @DeleteMapping("/items/{id}")
    @Operation(summary = "Delete book from cart", description = "Delete a book from shopping cart")
    public void deleteItemFromShoppingCart(Authentication authentication,
            @PathVariable Long id) {
        User user = getUser(authentication);
        shoppingCartService.deleteCartItem(id, user.getId());
    }

    private static User getUser(Authentication authentication) {
        return (User) authentication.getPrincipal();
    }
}
