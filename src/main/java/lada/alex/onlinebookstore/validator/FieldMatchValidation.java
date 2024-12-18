package lada.alex.onlinebookstore.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapperImpl;

public class FieldMatchValidation implements ConstraintValidator<FieldMatch, Object> {
    private String fieldName;
    private String repeatFieldName;

    @Override
    public void initialize(FieldMatch constraintAnnotation) {
        this.fieldName = constraintAnnotation.field();
        this.repeatFieldName = constraintAnnotation.repeatField();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {
        BeanWrapperImpl beanWrapper = new BeanWrapperImpl(value);
        Object firstFieldValue = beanWrapper.getPropertyValue(fieldName);
        Object secondFieldValue = beanWrapper.getPropertyValue(repeatFieldName);

        return firstFieldValue != null && firstFieldValue.equals(secondFieldValue);
    }
}
