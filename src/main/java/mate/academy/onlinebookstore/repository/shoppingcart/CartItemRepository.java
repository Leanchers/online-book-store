package mate.academy.onlinebookstore.repository.shoppingcart;

import java.util.Optional;
import mate.academy.onlinebookstore.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByIdAndShoppingCartId(Long id, Long shoppingCartId);

    Optional<CartItem> findByShoppingCartIdAndBookId(Long shoppingCartId, Long bookId);
}
