package mate.academy.onlinebookstore.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import mate.academy.onlinebookstore.dto.book.BookDto;
import mate.academy.onlinebookstore.dto.book.CreateBookRequestDto;
import mate.academy.onlinebookstore.dto.book.UpdateBookRequestDto;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookControllerTest {
    protected static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(
            @Autowired DataSource dataSource,
            @Autowired WebApplicationContext applicationContext
    ) throws SQLException {
        mockMvc = MockMvcBuilders
            .webAppContextSetup(applicationContext)
            .apply(springSecurity())
            .build();
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/books/add-books-to-books-table.sql")
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource(
                        "database/categories/add-categories-to-categories-table.sql")
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource(
                    "database/categories/add-books_categories-to-books_categories-table.sql")
            );
        }
    }

    @AfterAll
    static void afterAll(
            @Autowired DataSource dataSource) {
        tearDown(dataSource);
    }

    @SneakyThrows
    static void tearDown(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                new ClassPathResource(
                    "database/books/remove-books-and-categories-from-db-tables.sql")
            );
        }
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

        BookDto expected = new BookDto()
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
        Assertions.assertNotNull(actual);
        Assertions.assertNotNull(actual.getId());
        Assertions.assertEquals(requestDto.getTitle(), actual.getTitle());
        Assertions.assertEquals(expected.getPrice(), actual.getPrice());

        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @WithMockUser(username = "user", roles = {"ADMIN", "USER"})
    @Test
    @DisplayName("Get all books")
    void findAll_ValidRequestDto_ReturnAllProducts() throws Exception {
        List<BookDto> expected = new ArrayList<>();
        expected.add(new BookDto()
                .setId(1L)
                .setTitle("title1")
                .setAuthor("author1")
                .setIsbn("9781234567801")
                .setPrice(BigDecimal.valueOf(10.99))
                .setDescription("description1")
                .setCoverImage("cover_image1"));
        expected.add(new BookDto()
                .setId(2L)
                .setTitle("title2")
                .setAuthor("author2")
                .setIsbn("9781234567802")
                .setPrice(BigDecimal.valueOf(20.99))
                .setDescription("description2")
                .setCoverImage("cover_image2"));
        expected.add(new BookDto()
                .setId(3L)
                .setTitle("title3")
                .setAuthor("author3")
                .setIsbn("9781234567803")
                .setPrice(BigDecimal.valueOf(30.99))
                .setDescription("description3")
                .setCoverImage("cover_image3"));

        MvcResult result = mockMvc.perform(
                get("/books")
                    .contentType(MediaType.APPLICATION_JSON)
            )
                .andExpect(status().isOk())
                .andReturn();

        BookDto[] actual = objectMapper.readValue(result.getResponse().getContentAsString(),
            BookDto[].class);
        Assertions.assertEquals(3, actual.length);
        for (int i = 0; i < actual.length; i++) {
            EqualsBuilder.reflectionEquals(expected.get(i), actual[i], "id", "categoryIds");
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
                .setTitle("title1")
                .setAuthor("author1")
                .setIsbn("9781234567801")
                .setPrice(BigDecimal.valueOf(10.99))
                .setDescription("description1")
                .setCoverImage("cover_image1");

        Assertions.assertNotNull(actual);
        EqualsBuilder.reflectionEquals(expected, actual, "categoryIds");
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
        Assertions.assertNotNull(actual);
        Assertions.assertEquals("Updated Title", actual.getTitle());
        Assertions.assertEquals(BigDecimal.valueOf(19.99), actual.getPrice());
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
                get("/books/search?title=title1")
                    .contentType(MediaType.APPLICATION_JSON)
            )
                .andExpect(status().isOk())
                .andReturn();

        BookDto[] actual = objectMapper.readValue(result.getResponse().getContentAsString(),
            BookDto[].class);
        Assertions.assertTrue(actual.length > 0);
        Assertions.assertEquals("title1", actual[0].getTitle());
    }
}
