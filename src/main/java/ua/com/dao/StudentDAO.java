package ua.com.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ua.com.entity.Student;

import java.util.List;

public interface StudentDAO extends JpaRepository<Student, Integer> {

    @Query("from Student students left join fetch students.band where students.id=:id")
    Student findStudentWithBand(@Param("id") int id);

    @Query("from Student students left join fetch students.band")
    List<Student> findAllWithBand();

}
