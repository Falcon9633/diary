package ua.com.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ua.com.entity.Note;

import java.util.List;

public interface NoteDAO extends JpaRepository<Note, Integer> {

    @Query("from Note n left join fetch n.student as student " +
            "left join fetch n.lesson as lesson " +
            "where student.id=:studentId")
    List<Note> findAllByStudent(@Param("studentId") int studentId);

    @Query("from Note n left join fetch n.student as student " +
            "left join fetch n.lesson as lesson " +
            "where student.id=:studentId and lesson.id in (:lessonsId)")
    List<Note> findAllByStudentAndLesson(@Param("studentId") int studentId, @Param("lessonsId") List<Integer> lessonsId);
}
