package ua.com.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.dao.BandDAO;
import ua.com.dao.TeacherDAO;
import ua.com.dto.UserRegistrationDTO;
import ua.com.entity.Authority;
import ua.com.entity.Band;
import ua.com.entity.Subject;
import ua.com.entity.Teacher;
import ua.com.service.TeacherService;

import java.security.Principal;
import java.util.*;

@Service
@Transactional
public class TeacherServiceImpl implements TeacherService{

    @Autowired
    private TeacherDAO teacherDAO;

    @Autowired
    private BandDAO bandDAO;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void save(Teacher teacher) {
        teacherDAO.save(teacher);
    }

    @Override
    public void save(UserRegistrationDTO userRegistrationDTO) {
        Teacher teacher = new Teacher();
        teacher.setName(userRegistrationDTO.getName());
        teacher.setSurname(userRegistrationDTO.getSurname());
        teacher.setEmail(userRegistrationDTO.getEmail());
        String password = "1";
        String encode = passwordEncoder.encode(password);
        teacher.setPassword(encode);
        teacherDAO.save(teacher);
    }

    @Override
    public void edit(int id, String name,  String surname, String email, boolean isAdmin) {
        Teacher selectedTeacher = teacherDAO.findOne(id);
        if (!surname.trim().isEmpty()) {
            selectedTeacher.setSurname(surname);
        }
        if (!name.trim().isEmpty()) {
            selectedTeacher.setName(name);
        }
        if (!email.trim().isEmpty()) {
            selectedTeacher.setEmail(email);
        }
        if (isAdmin){
            selectedTeacher.setAuthority(Authority.ROLE_ADMIN);
        } else {
            selectedTeacher.setAuthority(Authority.ROLE_TEACHER);
        }
        teacherDAO.save(selectedTeacher);
    }

    @Override
    public boolean passwordCheck(Principal principal, String password) {
        String encodedPassword = teacherDAO.findByEmail(principal.getName()).getPassword();
        return passwordEncoder.matches(password, encodedPassword);
    }

    @Override
    public void changePassword(String password, Principal principal) {
        Teacher teacher = teacherDAO.findByEmail(principal.getName());
        String encode = passwordEncoder.encode(password);
        teacher.setPassword(encode);
        teacherDAO.save(teacher);
    }

    @Override
    public Teacher findOne(int id) {
        return teacherDAO.findOne(id);
    }

    @Override
    public Teacher findByEmail(String email) {
        return teacherDAO.findByEmail(email);
    }

    @Override
    public List<Teacher> findAll() {
        return teacherDAO.findAll();
    }

    @Override
    public List<Teacher> findAll(Sort.Direction sortDirection, String property) {
        Sort.Order byProperty = new Sort.Order(sortDirection, property);
        Sort orders = new Sort(byProperty);
        return teacherDAO.findAll(orders);
    }

    @Override
    public Set<Teacher> findAllWithSubject(Sort.Direction sortDirection, String property) {
        Sort.Order byProperty = new Sort.Order(sortDirection, property);
        Sort orders = new Sort(byProperty);
        return teacherDAO.findAllWithSubject(orders);
    }

    @Override
    public Set<Teacher> findAllWithAllNested() {
        return teacherDAO.findAllWithAllNested();
    }

    @Override
    public Set<Teacher> findAllWithAllNested(Sort.Direction sortDirection, String property) {
        Sort.Order byProperty = new Sort.Order(sortDirection, property);
        Sort orders = new Sort(byProperty);
        return teacherDAO.findAllWithAllNested(orders);
    }

    @Override
    public Set<Teacher> findAllBySubjectAndBand(int subjectId, int bandId) {
        return teacherDAO.findAllBySubjectAndBand(subjectId, bandId);
    }

    @Override
    public Set<Teacher> findSpecific(String searchForm) {
        String[] splitSearchForm = searchForm.trim().split(" ");

        if (splitSearchForm.length > 1) {
            return teacherDAO.findSpecific(splitSearchForm[0], splitSearchForm[1]);

        }
        return teacherDAO.findSpecific(searchForm);
    }

    @Override
    public Map<Subject, Set<Band>> journalNavigationSubjectAndBand(Principal principal) {
        Sort.Order sortByNameAsc = new Sort.Order(Sort.Direction.ASC, "name");
        Sort orders = new Sort(sortByNameAsc);
        Map<Subject, Set<Band>> journalNavigationSubjectAndBand = new LinkedHashMap<>();
        Teacher selectedTeacher = teacherDAO.findByEmailWithSubject(principal.getName());
        Set<Subject> subjectSet = selectedTeacher.getSubjectSet();
        for (Subject subject : subjectSet) {
            Set<Band> bandSet = bandDAO.findAllBySubjectAndTeacher(subject.getId(), selectedTeacher.getId(), orders);
            journalNavigationSubjectAndBand.put(subject, bandSet);
        }
        return journalNavigationSubjectAndBand;
    }
}
