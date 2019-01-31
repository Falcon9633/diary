package ua.com.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ua.com.entity.Band;

import java.util.Set;

public interface BandDAO extends JpaRepository<Band, Integer> {
    @Query("from Band b left join fetch b.studentsList where b.id=:id")
    Band findByIdWithStudents(@Param("id") int id);

    @Query("from Band b left join fetch b.subjectList where b.id=:id")
    Band findByIdWithSubject(@Param("id") int id);

    @Query("from Band b left join fetch b.studentsList" +
            " left join fetch b.subjectList" +
            " left join fetch b.scheduleList")
    Set<Band> findAllWithAllNested();
}
