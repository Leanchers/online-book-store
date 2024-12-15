package lada.alex.onlinebookstore.service.category;

import java.util.List;
import lada.alex.onlinebookstore.dto.category.CategoryDto;
import lada.alex.onlinebookstore.dto.category.CreateCategoryRequestDto;
import lada.alex.onlinebookstore.dto.category.UpdateCategoryRequestDto;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    List<CategoryDto> findAll(Pageable pageable);

    CategoryDto getById(Long id);

    CategoryDto save(CreateCategoryRequestDto requestDto);

    CategoryDto update(Long id, UpdateCategoryRequestDto requestDto);

    void deleteById(Long id);
}
