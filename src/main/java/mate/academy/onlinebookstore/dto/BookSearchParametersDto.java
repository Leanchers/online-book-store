package mate.academy.onlinebookstore.dto;

import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import mate.academy.onlinebookstore.validator.ValidPrice;
import org.hibernate.validator.constraints.Length;

@ValidPrice
public record BookSearchParametersDto(
        String title,
        String[] authors,
        @Length(max = 13)
        String isbn,
        @Positive
        BigDecimal fromPrice,
        @Positive
        BigDecimal toPrice) {
}
