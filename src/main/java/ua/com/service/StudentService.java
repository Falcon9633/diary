package ua.com.service;

import ua.com.entity.Student;

import java.util.List;
import java.util.Set;

public interface StudentService {

    void save(Student student);

    Student findOne(int id);

    Student findByEmail(String email);

    List<Student> findAll();

    Set<Student> findAllSet();

    Student findStudentByIdWithBand(int id);

    List<Student> findAllWithBand();

}
