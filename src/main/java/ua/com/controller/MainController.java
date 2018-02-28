package ua.com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.com.entity.Band;
import ua.com.entity.Student;
import ua.com.entity.Subject;
import ua.com.service.BandService;
import ua.com.service.StudentService;
import ua.com.service.SubjectService;

@Controller
public class MainController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private BandService bandService;

    @Autowired
    private SubjectService subjectService;

    @GetMapping("/")
    public String groups(Model model) {

//        model.addAttribute("key",groupStudentService.findOne(1));
        model.addAttribute("key", bandService.findAll());

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

    @PostMapping("/insert")
    public String insert(@RequestParam("name") String name,
                         @RequestParam("surname") String surname,
                         @RequestParam("idOfGroup") int idOfGroup) {
        Student student = new Student();
//        student.setName("вася");
        student.setName(name);
        student.setSurname(surname);


//      groupStudentService.findOne(idOfGroup);
        Band band = bandService.findOne(idOfGroup);

        student.setBand(band);
//        groupStudent.getStudentsList().add(student);

        studentService.save(student);

        return "redirect:/";
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

    @GetMapping("/subjectRegistration")
    public String subjectRegistration() {
        return "subjectRegistration";
    }

    @PostMapping("/saveSubject")
    public String saveSubject(@RequestParam("name") String name){
        Subject subject = new Subject();
        subject.setName(name);
        subjectService.save(subject);
        return "redirect:/subjectRegistration";
    }
}
