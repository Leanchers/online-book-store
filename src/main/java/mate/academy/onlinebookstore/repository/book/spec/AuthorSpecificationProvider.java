package mate.academy.onlinebookstore.repository.book.spec;

import java.util.Arrays;
import mate.academy.onlinebookstore.model.Book;
import mate.academy.onlinebookstore.repository.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class AuthorSpecificationProvider implements SpecificationProvider<Book> {
    private static final String AUTHOR_COLUMN_NAME = "author";

    @Override
    public String getKey() {
        return AUTHOR_COLUMN_NAME;
    }

    public Specification<Book> getSpecification(String[] params) {
        return (root, query, criteriaBuilder) -> root.get(AUTHOR_COLUMN_NAME)
            .in(Arrays.stream(params).toArray());
    }
}
