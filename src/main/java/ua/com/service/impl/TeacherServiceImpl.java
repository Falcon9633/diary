package ua.com.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.dao.TeacherDAO;
import ua.com.entity.Teacher;
import ua.com.service.TeacherService;

import java.util.List;

@Service
@Transactional
public class TeacherServiceImpl implements TeacherService {

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
    public List<Teacher> findAll() {
        return teacherDAO.findAll();
    }
}
