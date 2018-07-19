package ua.com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sun.misc.GC;
import ua.com.entity.*;
import ua.com.service.*;

import java.util.*;

@RestController
public class MainRestController {
    @Autowired
    private StudentService studentService;

    @Autowired
    private BandService bandService;

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private ScheduleService scheduleService;

    @GetMapping("/getAllBands")
    public Set<Band> getAllBands() {
        return bandService.findAllWithAllNested();
    }

    @RequestMapping(value = "/editBand", method = RequestMethod.POST)
    public void editBand(@RequestBody Band band) {
        if (!band.getStudentsList().isEmpty()){
            for (Student student : band.getStudentsList()) {
                student.setBand(null);
                studentService.save(student);
            }
        }
        bandService.save(band);
    }

    @GetMapping("/getAllSubjects")
    public Set<Subject> getAllSubjects() {
        return subjectService.findAllWithAllNested();
    }

    @RequestMapping(value = "/editSubject", method = RequestMethod.POST)
    public void editSubject(@RequestBody Subject subject) {
        subjectService.save(subject);
    }

    @GetMapping("/getAllTeachers")
    public Set<Teacher> getAllTeachers() {
        return teacherService.findAllWithAllNested();
    }

    @GetMapping("/getAllScheduleCurrentWeek")
    public List<Schedule> getAllScheduleCurrentWeek(){
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        return scheduleService.findAllByWeekOfYearWithAllNested(gregorianCalendar.get(GregorianCalendar.WEEK_OF_YEAR));
    }
}