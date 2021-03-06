package ua.com.service;

import org.springframework.data.domain.Sort;
import ua.com.dto.UserRegistrationDTO;
import ua.com.entity.Student;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface StudentService {

    void save(Student student);

    void save(UserRegistrationDTO userRegistrationDTO);

    void saveStudentToBand(Map<String, String> requestParam);

    void edit(int id,  String name, String surname, String email);

    boolean passwordCheck (Principal principal, String password);

    void changePassword (Principal principal, String password);

    Student findOne(int id);

    Student findByEmail(String email);

    Student findByEmailWithNested(String email);

    Student findByIdWithBand(int id);

    List<Student> findAll();

    Set<Student> findAllWithBand();

    Set<Student> findAllWithBand(Sort.Direction sortDirection, String property);

    Set<Student> findSpecific(String searchForm);

}
