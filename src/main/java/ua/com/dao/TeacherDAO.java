package ua.com.dao;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ua.com.entity.Teacher;

import java.util.List;
import java.util.Set;

public interface TeacherDAO extends JpaRepository<Teacher, Integer> {

    @Query("from Teacher t where t.email=:email")
    Teacher findByEmail (@Param("email") String email);

    @Query("from Teacher t left join fetch t.subjectSet")
    Set<Teacher> findAllWithSubject(Sort sort);

    @Query("from Teacher t left join fetch t.subjectSet" +
            " left join fetch t.scheduleSet")
    Set<Teacher> findAllWithAllNested();

    @Query("from Teacher t left join fetch t.subjectSet" +
            " left join fetch t.scheduleSet")
    Set<Teacher> findAllWithAllNested(Sort sort);

    @Query("from Teacher t left join fetch t.subjectSet subjectSet" +
            " where t.name=:searchForm or t.surname=:searchForm or subjectSet.name=:searchForm")
    Set<Teacher> findSpecific (@Param("searchForm") String searchForm);

    @Query("from Teacher t left join fetch t.subjectSet" +
            " where (t.name=:searchForm1 and t.surname=:searchForm2) or (t.name=:searchForm2 and t.surname=:searchForm1)")
    Set<Teacher> findSpecific (@Param("searchForm1") String searchForm1, @Param("searchForm2") String searchForm2);
}
