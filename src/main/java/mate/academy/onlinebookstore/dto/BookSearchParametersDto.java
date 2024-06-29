package mate.academy.onlinebookstore.dto;

import java.math.BigDecimal;

public record BookSearchParametersDto(
        String title,
        String[] authors,
        String isbn,
        BigDecimal fromPrice,
        BigDecimal toPrice) {
}
