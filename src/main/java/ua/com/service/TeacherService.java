package ua.com.service;

import org.springframework.data.domain.Sort;
import ua.com.dto.UserRegistrationDTO;
import ua.com.entity.Teacher;

import java.util.List;
import java.util.Set;

public interface TeacherService {
    void save(Teacher teacher);

    void save (UserRegistrationDTO userRegistrationDTO);

    void edit(int id, String name, String surname, String email, boolean isAdmin);

    Teacher findOne(int id);

    Teacher findByEmail(String email);

    List<Teacher> findAll();

    Set<Teacher> findAllWithSubject(Sort sort);

    Set<Teacher> findAllWithAllNested();

    Set<Teacher> findAllWithAllNested(Sort sort);

    Set<Teacher> findSpecific(String searchForm);
}

