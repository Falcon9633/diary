package ua.com.service;


import org.springframework.data.domain.Sort;
import ua.com.entity.Subject;

import java.util.List;
import java.util.Set;

public interface SubjectService {
    void save(Subject subject);

    Subject findOne(int id);

    Subject findByIdWithTeacher(int id);

    List<Subject> findAll();

    List<Subject> findAll(Sort sort);

    Set<Subject> findAllWithBand(Sort sort);

    List<Subject> findAllWithAllNested();
}
