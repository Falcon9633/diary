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
import ua.com.service.BandService;
import ua.com.service.StudentService;
import ua.com.service.TeacherService;

@Controller
public class MainController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private BandService bandService;

    @Autowired
    private TeacherService teacherService;

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
        }else if(userType.equals("teacher")){
            Teacher teacher=new Teacher();
            teacher.setName(name);
            teacher.setSurname(surname);
            teacher.setEmail(email);
            teacherService.save(teacher);

        }




        return "redirect:/userRegistration";
    }


    @PostMapping("/createGroup")
    public String createGroup(@RequestParam("nameOfGroup") String nameOfGroup) {

        Band groupStudent = new Band();
        groupStudent.setName(nameOfGroup);
        bandService.save(groupStudent);


        return "redirect:/";
//        return "index";
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

}
