package mate.academy.onlinebookstore.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class CreateBookRequestDto {
    @NotNull
    private String title;
    @NotNull
    private String author;
    @NotNull(message = "not null")
    @Length(min = 13, max = 13)
    private String isbn;
    @NotNull
    @Positive
    private BigDecimal price;
    private String description;
    private String coverImage;
}
