package lada.alex.onlinebookstore.repository.book;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lada.alex.onlinebookstore.exception.SpecificationProviderNotFoundException;
import lada.alex.onlinebookstore.model.Book;
import lada.alex.onlinebookstore.repository.SpecificationProvider;
import lada.alex.onlinebookstore.repository.SpecificationProviderManager;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookSpecificationProviderManager implements SpecificationProviderManager<Book> {
    private final List<SpecificationProvider<Book>> bookSpecificationProviders;

    @Override
    public SpecificationProvider<Book> getSpecificationProvider(String key) {
        return bookSpecificationProviders.stream()
            .filter(p -> p.getKey().equals(key))
            .findFirst()
            .orElseThrow(() -> new SpecificationProviderNotFoundException("Can't find correct "
                + "specification provider for key " + key));
    }
}
