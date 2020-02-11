package ua.com.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.misc.GC;
import ua.com.dao.*;
import ua.com.entity.*;
import ua.com.service.ScheduleService;

import java.security.Principal;
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
    private StudentDAO studentDAO;

    @Autowired
    private TeacherDAO teacherDAO;

    @Autowired
    private LessonDAO lessonDAO;

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
        final int BEGINNING_ACADEMIC_MONTH = 8;
        final int ENDING_ACADEMIC_MONTH = 4;
        GregorianCalendar calendar = new GregorianCalendar();
        Date currentTime = calendar.getTime();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentWeekOfYear = calendar.get(GregorianCalendar.WEEK_OF_YEAR);
        List<Schedule> oldSchedule = scheduleDAO.findAllByBandAndGraterThenOrEqualToWeekOfYearAndGraterThenOrEqualToYear(
                currentYear,
                currentWeekOfYear + 1,
                Integer.parseInt(requestParam.get("bandId"))
        );
        List<Schedule> newSchedule = new ArrayList<>();

        scheduleDAO.delete(oldSchedule);

        while (calendar.get(GregorianCalendar.MONTH) < ENDING_ACADEMIC_MONTH || calendar.get(GregorianCalendar.MONTH) > BEGINNING_ACADEMIC_MONTH) {
            for (int dayOfWeek = MIN_DAY_NR; dayOfWeek <= MAX_DAY_NR; dayOfWeek++) {
                if (calendar.get(GregorianCalendar.DAY_OF_WEEK) == dayOfWeek) {
                    for (int numberOfLesson = MIN_LESSON_NR; numberOfLesson <= MAX_LESSON_NR; numberOfLesson++) {
                        Schedule schedule = new Schedule();
                        schedule.setCalendar(new GregorianCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)));
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
            Schedule currentLessonSchedule = scheduleDAO.findByBandAndWeekOfYearAndDayOfWeekAndNumberOfLesson(
                    bandId,
                    weekOfYear,
                    dayOfWeek,
                    numberOfLesson
            );
            if (currentLessonSchedule != null) {
                try {
                    if (currentLessonSchedule.getSubject().getId() != Integer.parseInt(requestParam.get("subject_" + numberOfLesson)) ||
                            currentLessonSchedule.getTeacher().getId() != Integer.parseInt(requestParam.get("teacher_" + numberOfLesson))) {
                        scheduleDAO.save(editScheduleFields(currentLessonSchedule, currentTime, requestParam, numberOfLesson));
                    }
                } catch (NullPointerException | NumberFormatException ex) {
                    scheduleDAO.save(editScheduleFields(currentLessonSchedule, currentTime, requestParam, numberOfLesson));
                }
            }
        }
    }

    private Schedule editScheduleFields (Schedule schedule, Date time,  Map<String, String> requestParam, int numberOfLesson) {
        schedule.setEditingDate(time);
        schedule.setLesson(new Lesson());
        try {
            schedule.setSubject(subjectDAO
                    .findOne(Integer.parseInt(
                            requestParam.get("subject_" + numberOfLesson)
                    )));
        } catch (NumberFormatException ex) {
            schedule.setSubject(null);
        }
        try {
            schedule.setTeacher(teacherDAO
                    .findOne(Integer.parseInt(
                            requestParam.get("teacher_" + numberOfLesson)
                    )));
        } catch (NumberFormatException ex) {
            schedule.setTeacher(null);
        }
        return schedule;
    }

    @Override
    public List<Schedule> journalSchedules(int subjectId, int bandId, int monthIndex) {
        Sort.Order byCalendar = new Sort.Order(Sort.Direction.ASC, "calendar");
        Sort orders = new Sort(byCalendar);
        List<Integer> teachersId = teacherDAO.findAllBySubjectAndBand(subjectId, bandId).stream().map(Teacher::getId).collect(Collectors.toList());
        List<Schedule> schedules = scheduleDAO.findAllByTeacherAndSubjectAndBandAndMonth(teachersId, subjectId, bandId, monthIndex, orders);
        return schedules;
    }

    @Override
    public List<Schedule> getUsersScheduleCurrentTwoWeeks(Principal principal, Authentication authentication) {
        final int SCHEDULE_DAYS_PERIOD = 13;
        final Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        GregorianCalendar today = new GregorianCalendar();
        GregorianCalendar beginningDate = new GregorianCalendar(today.get(GregorianCalendar.YEAR), today.get(GregorianCalendar.MONTH),
                today.get(GregorianCalendar.DAY_OF_MONTH), 0, 0, 0);
        beginningDate.get(GregorianCalendar.DAY_OF_MONTH);
        beginningDate.set(GregorianCalendar.DAY_OF_WEEK, today.getFirstDayOfWeek());
        GregorianCalendar endingDate = new GregorianCalendar(beginningDate.get(GregorianCalendar.YEAR), beginningDate.get(GregorianCalendar.MONTH),
                beginningDate.get(GregorianCalendar.DAY_OF_MONTH) + SCHEDULE_DAYS_PERIOD, 0, 0, 0);

        for (GrantedAuthority authority : authorities) {
            if (authority.getAuthority().equals("ROLE_STUDENT")){
                Student currentStudent = studentDAO.findByEmailWithBand(principal.getName());
                if (currentStudent.getBand() != null){
                    return scheduleDAO.findAllBetweenBeginningAndEndingDateAndBand(beginningDate, endingDate, currentStudent.getBand().getId());
                }
                return null;
            }

            if (authority.getAuthority().equals("ROLE_TEACHER") || authority.getAuthority().equals("ROLE_ADMIN")){
                Teacher currentTeacher = teacherDAO.findByEmail(principal.getName());
                return scheduleDAO.findAllBetweenBeginningAndEndingDateAndTeacher(beginningDate, endingDate, currentTeacher.getId());
            }
        }

        return null;
    }

    @Override
    public List<Schedule> getScheduleWeekBySelectedDay(long millis) {
        final int SCHEDULE_DAYS_PERIOD = 6;
        GregorianCalendar selectedDay = new GregorianCalendar();
        selectedDay.setTimeInMillis(millis);
        GregorianCalendar beginningDate = new GregorianCalendar(selectedDay.get(GregorianCalendar.YEAR), selectedDay.get(GregorianCalendar.MONTH),
                selectedDay.get(GregorianCalendar.DAY_OF_MONTH), 0, 0, 0);
        beginningDate.get(GregorianCalendar.DAY_OF_MONTH);
        beginningDate.set(GregorianCalendar.DAY_OF_WEEK, selectedDay.getFirstDayOfWeek());
        GregorianCalendar endingDate = new GregorianCalendar(beginningDate.get(GregorianCalendar.YEAR), beginningDate.get(GregorianCalendar.MONTH),
                beginningDate.get(GregorianCalendar.DAY_OF_MONTH) + SCHEDULE_DAYS_PERIOD, 0, 0, 0);
        return scheduleDAO.findAllBetweenBeginningAndEndingDate(beginningDate, endingDate);
    }

    @Override
    public Schedule findOne(int id) {
        return scheduleDAO.findOne(id);
    }

    @Override
    public Schedule findByBandAndWeekOfYearAndDayOfWeekAndNumberOfLesson(int bandId, int weekOfYear, int dayOfWeek, int numberOfLesson) {
        return scheduleDAO.findByBandAndWeekOfYearAndDayOfWeekAndNumberOfLesson(bandId, weekOfYear, dayOfWeek, numberOfLesson);
    }

    @Override
    public List<Schedule> findAll() {
        return scheduleDAO.findAll();
    }

    @Override
    public List<Schedule> findAllByWeekOfYearWithAllNested(int weekOfYear) {
        return scheduleDAO.findAllByWeekOfYear(weekOfYear);
    }

    @Override
    public List<Schedule> findAllByBandAndWeekOfYearAndDayOfWeek(int bandId, int weekOfYear, int dayOfWeek) {
        return scheduleDAO.findAllByBandAndWeekOfYearAndDayOfWeek(bandId, weekOfYear, dayOfWeek);
    }

    @Override
    public void delete(Schedule schedule) {
        scheduleDAO.delete(schedule);
    }
}
