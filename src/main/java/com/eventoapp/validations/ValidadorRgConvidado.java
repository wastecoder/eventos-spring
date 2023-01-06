package com.eventoapp.validations;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidadorRgConvidado implements ConstraintValidator<RgConvidado, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        String regex = "([0-9]{2}[.]?[0-9]{3}[.]?[0-9]{3}-[0-9])|([0-9]{9})"; //12.345.678-9 ou 123456789 é válido
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }
}
