package ua.com.service;

import ua.com.entity.Schedule;

import java.util.Date;
import java.util.List;
import java.util.Map;


public interface ScheduleService {
    void save(Schedule schedule);

    void createSchedule(Map<String, String> requestParam);

    void editSchedule(int bandId, int weekOfYear, int dayOfWeek, Map<String, String> requestParam);

    Schedule findOne(int id);

    Schedule findByBandAndWeekOfYearAndDayOfWeekAndNumberOfLessonWithAllNested(int bandId,
                                                                               int weekOfYear,
                                                                               int dayOfWeek,
                                                                               int numberOfLesson);

    List<Schedule> findAll();

    List<Schedule> findAllByWeekOfYearWithAllNested(int weekOfYear);

    List<Schedule> findAllByBandAndWeekOfYearAndDayOfWeek(int bandId, int weekOfYear, int dayOfWeek);

    List<Schedule> findAllGraterThenOrEqualToWeekOfYear(int weekOfYear);

    void delete(Schedule schedule);
}
