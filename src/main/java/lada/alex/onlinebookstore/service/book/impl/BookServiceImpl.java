package lada.alex.onlinebookstore.service.book.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lada.alex.onlinebookstore.dto.book.BookDto;
import lada.alex.onlinebookstore.dto.book.BookDtoWithoutCategoryIds;
import lada.alex.onlinebookstore.dto.book.BookSearchParametersDto;
import lada.alex.onlinebookstore.dto.book.CreateBookRequestDto;
import lada.alex.onlinebookstore.dto.book.UpdateBookRequestDto;
import lada.alex.onlinebookstore.exception.EntityNotFoundException;
import lada.alex.onlinebookstore.mapper.BookMapper;
import lada.alex.onlinebookstore.model.Book;
import lada.alex.onlinebookstore.repository.book.BookRepository;
import lada.alex.onlinebookstore.repository.book.BookSpecificationBuilder;
import lada.alex.onlinebookstore.service.book.BookService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final BookSpecificationBuilder bookSpecificationBuilder;

    @Override
    public BookDto save(CreateBookRequestDto requestDto) {
        Book book = bookMapper.toModel(requestDto);
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public List<BookDto> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable).stream()
            .map(bookMapper::toDto)
            .toList();
    }

    @Override
    public BookDto findById(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find book by id" + id)
        );
        return bookMapper.toDto(book);
    }

    @Override
    public BookDto updateById(Long id, UpdateBookRequestDto requestDto) {
        Book updatedBook = bookRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find book by id " + id)
        );
        bookMapper.updateModel(updatedBook, requestDto);
        return bookMapper.toDto(bookRepository.save(updatedBook));
    }

    @Override
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public List<BookDto> search(BookSearchParametersDto params, Pageable pageable) {
        Specification<Book> bookSpecification = bookSpecificationBuilder.build(params);
        return bookRepository.findAll(bookSpecification, pageable)
            .stream()
            .map(bookMapper::toDto)
            .toList();
    }

    @Override
    public List<BookDtoWithoutCategoryIds> getBookByCategoryId(Pageable pageable, Long categoryId) {
        return bookRepository.findAllByCategoryId(pageable, categoryId).stream()
            .map(bookMapper::toDtoWithoutCategories)
            .toList();
    }

}
