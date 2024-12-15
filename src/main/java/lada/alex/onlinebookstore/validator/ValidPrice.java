package lada.alex.onlinebookstore.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = PriceValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPrice {
    String message() default "toPrice must be greater than fromPrice";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
