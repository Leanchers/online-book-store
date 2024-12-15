package lada.alex.onlinebookstore.dto.order;

public record OrderItemDto(
        Long id,
        Long bookId,
        int quantity
) {
}
