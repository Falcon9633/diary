package ua.com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ua.com.entity.Schedule;
import ua.com.service.ScheduleService;

import java.security.Principal;
import java.util.List;

@RestController
public class StudentRestController {

    @Autowired
    private ScheduleService scheduleService;

    @GetMapping("/student/getSchedulesReviewCurrentWeek")
    public List<Schedule> getSchedulesReviewCurrentWeek (Principal principal, Authentication authentication){
        return scheduleService.getUsersScheduleCurrentTwoWeeks(principal, authentication);
    }

    @GetMapping("/student/getScheduleWeekBySelectedDay")
    public List<Schedule> getScheduleWeekBySelectedDay(@RequestParam("selectedDateMs") long selectedDayMs){
        return scheduleService.getScheduleWeekBySelectedDay(selectedDayMs);
    }
}
