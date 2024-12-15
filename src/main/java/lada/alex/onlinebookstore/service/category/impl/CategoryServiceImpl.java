package lada.alex.onlinebookstore.service.category.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lada.alex.onlinebookstore.dto.category.CategoryDto;
import lada.alex.onlinebookstore.dto.category.CreateCategoryRequestDto;
import lada.alex.onlinebookstore.dto.category.UpdateCategoryRequestDto;
import lada.alex.onlinebookstore.exception.EntityNotFoundException;
import lada.alex.onlinebookstore.mapper.CategoryMapper;
import lada.alex.onlinebookstore.model.Category;
import lada.alex.onlinebookstore.repository.category.CategoryRepository;
import lada.alex.onlinebookstore.service.category.CategoryService;
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
            () -> new EntityNotFoundException("Can't find category by id: " + id)
        );
    }
}
