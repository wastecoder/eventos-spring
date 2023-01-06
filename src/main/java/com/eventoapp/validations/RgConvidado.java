package com.eventoapp.validations;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = ValidadorRgConvidado.class)
public @interface RgConvidado {

    String message() default "RG inválido";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
