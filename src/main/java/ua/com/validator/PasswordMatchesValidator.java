package ua.com.validator;

import ua.com.anotation.PasswordMatches;
import ua.com.dto.UserRegistrationDTO;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {
    @Override
    public void initialize(PasswordMatches passwordMatches) {
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        UserRegistrationDTO userRegistrationDTO = (UserRegistrationDTO) o;
        return userRegistrationDTO.getPassword().equals(userRegistrationDTO.getMatchingPassword());
    }
}
