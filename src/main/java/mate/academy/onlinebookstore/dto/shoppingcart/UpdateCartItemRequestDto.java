package mate.academy.onlinebookstore.dto.shoppingcart;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;

public record UpdateCartItemRequestDto(
    @Positive
    @Digits(integer = 3, fraction = 0)
    @Max(999)
    int quantity
) {

}
