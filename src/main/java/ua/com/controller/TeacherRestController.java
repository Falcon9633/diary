package ua.com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ua.com.entity.Lesson;
import ua.com.service.LessonService;

import java.util.List;

@RestController
public class TeacherRestController {

    @Autowired
    private LessonService lessonService;

    @GetMapping("/teacher/getSelectedMonthLessons")
    public List<Lesson> getSelectedMonthLessons(@RequestParam("selectedMonthIndex") int selectedMonthIndex) {
        return lessonService.findAllByMonth(selectedMonthIndex);
    }
}
