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
            "where sh.band.id=:bandId " +
            "and sh.weekOfYear=:weekOfYear " +
            "and sh.dayOfWeek=:dayOfWeek " +
            "and sh.numberOfLesson=:numberOfLesson")
    Schedule findByBandAndWeekOfYearAndDayOfWeekAndNumberOfLessonWithAllNested(@Param("bandId") int bandId,
                                                                               @Param("weekOfYear") int weekOfYear,
                                                                               @Param("dayOfWeek") int dayOfWeek,
                                                                               @Param("numberOfLesson") int numberOfLesson);

    @Query("from Schedule sh " +
            "left join fetch sh.band " +
            "left join fetch sh.subject " +
            "left join fetch sh.teacher " +
            "where sh.weekOfYear=:weekOfYear")
    List<Schedule> findAllByWeekOfYearWithAllNested(@Param("weekOfYear") int weekOfYear);

    @Query("from Schedule sh " +
            "left join fetch sh.band " +
            "left join fetch sh.subject " +
            "left join fetch sh.teacher " +
            "where sh.band.id=:bandId " +
            "and sh.weekOfYear=:weekOfYear " +
            "and sh.dayOfWeek=:dayOfWeek")
    List<Schedule> findAllByBandAndWeekOfYearAndDayOfWeek(@Param("bandId") int bandId,
                                                          @Param("weekOfYear") int weekOfYear,
                                                          @Param("dayOfWeek") int dayOfWeek);

    @Query("from Schedule sh where sh.weekOfYear>=:weekOfYear")
    List<Schedule> findAllGraterThenOrEqualToWeekOfYear(@Param("weekOfYear") int weekOfYear);
}
