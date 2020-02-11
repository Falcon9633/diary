package ua.com.dao;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.method.P;
import ua.com.entity.Schedule;

import java.util.GregorianCalendar;
import java.util.List;

public interface ScheduleDAO extends JpaRepository<Schedule, Integer> {

    @Query("from Schedule sh " +
            "left join fetch sh.band " +
            "left join fetch sh.subject " +
            "left join fetch sh.teacher " +
            "left join fetch sh.lesson " +
            "where sh.band.id=:bandId " +
            "and sh.weekOfYear=:weekOfYear " +
            "and sh.dayOfWeek=:dayOfWeek " +
            "and sh.numberOfLesson=:numberOfLesson")
    Schedule findByBandAndWeekOfYearAndDayOfWeekAndNumberOfLesson(@Param("bandId") int bandId,
                                                                  @Param("weekOfYear") int weekOfYear,
                                                                  @Param("dayOfWeek") int dayOfWeek,
                                                                  @Param("numberOfLesson") int numberOfLesson);

    @Query("from Schedule sh " +
            "left join fetch sh.band " +
            "left join fetch sh.subject " +
            "left join fetch sh.teacher " +
            "left join fetch sh.lesson " +
            "where sh.weekOfYear=:weekOfYear")
    List<Schedule> findAllByWeekOfYear(@Param("weekOfYear") int weekOfYear);

    @Query("from Schedule sh " +
            "left join fetch sh.band " +
            "left join fetch sh.subject " +
            "left join fetch sh.teacher " +
            "left join fetch sh.lesson " +
            "where sh.band.id=:bandId " +
            "and sh.weekOfYear=:weekOfYear " +
            "and sh.dayOfWeek=:dayOfWeek")
    List<Schedule> findAllByBandAndWeekOfYearAndDayOfWeek(@Param("bandId") int bandId,
                                                          @Param("weekOfYear") int weekOfYear,
                                                          @Param("dayOfWeek") int dayOfWeek);

    @Query("from Schedule sh " +
            "left join fetch sh.band " +
            "left join fetch sh.subject " +
            "left join fetch sh.teacher " +
            "left join fetch sh.lesson " +
            "where sh.calendar BETWEEN :beginningDate and :endingDate")
    List<Schedule> findAllBetweenBeginningAndEndingDate(@Param("beginningDate") GregorianCalendar beginningDate,
                                                        @Param("endingDate") GregorianCalendar endingDate);

    @Query("from Schedule sh " +
            "left join fetch sh.band " +
            "left join fetch sh.subject " +
            "left join fetch sh.teacher " +
            "left join fetch sh.lesson " +
            "where sh.calendar BETWEEN :beginningDate and :endingDate and sh.band.id = :bandId")
    List<Schedule> findAllBetweenBeginningAndEndingDateAndBand(@Param("beginningDate") GregorianCalendar beginningDate,
                                                               @Param("endingDate") GregorianCalendar endingDate,
                                                               @Param("bandId") int bandId);

    @Query("from Schedule sh " +
            "left join fetch sh.band " +
            "left join fetch sh.subject " +
            "left join fetch sh.teacher " +
            "left join fetch sh.lesson " +
            "where sh.calendar BETWEEN :beginningDate and :endingDate and sh.teacher.id = :teacherId")
    List<Schedule> findAllBetweenBeginningAndEndingDateAndTeacher(@Param("beginningDate") GregorianCalendar beginningDate,
                                                                  @Param("endingDate") GregorianCalendar endingDate,
                                                                  @Param("teacherId") int teacherId);

    @Query("from Schedule sh left join fetch sh.lesson " +
            "where ((sh.weekOfYear>=:weekOfYear and sh.year=:year) or sh.year>:year) and sh.band.id=:bandId")
    List<Schedule> findAllByBandAndGraterThenOrEqualToWeekOfYearAndGraterThenOrEqualToYear(@Param("year") int currentYear,
                                                                                           @Param("weekOfYear") int weekOfYear,
                                                                                           @Param("bandId") int bandId);

    @Query("from Schedule sh " +
            "left join fetch sh.band as band " +
            "left join fetch sh.subject " +
            "left join fetch sh.teacher " +
            "left join fetch sh.lesson " +
            "where sh.teacher.id in (:teachersId) " +
            "and sh.subject.id=:subjectId " +
            "and sh.band.id=:bandId " +
            "and MONTH(sh.calendar)=:monthIndex")
    List<Schedule> findAllByTeacherAndSubjectAndBandAndMonth(@Param("teachersId") List<Integer> teachers,
                                                             @Param("subjectId") int subjectId,
                                                             @Param("bandId") int bandId,
                                                             @Param("monthIndex") int monthIndex,
                                                             Sort sort);
}
