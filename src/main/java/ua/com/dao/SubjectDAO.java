package ua.com.dao;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ua.com.entity.Subject;

import java.util.List;
import java.util.Set;

public interface SubjectDAO extends JpaRepository<Subject, Integer> {
    @Query("from Subject s left join fetch s.teacherList where s.id=:id")
    Subject findByIdWithTeacher(@Param("id") int id);

    @Query("from Subject s left join fetch s.bandList")
    Set<Subject> findAllWithBand(Sort sort);

    @Query("from Subject s left join fetch s.bandList " +
            "left join fetch s.teacherList " +
            "left join fetch s.scheduleList")
    Set<Subject> findAllWithAllNested ();
}
