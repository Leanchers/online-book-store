package lada.alex.onlinebookstore.service.book;

import java.util.List;
import lada.alex.onlinebookstore.dto.book.BookDto;
import lada.alex.onlinebookstore.dto.book.BookDtoWithoutCategoryIds;
import lada.alex.onlinebookstore.dto.book.BookSearchParametersDto;
import lada.alex.onlinebookstore.dto.book.CreateBookRequestDto;
import lada.alex.onlinebookstore.dto.book.UpdateBookRequestDto;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDto);

    List<BookDto> findAll(Pageable pageable);

    BookDto findById(Long id);

    BookDto updateById(Long id, UpdateBookRequestDto requestDto);

    void deleteById(Long id);

    List<BookDto> search(BookSearchParametersDto params, Pageable pageable);

    List<BookDtoWithoutCategoryIds> getBookByCategoryId(Pageable pageable, Long categoryId);
}
