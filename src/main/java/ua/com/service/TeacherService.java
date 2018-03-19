package ua.com.service;

import org.springframework.data.domain.Sort;
import ua.com.entity.Teacher;

import java.util.List;
import java.util.Set;

public interface TeacherService {
    void save(Teacher teacher);

    Teacher findOne(int id);

    List<Teacher> findAll();

    Set<Teacher> findAllWithSubject(Sort sort);
}

