package ua.com.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ua.com.entity.Subject;

@Component
public class SubjectValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(Subject.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Subject subject = (Subject) o;
        if (subject.getName().isEmpty()){
            errors.rejectValue("name", "emptySubjectNameError", "Заповніть назву");
        }
    }
}
