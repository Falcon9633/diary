package ua.com.dao;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ua.com.entity.Teacher;

import java.util.List;
import java.util.Set;

public interface TeacherDAO extends JpaRepository<Teacher, Integer> {
    @Query("from Teacher t left join fetch t.subjectList")
    Set<Teacher> findAllWithSubject(Sort sort);

    @Query("from Teacher t left join fetch t.subjectList" +
            " left join fetch t.scheduleList")
    Set<Teacher> findAllWithAllNested();
}
