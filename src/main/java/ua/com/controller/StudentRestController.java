package ua.com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ua.com.entity.*;
import ua.com.service.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
public class StudentRestController {

    @Autowired
    private StudentService studentService;
    @Autowired
    private SubjectService subjectService;
    @Autowired
    private ScheduleService scheduleService;
    @Autowired
    private NoteService noteService;
    @Autowired
    private LessonService lessonService;

    @GetMapping("/student/getSchedulesReviewCurrentWeek")
    public List<Schedule> getSchedulesReviewCurrentWeek(Principal principal, Authentication authentication) {
        return scheduleService.getUsersScheduleCurrentTwoWeeks(principal, authentication);
    }

    @GetMapping("/student/getScheduleWeekBySelectedDay")
    public List<Schedule> getScheduleWeekBySelectedDay(@RequestParam("selectedDateMs") long selectedDayMs) {
        return scheduleService.getScheduleWeekBySelectedDay(selectedDayMs);
    }

    @GetMapping("/student/getWeeksScheduleBySelectedDayAndBand")
    public List<Schedule> getSchedulesBySelectedDayAndBand(@RequestParam("selectedDateMs") long selectedDayMs,
                                                           @RequestParam("bandId") int bandId) {
        return scheduleService.getScheduleWeekBySelectedDayAndBand(selectedDayMs, bandId);
    }

    @GetMapping("/student/getWeeksLessonsBySelectedDayAndBand")
    public List<Lesson> getWeeksLessonsBySelectedDayAndBand(@RequestParam("selectedDateMs") long selectedDayMs,
                                                            @RequestParam("bandId") int bandId) {
        return lessonService.getWeeksLessonsBySelectedDayAndBand(selectedDayMs, bandId);
    }

    @GetMapping("/student/getAuthenticatedUser")
    public Student getAuthenticatedUser(Principal principal) {
        return studentService.findByEmailWithNested(principal.getName());
    }

    @GetMapping("/student/getAllSubjects")
    public Set<Subject> getAllSubjects() {
        return subjectService.findAllWithAllNested();
    }

    @GetMapping("/student/getAllStudentsNotes")
    public List<Note> getAllStudentsNotes(Principal principal) {
        return noteService.getAllStudentsNotes(principal);
    }

    @GetMapping("/student/passwordCheck")
    public boolean passwordCheck(Principal principal,
                                 @RequestParam("password") String password) {
        return studentService.passwordCheck(principal, password);
    }

    @RequestMapping(value = "/student/changePassword", method = RequestMethod.POST)
    public void changePassword(@RequestParam("password") String password,
                               Principal principal) {
        studentService.changePassword(principal, password);
    }
}
