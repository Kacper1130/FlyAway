package FlyAway.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = PhoneNumberValidator.class)
@Target(FIELD)
@Retention(RUNTIME)
public @interface PhoneNumber {

    String message() default "Phone Number must be valid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
