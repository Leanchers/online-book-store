package lada.alex.onlinebookstore.mapper;

import lada.alex.onlinebookstore.config.MapperConfig;
import lada.alex.onlinebookstore.dto.category.CategoryDto;
import lada.alex.onlinebookstore.dto.category.CreateCategoryRequestDto;
import lada.alex.onlinebookstore.dto.category.UpdateCategoryRequestDto;
import lada.alex.onlinebookstore.model.Category;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(config = MapperConfig.class)
public interface CategoryMapper {
    CategoryDto toDto(Category category);

    Category toModel(CreateCategoryRequestDto requestDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateModel(@MappingTarget Category category, UpdateCategoryRequestDto requestDto);
}
