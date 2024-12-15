package lada.alex.onlinebookstore.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import lada.alex.onlinebookstore.model.Book;
import lada.alex.onlinebookstore.model.Category;
import lada.alex.onlinebookstore.repository.book.BookRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;

    @Test
    @DisplayName("""
        Find all books in the specified category when the category exists
            """)
    @Sql(
            scripts = {"classpath:database/books/add-books-to-books-table.sql",
                "classpath:database/categories/add-categories-to-categories-table.sql",
                "classpath:database/categories/add-books_categories-to-books_categories-table.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = {
                "classpath:database/categories/remove-books_categories-from-books_categories-table"
                    + ".sql",
                "classpath:database/categories/remove-categories-from-categories-table.sql",
                "classpath:database/books/remove-books-from-books-table.sql"
            },
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    void findAllByCategoryId_ExistedCategoryId_ReturnsBooksList() {
        Category category = new Category();
        category.setId(1L);
        category.setName("name1");
        category.setDescription("FirstDescription");

        Book book1 = new Book();
        book1.setId(1L);
        book1.setTitle("FirstTitle");
        book1.setAuthor("FirstAuthor");
        book1.setIsbn("9781234567801");
        book1.setPrice(BigDecimal.valueOf(10.99));
        book1.setDescription("FirstDescription");
        book1.setCoverImage("FirstCover_image");
        book1.setCategories(Set.of(category));

        Book book2 = new Book();
        book2.setId(2L);
        book2.setTitle("SecondTitle");
        book2.setAuthor("SecondAuthor");
        book2.setIsbn("9781234567802");
        book2.setPrice(BigDecimal.valueOf(20.99));
        book2.setDescription("SecondDescription");
        book2.setCoverImage("SecondCover_image");
        book2.setCategories(Set.of(category));

        List<Book> expected = List.of(book1, book2);

        Pageable pageable = PageRequest.of(0, 10);

        List<Book> actual = bookRepository.findAllByCategoryId(pageable, category.getId());

        assertNotNull(actual);

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }
}
