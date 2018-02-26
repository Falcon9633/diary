package ua.com.service;

import ua.com.entity.Teacher;

import java.util.List;

public interface TeacherService {
    void save(Teacher teacher);
    Teacher findOne(int id);
    List<Teacher> findAll();
}

