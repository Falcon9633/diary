package ua.com.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.dao.BandDAO;
import ua.com.dao.StudentDAO;
import ua.com.dto.UserRegistrationDTO;
import ua.com.entity.Band;
import ua.com.entity.Student;
import ua.com.service.StudentService;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@Transactional
public class StudentServiceImpl implements StudentService{

    @Autowired
    private StudentDAO studentDAO;

    @Autowired
    private BandDAO bandDAO;

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
    public void save(UserRegistrationDTO userRegistrationDTO) {
        Student student = new Student();
        student.setName(userRegistrationDTO.getName());
        student.setSurname(userRegistrationDTO.getSurname());
        student.setEmail(userRegistrationDTO.getEmail());
        String password = "1";
        String encode = passwordEncoder.encode(password);
        student.setPassword(encode);
        studentDAO.save(student);
    }

    @Override
    public void saveStudentToBand(Map<String, String> requestParam) {
        Band selectedBand = bandDAO.findOne(Integer.parseInt(requestParam.get("band")));
        for (String key : requestParam.keySet()) {
            if (key.contains("user")) {
                Student student = studentDAO.findByIdWithBand(Integer.parseInt(requestParam.get(key)));
                student.setBand(selectedBand);
                studentDAO.save(student);
            }
        }
    }

    public void edit(int id, String name, String surname, String email){
        Student selectedStudent = studentDAO.findOne(id);
        if (!surname.trim().isEmpty()) {
            selectedStudent.setSurname(surname);
        }
        if (!name.trim().isEmpty()) {
            selectedStudent.setName(name);
        }
        if (!email.trim().isEmpty()) {
            selectedStudent.setEmail(email);
        }
        studentDAO.save(selectedStudent);
    }

    @Override
    public boolean passwordCheck(Principal principal, String password) {
        String encodedPassword = studentDAO.findByEmail(principal.getName()).getPassword();
        return passwordEncoder.matches(password, encodedPassword);
    }

    @Override
    public void changePassword(Principal principal, String password) {
        Student student = studentDAO.findByEmail(principal.getName());
        String encode = passwordEncoder.encode(password);
        student.setPassword(encode);
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
    public Student findByEmailWithNested(String email) {
        return studentDAO.findByEmailWithNested(email);
    }

    @Override
    public Student findByIdWithBand(int id) {
        return studentDAO.findByIdWithBand(id);
    }

    @Override
    public List<Student> findAll() {
        return studentDAO.findAll();
    }

    @Override
    public Set<Student> findAllWithBand() {
        return studentDAO.findAllWithBand();
    }

    @Override
    public Set<Student> findAllWithBand(Sort.Direction sortDirection, String property) {
        Sort.Order byProperty = new Sort.Order(sortDirection, property);
        Sort orders = new Sort(byProperty);
        return studentDAO.findAllWithBand(orders);
    }

    @Override
    public Set<Student> findSpecific(String searchForm) {
        String[] splitSearchForm = searchForm.trim().split(" ");

        if (splitSearchForm.length > 1) {
            return studentDAO.findSpecific(splitSearchForm[0], splitSearchForm[1]);

        }
        return studentDAO.findSpecific(searchForm);
    }
}
