package mate.academy.onlinebookstore.service.category.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.onlinebookstore.dto.category.CategoryDto;
import mate.academy.onlinebookstore.dto.category.CreateCategoryRequestDto;
import mate.academy.onlinebookstore.dto.category.UpdateCategoryRequestDto;
import mate.academy.onlinebookstore.mapper.CategoryMapper;
import mate.academy.onlinebookstore.model.Category;
import mate.academy.onlinebookstore.repository.category.CategoryRepository;
import mate.academy.onlinebookstore.service.category.CategoryService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public List<CategoryDto> findAll(Pageable pageable) {
        return categoryRepository.findAll(pageable).stream()
            .map(categoryMapper::toDto)
            .toList();
    }

    @Override
    public CategoryDto getById(Long id) {
        Category category = getCategory(id);
        return categoryMapper.toDto(category);
    }

    @Override
    public CategoryDto save(CreateCategoryRequestDto requestDto) {
        Category category = categoryMapper.toModel(requestDto);
        return categoryMapper.toDto(categoryRepository.save(category));
    }

    @Override
    public CategoryDto update(Long id, UpdateCategoryRequestDto requestDto) {
        Category category = getCategory(id);
        categoryMapper.updateModel(category, requestDto);
        return categoryMapper.toDto(categoryRepository.save(category));
    }

    @Override
    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }

    private Category getCategory(Long id) {
        return categoryRepository.findById(id).orElseThrow(
            () -> new RuntimeException("Can't find category by id: " + id)
        );
    }
}
