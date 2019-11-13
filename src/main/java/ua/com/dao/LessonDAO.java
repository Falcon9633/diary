package ua.com.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ua.com.entity.Lesson;

import java.util.List;

public interface LessonDAO extends JpaRepository<Lesson, Integer> {

    @Query("from Lesson l left join fetch l.schedule " +
            "left join fetch l.noteList " +
            "where MONTH(l.schedule.calendar)=:monthIndex")
    List<Lesson> findAllByMonth(@Param("monthIndex") int monthIndex);
}
