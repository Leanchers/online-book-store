package mate.academy.onlinebookstore.repository.book.spec;

import mate.academy.onlinebookstore.model.Book;
import mate.academy.onlinebookstore.repository.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class TitleSpecificationProvider implements SpecificationProvider<Book> {
    private static final String TITLE_COLUMN_NAME = "title";

    @Override
    public String getKey() {
        return TITLE_COLUMN_NAME;
    }

    public Specification<Book> getSpecification(String[] params) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get(TITLE_COLUMN_NAME),
            "%" + params[0] + "%");
    }
}
