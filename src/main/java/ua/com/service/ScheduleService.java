package ua.com.service;

import org.springframework.security.core.Authentication;
import ua.com.entity.Band;
import ua.com.entity.Schedule;

import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;


public interface ScheduleService {
    void save(Schedule schedule);

    void createSchedule(Map<String, String> requestParam);

    void editSchedule(int bandId, int weekOfYear, int dayOfWeek, Map<String, String> requestParam);

    List<Schedule> journalSchedules(int subjectId, int bandId, int monthIndex);

    List<Schedule> getUsersScheduleCurrentTwoWeeks(Principal principal, Authentication authentication);

    List<Schedule> getScheduleWeekBySelectedDay(long millis);

    List<Schedule> getScheduleWeekBySelectedDayAndBand(long millis, int bandId);

    Schedule findOne(int id);

    Schedule findByBandAndWeekOfYearAndDayOfWeekAndNumberOfLesson(int bandId,
                                                                  int weekOfYear,
                                                                  int dayOfWeek,
                                                                  int numberOfLesson);

    List<Schedule> findAll();

    List<Schedule> findAllByWeekOfYearWithAllNested(int weekOfYear);

    List<Schedule> findAllByBandAndWeekOfYearAndDayOfWeek(int bandId, int weekOfYear, int dayOfWeek);

    void delete(Schedule schedule);
}
