package ua.com.service;

import ua.com.entity.Student;

import java.util.List;
import java.util.Set;

public interface StudentService {

    void save(Student student);

    Student findOne(int id);

    List<Student> findAll();

    Set<Student> findAllSet();

    Student findStudentWithBand(int id);

    List<Student> findAllWithBand();

}
