package mate.academy.onlinebookstore.dto.shoppingcart;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

public record CreateCartItemRequestDto(
    @NotNull
    @Positive
    @Digits(integer = 19, fraction = 0)
    Long bookId,
    @NotNull
    @Positive
    int quantity
) {

}
