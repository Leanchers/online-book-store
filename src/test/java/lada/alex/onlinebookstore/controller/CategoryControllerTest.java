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

import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import lada.alex.onlinebookstore.dto.book.BookDtoWithoutCategoryIds;
import lada.alex.onlinebookstore.dto.category.CategoryDto;
import lada.alex.onlinebookstore.dto.category.CreateCategoryRequestDto;
import lada.alex.onlinebookstore.dto.category.UpdateCategoryRequestDto;
import org.junit.jupiter.api.AfterAll;
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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CategoryControllerTest {
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
                new ClassPathResource("database/categories/add-categories-to-categories-table.sql")
            );
        }
    }

    @AfterAll
    static void afterAll(@Autowired DataSource dataSource) {
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
    @DisplayName("Create new category")
    @Sql(scripts = "classpath:database/categories/remove-saved-category-from-db-tables.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void createCategory_ValidRequest_Success() throws Exception {
        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto(
                "Category", "Description");
        CategoryDto expected = new CategoryDto(1L, "Category", "Description");

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(post("/categories")
                .content(jsonRequest)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        CategoryDto actual = objectMapper
                .readValue(result.getResponse().getContentAsString(), CategoryDto.class);
        assertNotNull(actual);
        assertNotNull(actual.id());
        assertEquals(expected.name(), actual.name());
    }

    @WithMockUser(username = "user", roles = {"USER", "ADMIN"})
    @Test
    @DisplayName("Get all categories")
    void getAllCategories_ValidRequest_ReturnCategories() throws Exception {
        MvcResult result = mockMvc.perform(get("/categories")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        CategoryDto[] actual = objectMapper
            .readValue(result.getResponse().getContentAsString(), CategoryDto[].class);

        assertTrue(actual.length > 0);
        assertEquals("name1", actual[0].name());
    }

    @WithMockUser(username = "user", roles = {"USER", "ADMIN"})
    @Test
    @DisplayName("Get category by id")
    void getCategoryById_ValidId_ReturnCategory() throws Exception {
        MvcResult result = mockMvc.perform(get("/categories/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        CategoryDto actual = objectMapper
                .readValue(result.getResponse().getContentAsString(), CategoryDto.class);

        assertNotNull(actual);
        assertEquals(1L, actual.id());
        assertEquals("name1", actual.name());
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Update category by id")
    @Sql(scripts = "classpath:database/categories/restore-updated-category-state-in-db-table.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void updateCategory_ValidRequest_Success() throws Exception {
        UpdateCategoryRequestDto requestDto = new UpdateCategoryRequestDto(
                "Updated Category", null);

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(put("/categories/1")
                .content(jsonRequest)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        CategoryDto actual = objectMapper
                .readValue(result.getResponse().getContentAsString(), CategoryDto.class);

        assertNotNull(actual);
        assertEquals("Updated Category", actual.name());
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Delete category by id")
    @Sql(scripts = "classpath:database/categories/save-new-category-to-db-table.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void deleteCategory_ValidId_Success() throws Exception {
        mockMvc.perform(delete("/categories/3")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @WithMockUser(username = "user", roles = {"USER", "ADMIN"})
    @Test
    @DisplayName("Get books by category id")
    @Sql(
            scripts = {"classpath:database/books/add-books-to-books-table.sql",
                "classpath:database/categories/add-books_categories-to-books_categories-table.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    void getBooksByCategoryId_ValidId_ReturnBooks() throws Exception {
        MvcResult result = mockMvc.perform(get("/categories/1/books")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        BookDtoWithoutCategoryIds[] actual = objectMapper
            .readValue(result.getResponse().getContentAsString(),
                BookDtoWithoutCategoryIds[].class);

        assertTrue(actual.length > 0);
        assertEquals("FirstTitle", actual[0].title());
    }
}
