package mate.academy.onlinebookstore.service.book.impl;

import java.util.List;
import mate.academy.onlinebookstore.dto.book.BookDto;
import mate.academy.onlinebookstore.dto.book.BookSearchParametersDto;
import mate.academy.onlinebookstore.dto.book.CreateBookRequestDto;
import mate.academy.onlinebookstore.dto.book.UpdateBookRequestDto;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDto);

    List<BookDto> findAll(Pageable pageable);

    BookDto findById(Long id);

    BookDto updateById(Long id, UpdateBookRequestDto requestDto);

    void deleteById(Long id);

    List<BookDto> search(BookSearchParametersDto params, Pageable pageable);
}
