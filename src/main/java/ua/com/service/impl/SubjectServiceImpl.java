package ua.com.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.dao.SubjectDAO;
import ua.com.dao.TeacherDAO;
import ua.com.entity.Subject;
import ua.com.entity.Teacher;
import ua.com.service.SubjectService;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@Transactional
public class SubjectServiceImpl implements SubjectService {

    @Autowired
    private SubjectDAO subjectDAO;

    @Autowired
    private TeacherDAO teacherDAO;

    @Override
    public void save(Subject subject) {
        subjectDAO.save(subject);
    }

    @Override
    public void saveTeacherToSubject(int subjectId, Map<String, String> requestParam) {
        Subject selectedSubject = subjectDAO.findByIdWithTeacher(subjectId);
        Set<Teacher> teacherSet = selectedSubject.getTeacherSet();

        for (String key : requestParam.keySet()) {
            if (key.contains("teacherId-")) {
                Teacher teacher = teacherDAO.findOne(Integer.parseInt(requestParam.get(key)));
                teacherSet.add(teacher);
            }
        }
        selectedSubject.setTeacherSet(teacherSet);
        subjectDAO.save(selectedSubject);
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
    public Set<Subject> findAllWithAllNested() {
        return subjectDAO.findAllWithAllNested();
    }
}
