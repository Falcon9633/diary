package ua.com.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ua.com.entity.Band;

@Component
public class BandValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(Band.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Band band = (Band) o;
        if (band.getName().isEmpty()){
            errors.rejectValue("name", "emptyBandNameError", "Заповніть назву");
        }
    }
}
