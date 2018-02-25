package ua.com.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.dao.SubjectDAO;
import ua.com.entity.Subject;
import ua.com.service.SubjectService;

@Service
@Transactional
public class SubjectServiceImpl implements SubjectService {

    @Autowired
    private SubjectDAO subjectDAO;

    @Override
    public void save(Subject subject) {
        subjectDAO.save(subject);
    }
}
