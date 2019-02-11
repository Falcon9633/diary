package ua.com.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ua.com.dto.UserRegistrationDTO;
import ua.com.entity.Student;
import ua.com.entity.Teacher;
import ua.com.service.StudentService;
import ua.com.service.TeacherService;

@Component
public class UserRegistrationValidator implements Validator {

    @Autowired
    private StudentService studentService;

    @Autowired
    private TeacherService teacherService;

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(UserRegistrationDTO.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        UserRegistrationDTO userRegistrationDTO = (UserRegistrationDTO) o;
        if (emailExist(userRegistrationDTO.getEmail())){
            errors.rejectValue("email", "email is already exist", "Така електронна адреса вже використовується");
        }
    }

    public boolean emailExist(String email){
        Student student = studentService.findByEmail(email);
        Teacher teacher = teacherService.findByEmail(email);

        if (student != null || teacher != null){
            return true;
        }

        return false;
    }
}
