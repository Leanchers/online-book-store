package mate.academy.onlinebookstore.dto.order;

import mate.academy.onlinebookstore.model.Order.Status;

public record UpdateOrderRequestDto(
        Status status
) {
}
