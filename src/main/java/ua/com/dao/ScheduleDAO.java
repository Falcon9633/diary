package ua.com.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ua.com.entity.Schedule;

import java.util.Date;
import java.util.List;

public interface ScheduleDAO extends JpaRepository<Schedule, Integer> {
    @Query("from Schedule sh " +
            "left join fetch sh.band " +
            "left join fetch sh.subject " +
            "left join fetch sh.teacher " +
            "where sh.weekOfYear=:weekOfYear")
    List<Schedule> findAllByWeekOfYearWithAllNested(@Param("weekOfYear") int weekOfYear);

    @Query("from Schedule sh where sh.date>=:startDate")
    List<Schedule> findAllByDate(@Param("startDate") Date startDate);
}
