package ru.clevertec.ecl.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = PatchValidator.class)
@Target( { ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPatch {
    String message() default "Invalid patch";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
