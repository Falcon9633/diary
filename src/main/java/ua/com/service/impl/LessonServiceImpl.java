package ua.com.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.dao.LessonDAO;
import ua.com.entity.Lesson;
import ua.com.entity.Schedule;
import ua.com.service.LessonService;
import ua.com.service.ScheduleService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LessonServiceImpl implements LessonService {

    @Autowired
    private LessonDAO lessonDAO;
    @Autowired
    private ScheduleService scheduleService;

    @Override
    public List<Lesson> findAllByMonth(int monthIndex) {
        return lessonDAO.findAllByMonth(monthIndex);
    }

    @Override
    public List<Lesson> getWeeksLessonsBySelectedDayAndBand(long selectedDayMs, int bandId) {
        List<Schedule> schedules = scheduleService.getScheduleWeekBySelectedDayAndBand(selectedDayMs, bandId);
        List<Integer> lessonsIds = schedules.stream().map(schedule -> schedule.getLesson().getId()).collect(Collectors.toList());
        return lessonDAO.findAllByIds(lessonsIds);
    }
}
