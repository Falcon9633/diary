package ua.com.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.dao.BandDAO;
import ua.com.dao.ScheduleDAO;
import ua.com.dao.SubjectDAO;
import ua.com.dao.TeacherDAO;
import ua.com.entity.Lesson;
import ua.com.entity.Schedule;
import ua.com.entity.Teacher;
import ua.com.service.ScheduleService;

import java.util.*;
import java.util.stream.Collectors;

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
        int currentYear = calendar.get(Calendar.YEAR);
        int currentWeekOfYear = calendar.get(GregorianCalendar.WEEK_OF_YEAR);
        List<Schedule> oldSchedule = scheduleDAO.findAllGraterThenOrEqualToWeekOfYearOrYearAndByBand(
                currentYear,
                currentWeekOfYear + 1,
                Integer.parseInt(requestParam.get("bandId"))
        );
        List<Schedule> newSchedule = new ArrayList<>();

        scheduleDAO.delete(oldSchedule);

        while (calendar.get(GregorianCalendar.MONTH) < 5 || calendar.get(GregorianCalendar.MONTH) > 7) {
            for (int dayOfWeek = MIN_DAY_NR; dayOfWeek <= MAX_DAY_NR; dayOfWeek++) {
                if (calendar.get(GregorianCalendar.DAY_OF_WEEK) == dayOfWeek) {
                    for (int numberOfLesson = MIN_LESSON_NR; numberOfLesson <= MAX_LESSON_NR; numberOfLesson++) {
                        Schedule schedule = new Schedule();
                        schedule.setCalendar(new GregorianCalendar(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)));
                        schedule.setEditingDate(currentTime);
                        schedule.setYear(calendar.get(Calendar.YEAR));
                        schedule.setMonth(calendar.get(Calendar.MONTH));
                        schedule.setDayOfMonth(calendar.get(Calendar.DAY_OF_MONTH));
                        schedule.setWeekOfYear(calendar.get(GregorianCalendar.WEEK_OF_YEAR));
                        schedule.setDayOfWeek(dayOfWeek);
                        schedule.setNumberOfLesson(numberOfLesson);
                        schedule.setLesson(new Lesson());
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
                        newSchedule.add(schedule);
                    }
                }
            }
            calendar.add(GregorianCalendar.DAY_OF_MONTH, 1); // day increment
        }

        scheduleDAO.save(newSchedule);
    }

    @Override
    public void editSchedule(int bandId, int weekOfYear, int dayOfWeek, Map<String, String> requestParam) {
        final int MIN_LESSON_NR = 0;
        final int MAX_LESSON_NR = 8;
        GregorianCalendar calendar = new GregorianCalendar();
        Date currentTime = calendar.getTime();
        for (int numberOfLesson = MIN_LESSON_NR; numberOfLesson <= MAX_LESSON_NR; numberOfLesson++) {
            Schedule currentLessonSchedule = scheduleDAO.findByBandAndWeekOfYearAndDayOfWeekAndNumberOfLessonWithNested(
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
    public List<Schedule> journalSchedules(int subjectId, int bandId, int monthIndex) {
        List<Integer> teachersId = teacherDAO.findAllBySubjectAndBand(subjectId, bandId).stream().map(Teacher::getId).collect(Collectors.toList());
        List<Schedule> schedules = scheduleDAO.findAllByTeacherAndSubjectAndBandAndMonth(teachersId, subjectId, bandId, monthIndex);
        return schedules;
    }

    @Override
    public Schedule findOne(int id) {
        return scheduleDAO.findOne(id);
    }

    @Override
    public Schedule findByBandAndWeekOfYearAndDayOfWeekAndNumberOfLessonWithAllNested(int bandId, int weekOfYear, int dayOfWeek, int numberOfLesson) {
        return scheduleDAO.findByBandAndWeekOfYearAndDayOfWeekAndNumberOfLessonWithNested(bandId, weekOfYear, dayOfWeek, numberOfLesson);
    }

    @Override
    public List<Schedule> findAll() {
        return scheduleDAO.findAll();
    }

    @Override
    public List<Schedule> findAllByWeekOfYearWithAllNested(int weekOfYear) {
        return scheduleDAO.findAllByWeekOfYearWithNested(weekOfYear);
    }

    @Override
    public List<Schedule> findAllByBandAndWeekOfYearAndDayOfWeek(int bandId, int weekOfYear, int dayOfWeek) {
        return scheduleDAO.findAllByBandAndWeekOfYearAndDayOfWeek(bandId, weekOfYear,dayOfWeek);
    }

    @Override
    public void delete(Schedule schedule) {
        scheduleDAO.delete(schedule);
    }
}
