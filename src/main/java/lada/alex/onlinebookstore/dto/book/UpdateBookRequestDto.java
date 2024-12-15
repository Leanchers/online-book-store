package lada.alex.onlinebookstore.dto.book;

import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@Accessors(chain = true)
public class UpdateBookRequestDto {
    private String title;
    private String author;
    @Length(min = 13, max = 13)
    private String isbn;
    @Positive
    private BigDecimal price;
    private String description;
    private String coverImage;
    private Set<Long> categoryIds;
}
