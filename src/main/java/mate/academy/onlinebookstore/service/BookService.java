package mate.academy.onlinebookstore.service;

import java.util.List;
import mate.academy.bookstore.model.Book;

public interface BookService {
    Book save(Book book);
    List findAll();
}
