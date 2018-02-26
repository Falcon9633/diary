package ua.com.service;

import ua.com.entity.Subject;

import java.util.List;

public interface SubjectService {
    void save(Subject subject);
    Subject findOne(int id);
    List<Subject> findAll();
}
