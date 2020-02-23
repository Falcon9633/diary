package ua.com.service;

import ua.com.entity.Lesson;

import java.util.List;

public interface LessonService {
    List<Lesson> findAllByMonth(int monthIndex);

    List<Lesson> getWeeksLessonsBySelectedDayAndBand(long selectedDayMs, int bandId);
}
