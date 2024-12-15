package lada.alex.onlinebookstore.dto.shoppingcart;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateCartItemRequestDto(
        @NotNull
        @Positive
        @Digits(integer = 19, fraction = 0)
        Long bookId,
        @Positive
        int quantity
) {

}
