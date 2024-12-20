package lada.alex.onlinebookstore.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lada.alex.onlinebookstore.dto.book.BookDto;
import lada.alex.onlinebookstore.dto.book.BookDtoWithoutCategoryIds;
import lada.alex.onlinebookstore.dto.book.BookSearchParametersDto;
import lada.alex.onlinebookstore.dto.book.CreateBookRequestDto;
import lada.alex.onlinebookstore.dto.book.UpdateBookRequestDto;
import lada.alex.onlinebookstore.exception.EntityNotFoundException;
import lada.alex.onlinebookstore.mapper.BookMapper;
import lada.alex.onlinebookstore.model.Book;
import lada.alex.onlinebookstore.model.Category;
import lada.alex.onlinebookstore.repository.book.BookRepository;
import lada.alex.onlinebookstore.repository.book.BookSpecificationBuilder;
import lada.alex.onlinebookstore.service.book.impl.BookServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    private static Book book;
    private static BookDto bookDto;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private BookMapper bookMapper;
    @Mock
    private BookSpecificationBuilder bookSpecificationBuilder;
    @InjectMocks
    private BookServiceImpl bookService;

    @BeforeAll
    static void setUp() {
        book = new Book();
        book.setId(1L);
        book.setTitle("title");
        book.setAuthor("Author");
        book.setIsbn("9781234567897");
        book.setPrice(BigDecimal.valueOf(19.99));
        book.setDescription("description");
        book.setCoverImage("cover image");
        book.setCategories(Set.of(new Category(1L)));

        bookDto = new BookDto();
        bookDto.setId(1L);
        bookDto.setTitle("title");
        bookDto.setAuthor("author");
        bookDto.setIsbn("9781234567897");
        bookDto.setPrice(BigDecimal.valueOf(19.99));
        bookDto.setDescription("description");
        bookDto.setCoverImage("cover image");
        bookDto.setCategoryIds(Set.of(1L));
    }

    @Test
    @DisplayName("""
        Save book into DB and return BookDto
            """)
    void save_ValidRequestDto_ReturnsBookDto() {
        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setTitle("title");
        requestDto.setAuthor("author");
        requestDto.setIsbn("9781234567897");
        requestDto.setPrice(BigDecimal.valueOf(19.99));
        requestDto.setDescription("description");
        requestDto.setCoverImage("cover image");
        requestDto.setCategoryIds(Set.of(1L));
        BookDto expected = bookDto;
        when(bookMapper.toModel(requestDto)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(expected);

        BookDto actual = bookService.save(requestDto);

        assertEquals(expected, actual);
        verify(bookMapper).toModel(requestDto);
        verify(bookRepository).save(book);
        verify(bookMapper).toDto(book);
    }

    @Test
    @DisplayName("""
        Find all books containing in DB
            """)
    void findAll_ReturnsListOfBookDto() {
        List<BookDto> expected = List.of(bookDto);
        Page<Book> page = new PageImpl<>(List.of(book));
        Pageable pageable = PageRequest.of(0, 10);
        when(bookRepository.findAll(pageable)).thenReturn(page);
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        List<BookDto> actual = bookService.findAll(pageable);

        assertEquals(expected, actual);

        verify(bookRepository).findAll(pageable);
        verify(bookMapper).toDto(book);
    }

    @Test
    @DisplayName("""
    Should find Book by ID and return BookDto
            """)
    void findById_ValidId_ReturnsBookDto() {
        BookDto expected = bookDto;

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookMapper.toDto(book)).thenReturn(expected);

        BookDto actual = bookService.findById(1L);

        assertEquals(expected, actual);
        verify(bookRepository).findById(1L);
        verify(bookMapper).toDto(book);
    }

    @Test
    @DisplayName("""
    Should throw EntityNotFoundException when Book not found by ID
            """)
    void findById_InvalidId_ThrowsEntityNotFoundException() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> bookService.findById(1L));
    }

    @Test
    @DisplayName("Should update and return BookDto")
    void updateById_ValidId_ReturnsUpdatedBookDto() {
        UpdateBookRequestDto updateRequestDto = new UpdateBookRequestDto();
        updateRequestDto.setTitle("new title");
        updateRequestDto.setAuthor("new author");
        updateRequestDto.setIsbn("2781234567897");
        updateRequestDto.setPrice(BigDecimal.valueOf(29.99));
        updateRequestDto.setDescription("new description");
        updateRequestDto.setCoverImage("new cover image");
        updateRequestDto.setCategoryIds(Set.of(2L));

        Book updatedBook = book;
        updatedBook.setTitle(updateRequestDto.getTitle());
        updatedBook.setAuthor(updateRequestDto.getAuthor());
        updatedBook.setIsbn(updateRequestDto.getIsbn());
        updatedBook.setPrice(updateRequestDto.getPrice());
        updatedBook.setDescription(updateRequestDto.getDescription());
        updatedBook.setCoverImage(updateRequestDto.getCoverImage());
        updatedBook.setCategories(Set.of(new Category(2L)));

        BookDto expected = new BookDto();
        expected.setId(1L);
        expected.setTitle("new title");
        expected.setAuthor("new author");
        expected.setIsbn("2781234567897");
        expected.setPrice(BigDecimal.valueOf(29.99));
        expected.setDescription("new description");
        expected.setCoverImage("new cover image");
        expected.setCategoryIds(Set.of(2L));

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookMapper.toDto(updatedBook)).thenReturn(expected);
        when(bookRepository.save(updatedBook)).thenReturn(updatedBook);

        BookDto actual = bookService.updateById(1L, updateRequestDto);

        assertEquals(expected, actual);
        verify(bookRepository).findById(1L);
        verify(bookRepository).save(updatedBook);
        verify(bookMapper).toDto(updatedBook);
    }

    @Test
    @DisplayName("Should delete Book by ID")
    void deleteById_ValidId_DeletesBook() {
        doNothing().when(bookRepository).deleteById(1L);

        bookService.deleteById(1L);

        verify(bookRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Should return list of BookDto when searching with parameters")
    void search_WithValidParameters_ReturnsListOfBookDto() {
        BookSearchParametersDto searchParams = new BookSearchParametersDto(
                null,
                new String[]{"author"},
                null,
                null,
                null
                );
        Pageable pageable = PageRequest.of(0, 10);
        Page<Book> page = new PageImpl<>(List.of(book));
        Specification<Book> bookSpecification = Mockito.mock(Specification.class);

        when(bookSpecificationBuilder.build(searchParams)).thenReturn(bookSpecification);
        when(bookRepository.findAll(eq(bookSpecification), eq(pageable))).thenReturn(page);
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        List<BookDto> expected = List.of(bookDto);
        List<BookDto> actual = bookService.search(searchParams, pageable);

        assertEquals(expected, actual);

        verify(bookSpecificationBuilder).build(searchParams);
        verify(bookRepository).findAll(eq(bookSpecification), eq(pageable));
        verify(bookMapper).toDto(book);
    }

    @Test
    @DisplayName("Should return list of BookDtoWithoutCategoryIds by CategoryId")
    void getBookByCategoryId_ReturnsListOfBookDtoWithoutCategoryIds() {
        Pageable pageable = PageRequest.of(0, 10);
        BookDtoWithoutCategoryIds bookDtoWithoutCategoryIds = new BookDtoWithoutCategoryIds(
                1L,
                "title",
                "author",
                "9781234567897",
                BigDecimal.valueOf(19.99),
                "description",
                "cover image"
        );
        List<BookDtoWithoutCategoryIds> expected = List.of(bookDtoWithoutCategoryIds);
        when(bookRepository.findAllByCategoryId(pageable, 1L)).thenReturn(List.of(book));
        when(bookMapper.toDtoWithoutCategories(book)).thenReturn(bookDtoWithoutCategoryIds);

        List<BookDtoWithoutCategoryIds> actual = bookService.getBookByCategoryId(pageable, 1L);

        assertEquals(expected, actual);
    }
}
