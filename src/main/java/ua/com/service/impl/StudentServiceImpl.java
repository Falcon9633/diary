package ua.com.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.dao.StudentDAO;
import ua.com.entity.Student;
import ua.com.service.StudentService;

import java.util.List;
import java.util.Set;

@Service
@Transactional
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentDAO studentDAO;


    @Override
    public void save(Student student) {
        studentDAO.save(student);
    }

    @Override
    public Student findOne(int id) {
        return studentDAO.findOne(id);
    }

    @Override
    public List<Student> findAll() {
        return studentDAO.findAll();
    }

    @Override
    public Set<Student> findAllSet() {
        return studentDAO.findAllSet();
    }

    @Override
    public Student findStudentWithBand(int id) {
        return studentDAO.findStudentWithBand(id);
    }

    @Override
    public List<Student> findAllWithBand() {
        return studentDAO.findAllWithBand();
    }


}
