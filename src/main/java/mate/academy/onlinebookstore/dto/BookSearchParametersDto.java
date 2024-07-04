package mate.academy.onlinebookstore.dto;

import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import mate.academy.onlinebookstore.validator.ValidPriceRange;
import org.hibernate.validator.constraints.Length;

@ValidPriceRange
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
