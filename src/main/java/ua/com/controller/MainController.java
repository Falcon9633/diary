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
import ua.com.validator.UserRegistrationValidator;

import javax.validation.Valid;
import java.security.Principal;
import java.util.*;

@Controller
public class MainController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private BandService bandService;

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private UserRegistrationValidator userRegistrationValidator;

    @Autowired
    private BandValidator bandValidator;

    @Autowired
    private SubjectValidator subjectValidator;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "redirect:/";
    }

    @GetMapping("/login-error")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        return "index";
    }

    @GetMapping("/student")
    public String student() {
        return "student";
    }

    @GetMapping("/teacher")
    public String teacher(Principal principal, Model model) {
        model.addAttribute("principal", principal);
        return "teacher";
    }

    @GetMapping("/admin")
    public String admin(Principal principal, Model model) {
        model.addAttribute("principal", principal);
        return "admin";
    }

    @GetMapping("/admin/userRegistration")
    public String userRegistration(Model model) {
        model.addAttribute("userRegistrationDTO", new UserRegistrationDTO());
        return "userRegistration";
    }

    @PostMapping("/admin/saveUser")
    public String saveUser(@ModelAttribute("userRegistrationDTO") @Valid UserRegistrationDTO userRegistrationDTO,
                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            bindingResult
                    .getFieldErrors()
                    .forEach(f -> System.out.println(f.getField() + ": " + f.getCode()));
            return "userRegistration";
        }

        if (userRegistrationDTO.getUserType().equals("student")) {
            Student registered = new Student();
            registered.setName(userRegistrationDTO.getName());
            registered.setSurname(userRegistrationDTO.getSurname());
            registered.setEmail(userRegistrationDTO.getEmail());
            studentService.save(registered);
        }

        if (userRegistrationDTO.getUserType().equals("teacher")) {
            Teacher registered = new Teacher();
            registered.setName(userRegistrationDTO.getName());
            registered.setSurname(userRegistrationDTO.getSurname());
            registered.setEmail(userRegistrationDTO.getEmail());
            teacherService.save(registered);
        }

        return "redirect:/admin/userRegistration";
    }

    @GetMapping("/admin/userEditingTeachers")
    public String userEditingTeachers(Model model) {
        Sort orders = sortByPropertyASC("surname");
        model.addAttribute("allTeachers", teacherService.findAllWithAllNested(orders));
        return "userEditingTeachers";
    }

    @GetMapping("/admin/findSpecificTeacher")
    public String findSpecificTeacher(@RequestParam("searchForm") String searchForm, Model model) {
        model.addAttribute("searchForm", searchForm);
        String[] splitSearchForm = searchForm.trim().split(" ");
        if (splitSearchForm.length > 1) {
            model.addAttribute("specifiedTeachers", teacherService.findSpecific(splitSearchForm[0], splitSearchForm[1]));
        } else {
            model.addAttribute("specifiedTeachers", teacherService.findSpecific(searchForm));
        }
        return "userEditingSpecifiedTeachers";
    }

    @PostMapping("/admin/editTeacher")
    public String editTeacher(@RequestParam("surname") String surname,
                              @RequestParam("name") String name,
                              @RequestParam("email") String email,
                              @RequestParam("id") int id) {
        userEditor(surname, name, email, id, "teacher");
        return "redirect:/admin/userEditingTeachers";
    }

    @GetMapping("/admin/userEditingStudents")
    public String userEditingStudents(Model model) {
        Sort orders = sortByPropertyASC("surname");
        model.addAttribute("allStudents", studentService.findAllWithBand(orders));
        return "userEditingStudents";
    }

    @GetMapping("/admin/findSpecificStudent")
    public String findSpecificStudent(@RequestParam("searchForm") String searchForm, Model model) {
        model.addAttribute("searchFrom", searchForm);
        String[] splitSearchForm = searchForm.trim().split(" ");
        if (splitSearchForm.length > 1) {
            model.addAttribute("specifiedStudents", studentService.findSpecific(splitSearchForm[0], splitSearchForm[1]));
        } else {
            model.addAttribute("specifiedStudents", studentService.findSpecific(searchForm));
        }
        return "userEditingSpecifiedStudents";
    }

    @PostMapping("/admin/editStudent")
    public String editStudent(@RequestParam("surname") String surname,
                              @RequestParam("name") String name,
                              @RequestParam("email") String email,
                              @RequestParam("id") int id) {
        userEditor(surname, name, email, id, "student");
        return "redirect:/admin/userEditingStudents";
    }

    private Sort sortByPropertyASC(String property) {
        Sort.Order byProperty = new Sort.Order(Sort.Direction.ASC, property);
        Sort orders = new Sort(byProperty);
        return orders;
    }

    private void userEditor(String surname, String name, String email, int id, String userType) {
        if (!surname.trim().isEmpty() || !name.trim().isEmpty() || !email.trim().isEmpty()) {
            if (userType.equals("student")) {
                Student selectedStudent = studentService.findOne(id);
                if (!surname.trim().isEmpty()) {
                    selectedStudent.setSurname(surname);
                }
                if (!name.trim().isEmpty()) {
                    selectedStudent.setName(name);
                }
                if (!email.trim().isEmpty()) {
                    selectedStudent.setEmail(email);
                }
                studentService.save(selectedStudent);
            }

            if (userType.equals("teacher")) {
                Teacher selectedTeacher = teacherService.findOne(id);
                if (!surname.trim().isEmpty()) {
                    selectedTeacher.setSurname(surname);
                }
                if (!name.trim().isEmpty()) {
                    selectedTeacher.setName(name);
                }
                if (!email.trim().isEmpty()) {
                    selectedTeacher.setEmail(email);
                }
                teacherService.save(selectedTeacher);
            }
        }
    }

    @GetMapping("/admin/setStudentToBand")
    public String setStudentToBand(Model model) {
        model.addAttribute("allBands", bandService.findAll());
        model.addAttribute("allStudents", studentService.findAllWithBand());

        return "setStudentToBand";
    }

    @PostMapping("/admin/saveStudentToBand")
    public String saveStudentToBand(@RequestParam Map<String, String> requestParam) {
        Band band = bandService.findOne(Integer.parseInt(requestParam.get("band")));

        for (String key : requestParam.keySet()) {
            if (key.contains("user")) {
                Student student = studentService.findStudentByIdWithBand(Integer.parseInt(requestParam.get(key)));
                student.setBand(band);
                studentService.save(student);
            }
        }

        return "redirect:/admin/setStudentToBand";
    }

    @GetMapping("/admin/bandCreation")
    public String bandRegistration(Model model) {
        model.addAttribute("band", new Band());
        return "bandCreation";
    }

    @PostMapping("/admin/saveBand")
    public String saveBand(@ModelAttribute("band") @Valid Band band, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            bindingResult
                    .getFieldErrors()
                    .forEach(f -> System.out.println(f.getField() + ": " + f.getCode()));
            return "bandCreation";
        }
        bandService.save(band);
        return "redirect:/admin/bandCreation";
    }

    @GetMapping("/admin/bandEditing")
    public String bandEditing(Model model) {
        Sort orders = sortByPropertyASC("name");
        model.addAttribute("allBand", bandService.findAll(orders));
        return "bandEditing";
    }

    @GetMapping("/admin/setSubjectToBand")
    public String setSubjectToBand(Model model) {
        Sort orders = sortByPropertyASC("name");
        model.addAttribute("allBand", bandService.findAll());
        model.addAttribute("allSubject", subjectService.findAllWithBand(orders));
        return "setSubjectToBand";
    }

    @PostMapping("/admin/saveSubjectToBand")
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
        return "redirect:/admin/setSubjectToBand";
    }

    @GetMapping("/admin/subjectCreation")
    public String subjectRegistration(Model model) {
        model.addAttribute("subject", new Subject());
        return "subjectCreation";
    }

    @PostMapping("/admin/saveSubject")
    public String saveSubject(@ModelAttribute("subject") @Valid Subject subject, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            bindingResult
                    .getFieldErrors()
                    .forEach(f -> System.out.println(f.getField() + ": " + f.getCode()));
            return "subjectCreation";
        }
        subjectService.save(subject);
        return "redirect:/admin/subjectCreation";
    }

    @GetMapping("/admin/subjectEditing")
    public String subjectEditing(Model model) {
        Sort orders = sortByPropertyASC("name");
        model.addAttribute("allSubject", subjectService.findAll(orders));
        return "subjectEditing";
    }

    @GetMapping("/admin/setTeacherToSubject")
    public String setTeacherToSubject(Model model) {
        Sort orders = sortByPropertyASC("surname");
        model.addAttribute("allSubjects", subjectService.findAll());
        model.addAttribute("allTeachers", teacherService.findAllWithSubject(orders));
        return "setTeacherToSubject";
    }

    @PostMapping("/admin/saveTeacherToSubject")
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
        return "redirect:/admin/setTeacherToSubject";
    }

    @GetMapping("/admin/scheduleCreation")
    public String scheduleCreating(Model model) {
        model.addAttribute("allBand", bandService.findAll());
        return "scheduleCreation";
    }

    @PostMapping("/admin/createSchedule")
    public String createSchedule(@RequestParam Map<String, String> requestParam) {
        final int MIN_DAY_NR = 2;
        final int MAX_DAY_NR = 7;
        final int MIN_LESSON_NR = 0;
        final int MAX_LESSON_NR = 8;
        GregorianCalendar calendar = new GregorianCalendar();
        Date currentTime = calendar.getTime();
        int currentWeekOfYear = calendar.get(GregorianCalendar.WEEK_OF_YEAR);
        List<Schedule> oldSchedule = scheduleService.findAllGraterThenOrEqualToWeekOfYear(currentWeekOfYear);
        for (Schedule schedule : oldSchedule) {
            scheduleService.delete(schedule);
        }
        while (calendar.get(GregorianCalendar.MONTH) < 5 || calendar.get(GregorianCalendar.MONTH) > 7) {
            for (int dayOfWeek = MIN_DAY_NR; dayOfWeek <= MAX_DAY_NR; dayOfWeek++) {
                if (calendar.get(GregorianCalendar.DAY_OF_WEEK) == dayOfWeek) {
                    for (int numberOfLesson = MIN_LESSON_NR; numberOfLesson <= MAX_LESSON_NR; numberOfLesson++) {
                        Schedule schedule = new Schedule();
                        schedule.setEditingDate(currentTime);
                        schedule.setWeekOfYear(calendar.get(GregorianCalendar.WEEK_OF_YEAR));
                        schedule.setDayOfWeek(dayOfWeek);
                        schedule.setNumberOfLesson(numberOfLesson);
                        scheduleService.save(schedule);
                        try {
                            schedule.setBand(bandService
                                    .findOne(Integer.parseInt(
                                            requestParam.get("bandId")
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
        return "redirect:/admin/scheduleCreation";
    }

    @GetMapping("/admin/scheduleEditing")
    public String scheduleEditing(Model model) {
        model.addAttribute("allBand", bandService.findAll());
        return "scheduleEditing";
    }

    @PostMapping("/admin/editSchedule")
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
        return "redirect:/admin/scheduleEditing";
    }

    @InitBinder("userRegistrationDTO")
    public void initUserRegistrationBinder(WebDataBinder binder) {
        binder.addValidators(userRegistrationValidator);
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