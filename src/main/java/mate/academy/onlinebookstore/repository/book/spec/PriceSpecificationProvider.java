package mate.academy.onlinebookstore.repository.book.spec;

import jakarta.persistence.criteria.Predicate;
import java.math.BigDecimal;
import mate.academy.onlinebookstore.model.Book;
import mate.academy.onlinebookstore.repository.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class PriceSpecificationProvider implements SpecificationProvider<Book> {
    private static final String KEY = "price";

    @Override
    public String getKey() {
        return KEY;
    }

    @Override
    public Specification<Book> getSpecification(String[] params) {
        return (root, query, criteriaBuilder) -> {
            String fromPrice = params[0];
            String toPrice = params[1];
            Predicate predicate = criteriaBuilder.conjunction();
            if (fromPrice != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.ge(root.get(KEY),
                    new BigDecimal(fromPrice)));
            }
            if (toPrice != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.le(root.get(KEY),
                    new BigDecimal(toPrice)));
            }
            return predicate;
        };
    }
}
