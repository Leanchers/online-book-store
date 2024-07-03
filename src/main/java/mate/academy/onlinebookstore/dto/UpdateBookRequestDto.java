package mate.academy.onlinebookstore.dto;

import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class UpdateBookRequestDto {
    private String title;
    private String author;
    @Length(min = 13, max = 13)
    private String isbn;
    @Positive
    private BigDecimal price;
    private String description;
    private String coverImage;
}
