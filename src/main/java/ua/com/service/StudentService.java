package ua.com.service;

import org.springframework.data.domain.Sort;
import ua.com.entity.Student;

import java.util.List;
import java.util.Set;

public interface StudentService {

    void save(Student student);

    Student findOne(int id);

    Student findByEmail(String email);

    List<Student> findAll();

    Student findStudentByIdWithBand(int id);

    Set<Student> findAllWithBand();

    Set<Student> findAllWithBand(Sort sort);

    Set<Student> findSpecific(String searchForm);

    Set<Student> findSpecific(String searchForm1, String searchForm2);

}
