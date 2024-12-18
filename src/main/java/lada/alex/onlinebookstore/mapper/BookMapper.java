package lada.alex.onlinebookstore.mapper;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lada.alex.onlinebookstore.config.MapperConfig;
import lada.alex.onlinebookstore.dto.book.BookDto;
import lada.alex.onlinebookstore.dto.book.BookDtoWithoutCategoryIds;
import lada.alex.onlinebookstore.dto.book.CreateBookRequestDto;
import lada.alex.onlinebookstore.dto.book.UpdateBookRequestDto;
import lada.alex.onlinebookstore.model.Book;
import lada.alex.onlinebookstore.model.Category;
import org.mapstruct.AfterMapping;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    @Mapping(target = "categoryIds", ignore = true)
    BookDto toDto(Book book);

    @AfterMapping
    default void setCategoryId(@MappingTarget BookDto bookDto, Book book) {
        bookDto.setCategoryIds(book.getCategories().stream()
                .map(Category::getId)
                .collect(Collectors.toSet()));
    }

    BookDtoWithoutCategoryIds toDtoWithoutCategories(Book book);

    @Mapping(target = "categories", ignore = true)
    Book toModel(CreateBookRequestDto requestDto);

    @Mapping(target = "categories", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateModel(@MappingTarget Book book, UpdateBookRequestDto requestDto);

    @AfterMapping
    default void setCategories(@MappingTarget Book book, CreateBookRequestDto requestDto) {
        if (requestDto.getCategoryIds() != null) {
            Set<Category> categories = requestDto.getCategoryIds()
                    .stream()
                    .map(Category::new)
                    .collect(Collectors.toSet());
            book.setCategories(categories);
        }
    }

    @AfterMapping
    default void setCategories(@MappingTarget Book book, UpdateBookRequestDto requestDto) {
        if (requestDto.getCategoryIds() != null) {
            Set<Category> categories = requestDto.getCategoryIds()
                        .stream()
                        .map(Category::new)
                        .collect(Collectors.toSet());
            book.setCategories(categories);
        }
    }

    @Named("bookFromId")
    default Book bookFromId(Long id) {
        return Optional.ofNullable(id)
            .map(Book::new)
            .orElse(null);
    }
}
