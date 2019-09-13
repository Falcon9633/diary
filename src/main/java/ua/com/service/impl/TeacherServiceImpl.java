package ua.com.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.dao.TeacherDAO;
import ua.com.entity.Teacher;
import ua.com.service.TeacherService;

import java.util.List;
import java.util.Set;

@Service
@Transactional
public class TeacherServiceImpl implements TeacherService{

    @Autowired
    private TeacherDAO teacherDAO;

    @Override
    public void save(Teacher teacher) {
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
    public Set<Teacher> findAllWithSubject(Sort sort) {
        return teacherDAO.findAllWithSubject(sort);
    }

    @Override
    public Set<Teacher> findAllWithAllNested() {
        return teacherDAO.findAllWithAllNested();
    }

    @Override
    public Set<Teacher> findAllWithAllNested(Sort sort) {
        return teacherDAO.findAllWithAllNested(sort);
    }

    @Override
    public Set<Teacher> findSpecific(String searchForm) {
        return teacherDAO.findSpecific(searchForm);
    }

    @Override
    public Set<Teacher> findSpecific(String searchForm1, String searchForm2) {
        return teacherDAO.findSpecific(searchForm1, searchForm2);
    }
}
