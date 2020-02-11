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

    @GetMapping("/admin")
    public String admin(Principal principal, Model model) {
        model.addAttribute("principal", principal);
        return "admin";
    }

    @GetMapping("/admin/userRegistration")
    public String userRegistration(Model model) {
        model.addAttribute("userRegistrationDTO", new UserRegistrationDTO());
        return "adminUserRegistration";
    }

    @PostMapping("/admin/saveUser")
    public String saveUser(@ModelAttribute("userRegistrationDTO") @Valid UserRegistrationDTO userRegistrationDTO,
                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            bindingResult
                    .getFieldErrors()
                    .forEach(f -> System.out.println(f.getField() + ": " + f.getCode()));
            return "adminUserRegistration";
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
        model.addAttribute("allTeachers", teacherService.findAllWithAllNested(Sort.Direction.ASC, "surname"));
        return "adminUserEditingTeachers";
    }

    @GetMapping("/admin/findSpecificTeacher")
    public String findSpecificTeacher(@RequestParam("searchForm") String searchForm, Model model) {
        model.addAttribute("searchForm", searchForm);
        model.addAttribute("specifiedTeachers", teacherService.findSpecific(searchForm));
        return "adminUserEditingSpecifiedTeachers";
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
        model.addAttribute("allStudents", studentService.findAllWithBand(Sort.Direction.ASC, "surname"));
        return "adminUserEditingStudents";
    }

    @GetMapping("/admin/findSpecificStudent")
    public String findSpecificStudent(@RequestParam("searchForm") String searchForm, Model model) {
        model.addAttribute("searchFrom", searchForm);
        model.addAttribute("specifiedStudents", studentService.findSpecific(searchForm));
        return "adminUserEditingSpecifiedStudents";
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
        return "adminSetStudentToBand";
    }

    @PostMapping("/admin/saveStudentToBand")
    public String saveStudentToBand(@RequestParam Map<String, String> requestParam) {
        studentService.saveStudentToBand(requestParam);
        return "redirect:/admin/setStudentToBand";
    }

    @GetMapping("/admin/bandCreation")
    public String bandRegistration(Model model) {
        model.addAttribute("band", new Band());
        return "adminBandCreation";
    }

    @PostMapping("/admin/saveBand")
    public String saveBand(@ModelAttribute("band") @Valid Band band, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            bindingResult
                    .getFieldErrors()
                    .forEach(f -> System.out.println(f.getField() + ": " + f.getCode()));
            return "adminBandCreation";
        }
        bandService.save(band);
        return "redirect:/admin/bandCreation";
    }

    @GetMapping("/admin/bandEditing")
    public String bandEditing(Model model) {
        model.addAttribute("allBand", bandService.findAll(Sort.Direction.ASC, "name"));
        return "adminBandEditing";
    }

    @GetMapping("/admin/setSubjectToBand")
    public String setSubjectToBand(Model model) {
        model.addAttribute("allBand", bandService.findAll());
        model.addAttribute("allSubject", subjectService.findAllWithBand(Sort.Direction.ASC, "name"));
        return "adminSetSubjectToBand";
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
        return "adminSubjectCreation";
    }

    @PostMapping("/admin/saveSubject")
    public String saveSubject(@ModelAttribute("subject") @Valid Subject subject, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            bindingResult
                    .getFieldErrors()
                    .forEach(f -> System.out.println(f.getField() + ": " + f.getCode()));
            return "adminSubjectCreation";
        }
        subjectService.save(subject);
        return "redirect:/admin/subjectCreation";
    }

    @GetMapping("/admin/subjectEditing")
    public String subjectEditing(Model model) {
        model.addAttribute("allSubject", subjectService.findAll(Sort.Direction.ASC, "name"));
        return "adminSubjectEditing";
    }

    @GetMapping("/admin/setTeacherToSubject")
    public String setTeacherToSubject(Model model) {
        model.addAttribute("allSubjects", subjectService.findAll());
        model.addAttribute("allTeachers", teacherService.findAllWithSubject(Sort.Direction.ASC, "surname"));
        return "adminSetTeacherToSubject";
    }

    @PostMapping("/admin/saveTeacherToSubject")
    public String saveTeacherToSubject(@RequestParam("subjectId") int subjectId,
                                       @RequestParam Map<String, String> requestParam) {
        subjectService.saveTeacherToSubject(subjectId, requestParam);
        return "redirect:/admin/setTeacherToSubject";
    }

    @GetMapping("/admin/scheduleCreation")
    public String scheduleCreating(Model model) {
        model.addAttribute("allBand", bandService.findAll(Sort.Direction.ASC, "name"));
        return "adminScheduleCreation";
    }

    @PostMapping("/admin/createSchedule")
    public String createSchedule(@RequestParam Map<String, String> requestParam) {
        scheduleService.createSchedule(requestParam);
        return "redirect:/admin/scheduleCreation";
    }

    @GetMapping("/admin/scheduleEditing")
    public String scheduleEditing(Model model) {
        model.addAttribute("allBand", bandService.findAll(Sort.Direction.ASC, "name"));
        return "adminScheduleEditing";
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
