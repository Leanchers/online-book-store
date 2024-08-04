package mate.academy.onlinebookstore.dto.shoppingcart;

public record CartItemDto(
        Long id,
        Long bookId,
        String bookTitle,
        int quantity
) {

}
