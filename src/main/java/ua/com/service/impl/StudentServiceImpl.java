package ua.com.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.dao.StudentDAO;
import ua.com.entity.Student;
import ua.com.service.StudentService;

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


}
