package ua.com.service;

import ua.com.entity.Band;
import ua.com.entity.Schedule;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;


public interface ScheduleService {
    void save(Schedule schedule);

    void createSchedule(Map<String, String> requestParam);

    void editSchedule(int bandId, int weekOfYear, int dayOfWeek, Map<String, String> requestParam);

    List<Schedule> journalSchedules(int subjectId, int bandId, int monthIndex);

    Schedule findOne(int id);

    Schedule findByBandAndWeekOfYearAndDayOfWeekAndNumberOfLessonWithAllNested(int bandId,
                                                                               int weekOfYear,
                                                                               int dayOfWeek,
                                                                               int numberOfLesson);

    List<Schedule> findAll();

    List<Schedule> findAllByWeekOfYearWithAllNested(int weekOfYear);

    List<Schedule> findAllByBandAndWeekOfYearAndDayOfWeek(int bandId, int weekOfYear, int dayOfWeek);

//    Set<Band> findAllBandBySubjectAndTeacher

    void delete(Schedule schedule);
}
