package ua.com.dao;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ua.com.entity.Student;

import java.util.List;
import java.util.Set;

public interface StudentDAO extends JpaRepository<Student, Integer> {

    @Query("from Student s where s.email=:email")
    Student findByEmail(@Param("email") String email);

    @Query("from Student s " +
            "left join fetch s.band " +
            "left join fetch s.noteList " +
            "where s.email=:email")
    Student findByEmailWithNested(@Param("email") String email);

    @Query("from Student s left join fetch s.band where s.id=:id")
    Student findByIdWithBand(@Param("id") int id);

    @Query("from Student s left join fetch s.band")
    Set<Student> findAllWithBand();

    @Query("from Student s left join fetch s.band")
    Set<Student> findAllWithBand(Sort sort);

    @Query("from Student s left join fetch s.band bands" +
            " where s.name=:searchForm or s.surname=:searchForm or bands.name=:searchForm")
    Set<Student> findSpecific (@Param("searchForm") String searchForm);

    @Query("from Student s left join fetch s.band" +
            " where (s.name=:searchForm1 and s.surname=:searchForm2) or (s.name=:searchForm2 and s.surname=:searchForm1)")
    Set<Student> findSpecific (@Param("searchForm1") String searchForm1, @Param("searchForm2") String searchForm2);
}
