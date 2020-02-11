package ua.com.dao;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ua.com.entity.Band;

import java.util.Set;

public interface BandDAO extends JpaRepository<Band, Integer> {
    @Query("from Band b left join fetch b.studentsSet where b.id=:id")
    Band findByIdWithStudents(@Param("id") int id);

    @Query("from Band b left join fetch b.subjectSet where b.id=:id")
    Band findByIdWithSubject(@Param("id") int id);

    @Query("from Band b left join fetch b.studentsSet" +
            " left join fetch b.subjectSet" +
            " left join fetch b.scheduleSet")
    Set<Band> findAllWithAllNested();

    @Query("from Band band " +
            "left join fetch band.scheduleSet schedules " +
            "where schedules.subject.id=:subjectId and schedules.teacher.id=:teacherId")
    Set<Band> findAllBySubjectAndTeacher(@Param("subjectId") int subjectId, @Param("teacherId") int teacherId, Sort sort);
}
