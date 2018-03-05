package ua.com.service;

import ua.com.entity.Student;

import java.util.List;

public interface StudentService {

    void save(Student student);

    Student findOne(int id);

    List<Student> findAll();

    Student findStudentWithBand(int id);

    List<Student> findAllWithBand();

}
