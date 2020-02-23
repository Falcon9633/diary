package ua.com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ua.com.entity.Lesson;
import ua.com.entity.Schedule;
import ua.com.service.LessonService;
import ua.com.service.ScheduleService;
import ua.com.service.TeacherService;

import java.security.Principal;
import java.util.List;

@RestController
public class TeacherRestController {

    @Autowired
    private LessonService lessonService;
    @Autowired
    private ScheduleService scheduleService;
    @Autowired
    private TeacherService teacherService;

    @GetMapping("/teacher/getSelectedMonthLessons")
    public List<Lesson> getSelectedMonthLessons(@RequestParam("selectedMonthIndex") int selectedMonthIndex) {
        return lessonService.findAllByMonth(selectedMonthIndex);
    }

    @GetMapping("/teacher/getSchedulesReviewCurrentWeek")
    public List<Schedule> getSchedulesReviewCurrentWeek(Principal principal, Authentication authentication) {
        return scheduleService.getUsersScheduleCurrentTwoWeeks(principal, authentication);
    }

    @GetMapping("/teacher/getScheduleWeekBySelectedDay")
    public List<Schedule> getScheduleWeekBySelectedDay(@RequestParam("selectedDateMs") long selectedDayMs){
        return scheduleService.getScheduleWeekBySelectedDay(selectedDayMs);
    }

    @GetMapping("/teacher/passwordCheck")
    public boolean passwordCheck(Principal principal,
                                 @RequestParam("password") String password){
        return teacherService.passwordCheck(principal, password);
    }

    @PostMapping("/teacher/changePassword")
    public void changePassword(@RequestParam("password") String password,
                               Principal principal){
        teacherService.changePassword(password, principal);
    }
}