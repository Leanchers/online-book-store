package lada.alex.onlinebookstore.dto.book;

import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import lada.alex.onlinebookstore.validator.ValidPrice;
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
