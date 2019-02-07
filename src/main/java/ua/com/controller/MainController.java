package ua.com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import ua.com.dto.UserRegistrationDTO;
import ua.com.entity.*;
import ua.com.service.*;
import ua.com.validator.BandValidator;
import ua.com.validator.SubjectValidator;

import javax.validation.Valid;
import java.util.*;

@Controller
public class MainController {

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

    @Autowired
    private BandValidator bandValidator;

    @Autowired
    private SubjectValidator subjectValidator;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/student")
    public String student() {
        return "student";
    }

    @GetMapping("/teacher")
    public String teacher() {
        return "teacher";
    }

    @GetMapping("/admin")
    public String admin() {
        return "admin";
    }


    @GetMapping("/userRegistration")
    public String userRegistration(Model model) {
        model.addAttribute("userRegistrationDTO", new UserRegistrationDTO());
        return "userRegistration";
    }


    @PostMapping("/saveUser")
    public String saveUser(@ModelAttribute("userRegistrationDTO") @Valid UserRegistrationDTO userRegistrationDTO,
                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "userRegistration";
        }

        return "redirect:/userRegistration";
    }

    @GetMapping("/setStudentToBand")
    public String setStudentToBand(Model model) {
        model.addAttribute("allBands", bandService.findAll());
        model.addAttribute("dateStudent", studentService.findAllWithBand());


        return "setStudentToBand";
    }

    @PostMapping("/saveStudentToBand")
    public String saveStudentToBand(@RequestParam Map<String, String> requestParam) {

        Band band = bandService.findOne(Integer.parseInt(requestParam.get("band")));

        for (String key : requestParam.keySet()) {
            if (key.contains("user")) {
                Student student = studentService.findStudentWithBand(Integer.parseInt(requestParam.get(key)));

                student.setBand(band);
                studentService.save(student);
            }
        }

        return "redirect:/setStudentToBand";
    }

    @GetMapping("/bandCreation")
    public String bandRegistration(Model model) {
        model.addAttribute("band", new Band());
        return "bandCreation";
    }

    @PostMapping("/saveBand")
    public String saveBand(@ModelAttribute("band") @Valid Band band, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            System.out.println("bandEmptyNameErr");
            return "bandRegistration";
        }
        bandService.save(band);
        return "redirect:/bandRegistration";
    }

    @GetMapping("/bandEditing")
    public String bandEditing(Model model) {
        Sort.Order byName = new Sort.Order(Sort.Direction.ASC, "name");
        Sort orders = new Sort(byName);
        model.addAttribute("allBand", bandService.findAll(orders));
        return "bandEditing";
    }

    @GetMapping("/setSubjectToBand")
    public String setSubjectToBand(Model model) {
        Sort.Order byName = new Sort.Order(Sort.Direction.ASC, "name");
        Sort orders = new Sort(byName);
        model.addAttribute("allBand", bandService.findAll());
        model.addAttribute("allSubject", subjectService.findAllWithBand(orders));
        return "setSubjectToBand";
    }

    @PostMapping("/saveSubjectToBand")
    public String saveSubjectToBand(@RequestParam("bandId") int idBand,
                                    @RequestParam Map<String, String> requestParam) {
        Band band = bandService.findByIdWithSubject(idBand);
        Set<Subject> subjectList = band.getSubjectList();

        for (String key : requestParam.keySet()) {
            if (key.contains("subjectId-")) {
                Subject subject = subjectService.findOne(Integer.parseInt(requestParam.get(key)));
                subjectList.add(subject);
            }
        }
        band.setSubjectList(subjectList);
        bandService.save(band);
        return "redirect:/setSubjectToBand";
    }

    @GetMapping("/subjectCreation")
    public String subjectRegistration(Model model) {
        model.addAttribute("subject", new Subject());
        return "subjectCreation";
    }

    @PostMapping("/saveSubject")
    public String saveSubject(@ModelAttribute("subject") @Valid Subject subject, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            System.out.println("subjectEmptyNameErr");
            return "subjectRegistration";
        }
        subjectService.save(subject);
        return "redirect:/subjectRegistration";
    }

    @GetMapping("/subjectEditing")
    public String subjectEditing(Model model) {
        Sort.Order byName = new Sort.Order(Sort.Direction.ASC, "name");
        Sort orders = new Sort(byName);
        model.addAttribute("allSubject", subjectService.findAll(orders));
        return "subjectEditing";
    }

    @GetMapping("/setTeacherToSubject")
    public String setTeacherToSubject(Model model) {
        Sort.Order bySurname = new Sort.Order(Sort.Direction.ASC, "surname");
        Sort orders = new Sort(bySurname);
        model.addAttribute("allSubject", subjectService.findAll());
        model.addAttribute("allTeacher", teacherService.findAllWithSubject(orders));
        return "setTeacherToSubject";
    }

    @PostMapping("/saveTeacherToSubject")
    public String saveTeacherToSubject(@RequestParam("subjectId") int idSubject,
                                       @RequestParam Map<String, String> requestParam) {
        Subject subject = subjectService.findByIdWithTeacher(idSubject);
        Set<Teacher> teacherList = subject.getTeacherList();

        for (String key : requestParam.keySet()) {
            if (key.contains("teacherId-")) {
                Teacher teacher = teacherService.findOne(Integer.parseInt(requestParam.get(key)));
                teacherList.add(teacher);
            }
        }
        subject.setTeacherList(teacherList);
        subjectService.save(subject);
        return "redirect:/setTeacherToSubject";
    }

    @GetMapping("/scheduleCreation")
    public String scheduleCreating(Model model) {
        model.addAttribute("allBand", bandService.findAll());
        return "scheduleCreation";
    }

    @PostMapping("/createSchedule")
    public String createSchedule(@RequestParam Map<String, String> requestParam) {
        GregorianCalendar calendar = new GregorianCalendar();
        Date currentTime = calendar.getTime();
        int currentWeekOfYear = calendar.get(GregorianCalendar.WEEK_OF_YEAR);
        List<Schedule> oldSchedule = scheduleService.findAllGraterThenOrEqualToWeekOfYear(currentWeekOfYear);
        for (Schedule schedule : oldSchedule) {
            scheduleService.delete(schedule);
        }
        while (calendar.get(GregorianCalendar.MONTH) < 5 || calendar.get(GregorianCalendar.MONTH) > 7) {
            for (int dayOfWeek = 2; dayOfWeek <= 7; dayOfWeek++) {
                if (calendar.get(GregorianCalendar.DAY_OF_WEEK) == dayOfWeek) {
                    for (int numberOfLesson = 0; numberOfLesson <= 8; numberOfLesson++) {
                        Schedule schedule = new Schedule();
                        schedule.setEditingDate(currentTime);
                        schedule.setWeekOfYear(calendar.get(GregorianCalendar.WEEK_OF_YEAR));
                        schedule.setDayOfWeek(dayOfWeek);
                        schedule.setNumberOfLesson(numberOfLesson);
                        scheduleService.save(schedule);
                        try {
                            schedule.setBand(bandService
                                    .findOne(Integer.parseInt(
                                            requestParam.get("idBand")
                                    )));
                        } catch (NumberFormatException ex) {
                            ex.printStackTrace();
                        }
                        try {
                            schedule.setSubject(subjectService
                                    .findOne(Integer.parseInt(
                                            requestParam.get("subject_" + dayOfWeek + "_" + numberOfLesson)
                                    )));
                        } catch (NumberFormatException ex) {
                            schedule.setSubject(null);
                        }
                        try {
                            schedule.setTeacher(teacherService
                                    .findOne(Integer.parseInt(
                                            requestParam.get("teacher_" + dayOfWeek + "_" + numberOfLesson)
                                    )));
                        } catch (NumberFormatException ex) {
                            schedule.setTeacher(null);
                        }
                        scheduleService.save(schedule);
                    }
                }
            }
            calendar.add(GregorianCalendar.DAY_OF_MONTH, 1); // day increment
        }
        return "redirect:/scheduleCreation";
    }

    @GetMapping("/scheduleEditing")
    public String scheduleEditing(Model model) {
        model.addAttribute("allBand", bandService.findAll());
        return "scheduleEditing";
    }

    @PostMapping("/editSchedule")
    public String editSchedule(@RequestParam Map<String, String> requestParam,
                               @RequestParam("bandId") int bandId,
                               @RequestParam("weekOfYear") int weekOfYear,
                               @RequestParam("dayOfWeek") int dayOfWeek) {
        GregorianCalendar calendar = new GregorianCalendar();
        Date currentTime = calendar.getTime();
        for (int numberOfLesson = 0; numberOfLesson <= 8; numberOfLesson++) {
            Schedule currentLessonSchedule = scheduleService.findByBandAndWeekOfYearAndDayOfWeekAndNumberOfLessonWithAllNested(
                    bandId,
                    weekOfYear,
                    dayOfWeek,
                    numberOfLesson
            );
            currentLessonSchedule.setEditingDate(currentTime);
            try {
                currentLessonSchedule.setSubject(subjectService
                        .findOne(Integer.parseInt(
                                requestParam.get("subject_" + numberOfLesson)
                        )));
            } catch (NumberFormatException ex) {
                currentLessonSchedule.setSubject(null);
            }
            try {
                currentLessonSchedule.setTeacher(teacherService
                        .findOne(Integer.parseInt(
                                requestParam.get("teacher_" + numberOfLesson)
                        )));
            } catch (NumberFormatException ex) {
                currentLessonSchedule.setTeacher(null);
            }
            scheduleService.save(currentLessonSchedule);
        }
        return "redirect:/scheduleEditing";
    }

    @InitBinder("band")
    public void initBandBinder(WebDataBinder binder) {
        binder.addValidators(bandValidator);
    }

    @InitBinder("subject")
    public void initSubjectBinder(WebDataBinder binder) {
        binder.addValidators(subjectValidator);
    }
}