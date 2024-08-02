package mate.academy.onlinebookstore.service.shoppingcart;

import mate.academy.onlinebookstore.dto.shoppingcart.ShoppingCartDto;
import mate.academy.onlinebookstore.model.ShoppingCart;
import mate.academy.onlinebookstore.model.User;

public interface ShoppingCartService{
    void createShoppingCart(User user);
    //ShoppingCartDto getByUserId(e);
}
