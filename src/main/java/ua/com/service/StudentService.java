package ua.com.service;

import org.springframework.data.domain.Sort;
import ua.com.dto.UserRegistrationDTO;
import ua.com.entity.Student;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface StudentService {

    void save(Student student);

    void save(UserRegistrationDTO userRegistrationDTO);

    void saveStudentToBand(Map<String, String> requestParam);

    void edit(int id,  String name, String surname, String email);

    Student findOne(int id);

    Student findByEmail(String email);

    List<Student> findAll();

    Student findStudentByIdWithBand(int id);

    Set<Student> findAllWithBand();

    Set<Student> findAllWithBand(Sort sort);

    Set<Student> findSpecific(String searchForm);

}
