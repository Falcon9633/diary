package ua.com.service;

import org.springframework.data.domain.Sort;
import ua.com.dto.UserRegistrationDTO;
import ua.com.entity.Band;
import ua.com.entity.Subject;
import ua.com.entity.Teacher;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface TeacherService {
    void save(Teacher teacher);

    void save(UserRegistrationDTO userRegistrationDTO);

    void edit(int id, String name, String surname, String email, boolean isAdmin);

    Teacher findOne(int id);

    Teacher findByEmail(String email);

    List<Teacher> findAll();

    List<Teacher> findAll(Sort.Direction sortDirection, String property);

    Set<Teacher> findAllWithSubject(Sort.Direction sortDirection, String property);

    Set<Teacher> findAllWithAllNested();

    Set<Teacher> findAllWithAllNested(Sort.Direction sortDirection, String property);

    Set<Teacher> findAllBySubjectAndBand(int subjectId, int bandId);

    Set<Teacher> findSpecific(String searchForm);

    Map<Subject, Set<Band>> journalNavigationSubjectAndBand(Principal principal);
}

