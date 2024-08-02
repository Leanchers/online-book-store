package mate.academy.onlinebookstore.dto.shoppingcart;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateCartItemRequestDto(
    @NotNull
    @Positive
    @Digits(integer = 19, fraction = 0)
    Long bookId,
    @Positive
    @Digits(integer = 3, fraction = 0)
    @Max(999)
    int quantity
) {

}
