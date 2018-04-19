package ua.com.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.dao.SubjectDAO;
import ua.com.entity.Subject;
import ua.com.service.SubjectService;

import java.util.List;
import java.util.Set;

@Service
@Transactional
public class SubjectServiceImpl implements SubjectService {

    @Autowired
    private SubjectDAO subjectDAO;

    @Override
    public void save(Subject subject) {
        subjectDAO.save(subject);
    }

    @Override
    public Subject findOne(int id) {
        return subjectDAO.findOne(id);
    }

    @Override
    public Subject findByIdWithTeacher(int id) {
        return subjectDAO.findByIdWithTeacher(id);
    }

    @Override
    public List<Subject> findAll() {
        return subjectDAO.findAll();
    }

    @Override
    public List<Subject> findAll(Sort sort) {
        return subjectDAO.findAll(sort);
    }

    @Override
    public Set<Subject> findAllWithBand(Sort sort) {
        return subjectDAO.findAllWithBand(sort);
    }

    @Override
    public List<Subject> findAllWithAllNested() {
        return subjectDAO.findAllWithAllNested();
    }
}
