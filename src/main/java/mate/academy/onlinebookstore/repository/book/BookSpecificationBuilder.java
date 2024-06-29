package mate.academy.onlinebookstore.repository.book;

import java.util.Objects;
import lombok.RequiredArgsConstructor;
import mate.academy.onlinebookstore.dto.BookSearchParametersDto;
import mate.academy.onlinebookstore.model.Book;
import mate.academy.onlinebookstore.repository.SpecificationBuilder;
import mate.academy.onlinebookstore.repository.SpecificationProviderManager;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookSpecificationBuilder implements SpecificationBuilder<Book> {
    private static final String KEY_TITLE = "title";
    private static final String KEY_AUTHOR = "author";
    private static final String KEY_ISBN = "isbn";
    private static final String KEY_PRICE = "price";
    private final SpecificationProviderManager<Book> bookSpecificationProviderManager;

    @Override
    public Specification<Book> build(BookSearchParametersDto searchParameters) {
        Specification<Book> spec = Specification.where(null);
        if (searchParameters.title() != null && !searchParameters.title().isEmpty()) {
            spec = spec.and(bookSpecificationProviderManager.getSpecificationProvider(KEY_TITLE)
                .getSpecification(new String[]{searchParameters.title()}));
        }
        if (searchParameters.authors() != null && searchParameters.authors().length > 0) {
            spec = spec.and(bookSpecificationProviderManager.getSpecificationProvider(KEY_AUTHOR)
                .getSpecification(searchParameters.authors()));
        }
        if (searchParameters.isbn() != null && !searchParameters.isbn().isEmpty()) {
            spec = spec.and(bookSpecificationProviderManager.getSpecificationProvider(KEY_ISBN)
                .getSpecification(new String[]{searchParameters.isbn()}));
        }
        if (searchParameters.toPrice() != null || searchParameters.fromPrice() != null) {
            String fromPrice = Objects.toString(searchParameters.fromPrice(), null);
            String toPrice = Objects.toString(searchParameters.toPrice(), null);
            spec = spec.and(bookSpecificationProviderManager.getSpecificationProvider(KEY_PRICE)
                .getSpecification(new String[]{fromPrice, toPrice}));
        }
        return spec;
    }
}
