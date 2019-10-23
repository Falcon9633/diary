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
public class AdminController {

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

    private Sort sortByPropertyASC(String property) {
        Sort.Order byProperty = new Sort.Order(Sort.Direction.ASC, property);
        return new Sort(byProperty);
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
            studentService.save(userRegistrationDTO);
        }

        if (userRegistrationDTO.getUserType().equals("teacher")) {
            teacherService.save(userRegistrationDTO);
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
        model.addAttribute("specifiedTeachers", teacherService.findSpecific(searchForm));
        return "userEditingSpecifiedTeachers";
    }

    @PostMapping("/admin/editTeacher")
    public String editTeacher(@RequestParam("surname") String surname,
                              @RequestParam("name") String name,
                              @RequestParam("email") String email,
                              @RequestParam("id") int id,
                              @RequestParam(value = "isAdmin", defaultValue = "false") boolean isAdmin) {
        teacherService.edit(id, name, surname, email, isAdmin);
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
        model.addAttribute("specifiedStudents", studentService.findSpecific(searchForm));
        return "userEditingSpecifiedStudents";
    }

    @PostMapping("/admin/editStudent")
    public String editStudent(@RequestParam("surname") String surname,
                              @RequestParam("name") String name,
                              @RequestParam("email") String email,
                              @RequestParam("id") int id) {
        studentService.edit(id, name, surname, email);
        return "redirect:/admin/userEditingStudents";
    }

    @GetMapping("/admin/setStudentToBand")
    public String setStudentToBand(Model model) {
        model.addAttribute("allBands", bandService.findAll());
        model.addAttribute("allStudents", studentService.findAllWithBand());
        return "setStudentToBand";
    }

    @PostMapping("/admin/saveStudentToBand")
    public String saveStudentToBand(@RequestParam Map<String, String> requestParam) {
        studentService.saveStudentToBand(requestParam);
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
    public String saveSubjectToBand(@RequestParam("bandId") int bandId,
                                    @RequestParam Map<String, String> requestParam) {
        bandService.saveSubjectToBand(bandId, requestParam);
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
    public String saveTeacherToSubject(@RequestParam("subjectId") int subjectId,
                                       @RequestParam Map<String, String> requestParam) {
        subjectService.saveTeacherToSubject(subjectId, requestParam);
        return "redirect:/admin/setTeacherToSubject";
    }

    @GetMapping("/admin/scheduleCreation")
    public String scheduleCreating(Model model) {
        model.addAttribute("allBand", bandService.findAll());
        return "scheduleCreation";
    }

    @PostMapping("/admin/createSchedule")
    public String createSchedule(@RequestParam Map<String, String> requestParam) {
        scheduleService.createSchedule(requestParam);
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
        scheduleService.editSchedule(bandId, weekOfYear, dayOfWeek, requestParam);
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
