package lada.alex.onlinebookstore.dto.order;

import jakarta.validation.constraints.NotBlank;
import lada.alex.onlinebookstore.model.Order.Status;

public record UpdateOrderRequestDto(
        @NotBlank
        Status status
) {
}
