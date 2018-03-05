package ua.com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.com.entity.Band;
import ua.com.entity.Student;
import ua.com.entity.Teacher;
import ua.com.entity.Subject;
import ua.com.service.BandService;
import ua.com.service.StudentService;
import ua.com.service.TeacherService;
import ua.com.service.SubjectService;

import java.util.Map;
import java.util.Set;

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
    public String userRegistration() {
        return "userRegistration";
    }


    @PostMapping("/saveUser")
    public String saveUser(@RequestParam("name") String name,
                           @RequestParam("surname") String surname,
                           @RequestParam("email") String email,
                           @RequestParam("userType") String userType) {

        if (userType.equals("student")) {

            Student student = new Student();
            student.setName(name);
            student.setSurname(surname);
            student.setEmail(email);
            studentService.save(student);
        } else if (userType.equals("teacher")) {
            Teacher teacher = new Teacher();
            teacher.setName(name);
            teacher.setSurname(surname);
            teacher.setEmail(email);
            teacherService.save(teacher);

        }


        return "redirect:/userRegistration";
    }

    @GetMapping("/bandRegistration")
    public String bandRegistration() {
        return "bandRegistration";
    }

    @PostMapping("/saveBand")
    public String saveBand(@RequestParam("name") String name) {
        Band band = new Band();
        band.setName(name);
        bandService.save(band);
        return "redirect:/bandRegistration";
    }

    @GetMapping("/setSubjectToBand")
    public String setSubjectToBand(Model model) {
        model.addAttribute("allBand", bandService.findAll());
        model.addAttribute("allSubject", subjectService.findAll());
        return "setSubjectToBand";
    }

    @PostMapping("/saveSubjectToBand")
    public String saveSubjectToBand(@RequestParam("idBand") int idBand,
                                    @RequestParam Map<String, String> requestParam) {
        Band band = bandService.findByIdWithSubject(idBand);
        Set<Subject> subjectList = band.getSubjectList();

        for (String key : requestParam.keySet()) {
            if (key.contains("idSubject-")) {
                Subject subject = subjectService.findOne(Integer.parseInt(requestParam.get(key)));
                subjectList.add(subject);

            }
        }
        band.setSubjectList(subjectList);
        System.out.println(band);
        System.out.println(subjectList);
        bandService.save(band);
        return "redirect:/setSubjectToBand";
    }

    @GetMapping("/subjectRegistration")
    public String subjectRegistration() {
        return "subjectRegistration";
    }

    @PostMapping("/saveSubject")
    public String saveSubject(@RequestParam("name") String name) {
        Subject subject = new Subject();
        subject.setName(name);
        subjectService.save(subject);
        return "redirect:/subjectRegistration";
    }
}
