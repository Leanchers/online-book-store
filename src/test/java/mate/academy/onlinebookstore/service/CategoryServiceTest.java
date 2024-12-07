package mate.academy.onlinebookstore.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import mate.academy.onlinebookstore.dto.category.CategoryDto;
import mate.academy.onlinebookstore.dto.category.CreateCategoryRequestDto;
import mate.academy.onlinebookstore.dto.category.UpdateCategoryRequestDto;
import mate.academy.onlinebookstore.exception.EntityNotFoundException;
import mate.academy.onlinebookstore.mapper.CategoryMapper;
import mate.academy.onlinebookstore.model.Category;
import mate.academy.onlinebookstore.repository.category.CategoryRepository;
import mate.academy.onlinebookstore.service.category.impl.CategoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryMapper categoryMapper;
    @InjectMocks
    private CategoryServiceImpl categoryService;

    private Category category;
    private CategoryDto categoryDto;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setId(1L);
        category.setName("Category Name");

        categoryDto = new CategoryDto(1L, "Category Name", "Category Description");
    }

    @Test
    @DisplayName("Should return all categories as a list of CategoryDto")
    void findAll_ReturnsListOfCategoryDto() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Category> page = new PageImpl<>(List.of(category));
        when(categoryRepository.findAll(pageable)).thenReturn(page);
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        List<CategoryDto> result = categoryService.findAll(pageable);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(categoryDto, result.get(0));
        verify(categoryRepository).findAll(pageable);
        verify(categoryMapper).toDto(category);
    }

    @Test
    @DisplayName("Should return category by ID")
    void getById_ReturnsCategoryDto() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        CategoryDto result = categoryService.getById(1L);

        assertNotNull(result);
        assertEquals(categoryDto, result);
        verify(categoryRepository).findById(1L);
        verify(categoryMapper).toDto(category);
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when category not found by ID")
    void getById_CategoryNotFound_ThrowsException() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> categoryService.getById(1L));
        verify(categoryRepository).findById(1L);
    }

    @Test
    @DisplayName("Should save a new category and return CategoryDto")
    void save_ReturnsCategoryDto() {
        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto("New Category",
                "New Description");
        when(categoryMapper.toModel(requestDto)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        CategoryDto result = categoryService.save(requestDto);

        assertNotNull(result);
        assertEquals(categoryDto, result);
        verify(categoryMapper).toModel(requestDto);
        verify(categoryRepository).save(category);
        verify(categoryMapper).toDto(category);
    }

    @Test
    @DisplayName("Should update an existing category and return updated CategoryDto")
    void update_ReturnsUpdatedCategoryDto() {
        UpdateCategoryRequestDto requestDto = new UpdateCategoryRequestDto("Updated Category",
                "Updated Description");
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        doNothing().when(categoryMapper).updateModel(category, requestDto);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        CategoryDto result = categoryService.update(1L, requestDto);

        assertNotNull(result);
        assertEquals(categoryDto, result);
        verify(categoryRepository).findById(1L);
        verify(categoryMapper).updateModel(category, requestDto);
        verify(categoryRepository).save(category);
        verify(categoryMapper).toDto(category);
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when updating non-existing category")
    void update_CategoryNotFound_ThrowsException() {
        UpdateCategoryRequestDto requestDto = new UpdateCategoryRequestDto("Updated Category",
                "Updated Description");
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> categoryService.update(1L, requestDto));
        verify(categoryRepository).findById(1L);
        verify(categoryMapper, never()).updateModel(any(), any());
    }

    @Test
    @DisplayName("Should delete category by ID")
    void deleteById_DeletesCategory() {
        doNothing().when(categoryRepository).deleteById(1L);

        categoryService.deleteById(1L);

        verify(categoryRepository).deleteById(1L);
    }
}
