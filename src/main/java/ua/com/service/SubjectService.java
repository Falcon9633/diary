package ua.com.service;


import org.springframework.data.domain.Sort;
import ua.com.entity.Subject;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface SubjectService {
    void save(Subject subject);

    void saveTeacherToSubject(int subjectId, Map<String, String> requestParam);

    Subject findOne(int id);

    Subject findByIdWithTeacher(int id);

    List<Subject> findAll();

    List<Subject> findAll(Sort.Direction sortDirection, String property);

    Set<Subject> findAllWithBand(Sort.Direction sortDirection, String property);

    Set<Subject> findAllWithAllNested();
}
