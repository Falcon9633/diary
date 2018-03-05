package ua.com.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ua.com.entity.Band;

public interface BandDAO extends JpaRepository<Band, Integer> {
    @Query("from Band b left join fetch b.subjectList where b.id=:id")
    Band findByIdWithSubject(@Param("id") int id);
}
