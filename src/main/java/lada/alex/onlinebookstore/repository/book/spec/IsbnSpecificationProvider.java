package lada.alex.onlinebookstore.repository.book.spec;

import lada.alex.onlinebookstore.model.Book;
import lada.alex.onlinebookstore.repository.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class IsbnSpecificationProvider implements SpecificationProvider<Book> {
    private static final String ISBN_COLUMN_NAME = "isbn";

    @Override
    public String getKey() {
        return ISBN_COLUMN_NAME;
    }

    @Override
    public Specification<Book> getSpecification(String[] params) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get(ISBN_COLUMN_NAME),
            "%" + params[0] + "%");
    }
}
