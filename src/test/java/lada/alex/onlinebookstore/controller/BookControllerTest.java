package lada.alex.onlinebookstore.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lada.alex.onlinebookstore.dto.book.BookDto;
import lada.alex.onlinebookstore.dto.book.CreateBookRequestDto;
import lada.alex.onlinebookstore.dto.book.UpdateBookRequestDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = {
    "classpath:database/books/add-books-to-books-table.sql",
    "classpath:database/categories/add-categories-to-categories-table.sql",
    "classpath:database/categories/add-books_categories-to-books_categories-table.sql"
}, executionPhase = ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = "classpath:database/books/remove-books-and-categories-from-db-tables.sql",
        executionPhase = ExecutionPhase.AFTER_TEST_CLASS)
public class BookControllerTest {
    protected static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders
            .webAppContextSetup(applicationContext)
            .apply(springSecurity())
            .build();
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Create new book")
    @Sql(scripts = "classpath:database/books/remove-saved-book-from-db-tables.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void save_ValidRequestDto_Success() throws Exception {
        CreateBookRequestDto requestDto = new CreateBookRequestDto()
                .setTitle("Title")
                .setAuthor("Author")
                .setPrice(BigDecimal.valueOf(9.99))
                .setIsbn("1234567890123");

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(post("/books")
                .content(jsonRequest)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        BookDto actual = objectMapper
                .readValue(result.getResponse().getContentAsString(), BookDto.class);
        assertNotNull(actual);
        assertNotNull(actual.getId());

        BookDto expected = new BookDto()
                .setTitle("Title")
                .setAuthor("Author")
                .setPrice(BigDecimal.valueOf(9.99))
                .setIsbn("1234567890123");

        assertEquals(requestDto.getTitle(), actual.getTitle());
        assertEquals(expected.getPrice(), actual.getPrice());

        reflectionEquals(expected, actual, "id");
    }

    @WithMockUser(username = "user", roles = {"ADMIN", "USER"})
    @Test
    @DisplayName("Get all books")
    void findAll_ValidRequestDto_ReturnAllProducts() throws Exception {
        List<BookDto> expected = new ArrayList<>();
        expected.add(new BookDto()
                .setId(1L)
                .setTitle("FirstTitle")
                .setAuthor("FirstAuthor")
                .setIsbn("9781234567801")
                .setPrice(BigDecimal.valueOf(10.99))
                .setDescription("FirstDescription")
                .setCoverImage("FirstCover_image"));
        expected.add(new BookDto()
                .setId(2L)
                .setTitle("SecondTitle")
                .setAuthor("SecondAuthor")
                .setIsbn("9781234567802")
                .setPrice(BigDecimal.valueOf(20.99))
                .setDescription("SecondDescription")
                .setCoverImage("SecondCover_image"));
        expected.add(new BookDto()
                .setId(3L)
                .setTitle("ThirdTitle")
                .setAuthor("ThirdAuthor")
                .setIsbn("9781234567803")
                .setPrice(BigDecimal.valueOf(30.99))
                .setDescription("ThirdDescription")
                .setCoverImage("ThirdCover_image"));

        MvcResult result = mockMvc.perform(
                get("/books")
                    .contentType(MediaType.APPLICATION_JSON)
            )
                .andExpect(status().isOk())
                .andReturn();

        BookDto[] actual = objectMapper.readValue(result.getResponse().getContentAsString(),
            BookDto[].class);
        assertEquals(3, actual.length);
        for (int i = 0; i < actual.length; i++) {
            reflectionEquals(expected.get(i), actual[i], "id", "categoryIds");
        }
    }

    @WithMockUser(username = "user", roles = {"ADMIN", "USER"})
    @Test
    @DisplayName("Get book by id")
    void findById_ValidId_ReturnBook() throws Exception {
        MvcResult result = mockMvc.perform(
                get("/books/1")
                    .contentType(MediaType.APPLICATION_JSON)
            )
                .andExpect(status().isOk())
                .andReturn();

        BookDto actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                BookDto.class);
        BookDto expected = new BookDto()
                .setId(1L)
                .setTitle("FirstTitle")
                .setAuthor("FirstAuthor")
                .setIsbn("9781234567801")
                .setPrice(BigDecimal.valueOf(10.99))
                .setDescription("FirstDescription")
                .setCoverImage("FirstCover_image");

        assertNotNull(actual);
        reflectionEquals(expected, actual, "categoryIds");
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Update book by id")
    @Sql(scripts = "classpath:database/books/restore-updated-book-state-in-db-table.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void updateById_ValidRequestDto_Success() throws Exception {
        UpdateBookRequestDto requestDto = new UpdateBookRequestDto()
                .setTitle("Updated Title")
                .setAuthor("Updated Author")
                .setPrice(BigDecimal.valueOf(19.99));

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(put("/books/1")
                .content(jsonRequest)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        BookDto actual = objectMapper
                .readValue(result.getResponse().getContentAsString(), BookDto.class);
        assertNotNull(actual);
        assertEquals("Updated Title", actual.getTitle());
        assertEquals(BigDecimal.valueOf(19.99), actual.getPrice());
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Delete book by id")
    @Sql(scripts = "classpath:database/books/save-new-book-state-in-db-table.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void deleteById_ValidId_Success() throws Exception {
        mockMvc.perform(delete("/books/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @WithMockUser(username = "user", roles = {"ADMIN", "USER"})
    @Test
    @DisplayName("Search books by parameters")
    void search_ValidParameters_ReturnBooks() throws Exception {
        MvcResult result = mockMvc.perform(
                get("/books/search?title=FirstTitle")
                    .contentType(MediaType.APPLICATION_JSON)
            )
                .andExpect(status().isOk())
                .andReturn();

        BookDto[] actual = objectMapper.readValue(result.getResponse().getContentAsString(),
            BookDto[].class);
        assertTrue(actual.length > 0);
        assertEquals("FirstTitle", actual[0].getTitle());
    }

    @WithMockUser(username = "user", roles = {"ADMIN", "USER"})
    @Test
    @DisplayName("Search books by invalid parameters")
    void search_InvalidParameters_ReturnBooks() throws Exception {
        MvcResult result = mockMvc.perform(
                get("/books/search?isbn=12345678901234")
                    .contentType(MediaType.APPLICATION_JSON)
            )
                .andExpect(status().isBadRequest())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        assertTrue(responseContent.contains("isbn length must be between 0 and 13"));
    }
}
