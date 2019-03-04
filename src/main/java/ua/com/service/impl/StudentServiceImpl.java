package ua.com.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.dao.StudentDAO;
import ua.com.entity.Student;
import ua.com.service.StudentService;

import java.util.List;
import java.util.Set;

@Service
@Transactional
public class StudentServiceImpl implements StudentService{

    @Autowired
    private StudentDAO studentDAO;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void save(Student student) {
//        String password = "1";
//        String encode = passwordEncoder.encode(password);
//        student.setPassword(encode);
        studentDAO.save(student);
    }

    @Override
    public Student findOne(int id) {
        return studentDAO.findOne(id);
    }

    @Override
    public Student findByEmail(String email) {
        return studentDAO.findByEmail(email);
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
    public Student findStudentByIdWithBand(int id) {
        return studentDAO.findStudentByIdWithBand(id);
    }

    @Override
    public List<Student> findAllWithBand() {
        return studentDAO.findAllWithBand();
    }
}
