package ua.com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.com.entity.GroupStudent;
import ua.com.entity.Student;
import ua.com.service.GroupStudentService;
import ua.com.service.StudentService;

import java.util.ArrayList;
import java.util.List;

@Controller
public class MainController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private GroupStudentService groupStudentService;

    @GetMapping("/")
    public String groups(Model model) {

//        model.addAttribute("key",groupStudentService.findOne(1));
        model.addAttribute("key", groupStudentService.findAll());

        return "index";
    }

    @GetMapping("/student")
    public String student(){
        return "student";
    }

    @GetMapping("/teacher")
    public String teacher(){
        return "teacher";
    }

    @GetMapping("/admin")
    public String admin(){
        return "admin";
    }

    @PostMapping("/createGroup")
    public String createGroup(@RequestParam("nameOfGroup") String nameOfGroup) {

        GroupStudent groupStudent = new GroupStudent();
        groupStudent.setNameOfGroup(nameOfGroup);
        groupStudentService.save(groupStudent);

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
        GroupStudent groupStudent = groupStudentService.findOne(idOfGroup);

        student.setGroupStudent(groupStudent);
//        groupStudent.getStudentsList().add(student);

        studentService.save(student);


        return "redirect:/";
    }

}
