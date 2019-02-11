package ua.com.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ua.com.entity.Student;

import java.util.List;
import java.util.Set;

public interface StudentDAO extends JpaRepository<Student, Integer> {

    @Query("from Student s where s.email=:email")
    Student findByEmail(@Param("email") String email);

    @Query("from Student s left join fetch s.band where s.id=:id")
    Student findStudentByIdWithBand(@Param("id") int id);

    @Query("from Student s left join fetch s.band")
    List<Student> findAllWithBand();

    @Query("from Student s")
    Set<Student> findAllSet();
}
