package mate.academy.onlinebookstore.dto.order;

import jakarta.validation.constraints.NotBlank;
import mate.academy.onlinebookstore.model.Order.Status;

public record UpdateOrderRequestDto(
        @NotBlank
        Status status
) {
}
