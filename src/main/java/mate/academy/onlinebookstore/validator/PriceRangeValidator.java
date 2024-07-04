package mate.academy.onlinebookstore.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import mate.academy.onlinebookstore.dto.BookSearchParametersDto;

public class PriceRangeValidator implements ConstraintValidator<ValidPriceRange,
            BookSearchParametersDto> {

    @Override
    public void initialize(ValidPriceRange constraintAnnotation) {
    }

    @Override
    public boolean isValid(BookSearchParametersDto searchParametersDto,
            ConstraintValidatorContext constraintValidatorContext) {
        if (searchParametersDto.fromPrice() == null || searchParametersDto.toPrice() == null) {
            return true;
        }
        return searchParametersDto.toPrice().compareTo(searchParametersDto.fromPrice()) >= 0;
    }
}
