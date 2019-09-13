package ua.com.service;

import org.springframework.data.domain.Sort;
import ua.com.entity.Teacher;

import java.util.List;
import java.util.Set;

public interface TeacherService {
    void save(Teacher teacher);

    Teacher findOne(int id);

    Teacher findByEmail(String email);

    List<Teacher> findAll();

    Set<Teacher> findAllWithSubject(Sort sort);

    Set<Teacher> findAllWithAllNested();

    Set<Teacher> findAllWithAllNested(Sort sort);

    Set<Teacher> findSpecific(String searchForm);

    Set<Teacher> findSpecific(String searchForm1, String searchForm2);
}

