package ua.com.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.dao.BandDAO;
import ua.com.dao.ScheduleDAO;
import ua.com.dao.SubjectDAO;
import ua.com.dao.TeacherDAO;
import ua.com.entity.Schedule;
import ua.com.service.ScheduleService;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ScheduleServiceImpl implements ScheduleService {

    @Autowired
    private ScheduleDAO scheduleDAO;

    @Autowired
    private BandDAO bandDAO;

    @Autowired
    private SubjectDAO subjectDAO;

    @Autowired
    private TeacherDAO teacherDAO;

    @Override
    public void save(Schedule schedule) {
        scheduleDAO.save(schedule);
    }

    @Override
    public void createSchedule(Map<String, String> requestParam) {
        final int MIN_DAY_NR = 2;
        final int MAX_DAY_NR = 7;
        final int MIN_LESSON_NR = 0;
        final int MAX_LESSON_NR = 8;
        GregorianCalendar calendar = new GregorianCalendar();
        Date currentTime = calendar.getTime();
        int currentWeekOfYear = calendar.get(GregorianCalendar.WEEK_OF_YEAR);
        List<Schedule> oldSchedule = scheduleDAO.findAllGraterThenOrEqualToWeekOfYear(currentWeekOfYear);

        scheduleDAO.delete(oldSchedule);

        while (calendar.get(GregorianCalendar.MONTH) < 5 || calendar.get(GregorianCalendar.MONTH) > 7) {
            for (int dayOfWeek = MIN_DAY_NR; dayOfWeek <= MAX_DAY_NR; dayOfWeek++) {
                if (calendar.get(GregorianCalendar.DAY_OF_WEEK) == dayOfWeek) {
                    for (int numberOfLesson = MIN_LESSON_NR; numberOfLesson <= MAX_LESSON_NR; numberOfLesson++) {
                        Schedule schedule = new Schedule();
                        schedule.setEditingDate(currentTime);
                        schedule.setWeekOfYear(calendar.get(GregorianCalendar.WEEK_OF_YEAR));
                        schedule.setDayOfWeek(dayOfWeek);
                        schedule.setNumberOfLesson(numberOfLesson);
//                        scheduleDAO.save(schedule);
                        try {
                            schedule.setBand(bandDAO
                                    .findOne(Integer.parseInt(
                                            requestParam.get("bandId")
                                    )));
                        } catch (NumberFormatException ex) {
                            ex.printStackTrace();
                        }
                        try {
                            schedule.setSubject(subjectDAO
                                    .findOne(Integer.parseInt(
                                            requestParam.get("subject_" + dayOfWeek + "_" + numberOfLesson)
                                    )));
                        } catch (NumberFormatException ex) {
                            schedule.setSubject(null);
                        }
                        try {
                            schedule.setTeacher(teacherDAO
                                    .findOne(Integer.parseInt(
                                            requestParam.get("teacher_" + dayOfWeek + "_" + numberOfLesson)
                                    )));
                        } catch (NumberFormatException ex) {
                            schedule.setTeacher(null);
                        }
                        scheduleDAO.save(schedule);
                    }
                }
            }
            calendar.add(GregorianCalendar.DAY_OF_MONTH, 1); // day increment
        }
    }

    @Override
    public void editSchedule(int bandId, int weekOfYear, int dayOfWeek, Map<String, String> requestParam) {
        final int MIN_LESSON_NR = 0;
        final int MAX_LESSON_NR = 8;
        GregorianCalendar calendar = new GregorianCalendar();
        Date currentTime = calendar.getTime();
        for (int numberOfLesson = MIN_LESSON_NR; numberOfLesson <= MAX_LESSON_NR; numberOfLesson++) {
            Schedule currentLessonSchedule = scheduleDAO.findByBandAndWeekOfYearAndDayOfWeekAndNumberOfLessonWithAllNested(
                    bandId,
                    weekOfYear,
                    dayOfWeek,
                    numberOfLesson
            );
            currentLessonSchedule.setEditingDate(currentTime);
            try {
                currentLessonSchedule.setSubject(subjectDAO
                        .findOne(Integer.parseInt(
                                requestParam.get("subject_" + numberOfLesson)
                        )));
            } catch (NumberFormatException ex) {
                currentLessonSchedule.setSubject(null);
            }
            try {
                currentLessonSchedule.setTeacher(teacherDAO
                        .findOne(Integer.parseInt(
                                requestParam.get("teacher_" + numberOfLesson)
                        )));
            } catch (NumberFormatException ex) {
                currentLessonSchedule.setTeacher(null);
            }
            scheduleDAO.save(currentLessonSchedule);
        }
    }

    @Override
    public Schedule findOne(int id) {
        return scheduleDAO.findOne(id);
    }

    @Override
    public Schedule findByBandAndWeekOfYearAndDayOfWeekAndNumberOfLessonWithAllNested(int bandId, int weekOfYear, int dayOfWeek, int numberOfLesson) {
        return scheduleDAO.findByBandAndWeekOfYearAndDayOfWeekAndNumberOfLessonWithAllNested(bandId, weekOfYear, dayOfWeek, numberOfLesson);
    }

    @Override
    public List<Schedule> findAll() {
        return scheduleDAO.findAll();
    }

    @Override
    public List<Schedule> findAllByWeekOfYearWithAllNested(int weekOfYear) {
        return scheduleDAO.findAllByWeekOfYearWithAllNested(weekOfYear);
    }

    @Override
    public List<Schedule> findAllByBandAndWeekOfYearAndDayOfWeek(int bandId, int weekOfYear, int dayOfWeek) {
        return scheduleDAO.findAllByBandAndWeekOfYearAndDayOfWeek(bandId, weekOfYear,dayOfWeek);
    }

    @Override
    public List<Schedule> findAllGraterThenOrEqualToWeekOfYear(int weekOfYear) {
        return scheduleDAO.findAllGraterThenOrEqualToWeekOfYear(weekOfYear);
    }

    @Override
    public void delete(Schedule schedule) {
        scheduleDAO.delete(schedule);
    }
}
