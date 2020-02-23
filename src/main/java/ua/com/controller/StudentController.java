package ua.com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import ua.com.service.BandService;
import ua.com.service.TeacherService;

@Controller
public class StudentController {

    @Autowired
    private BandService bandService;
    @Autowired
    private TeacherService teacherService;

    @GetMapping("/student")
    public String student() {
        return "student";
    }

    @GetMapping("/student/scheduleReview")
    public String scheduleReview (){
        return "studentScheduleReview";
    }

    @GetMapping("/student/scheduleBandsGeneralReview")
    public String scheduleBandGeneralReview(Model model){
        model.addAttribute("allBands", bandService.findAll(Sort.Direction.ASC, "name"));
        return "studentScheduleBandsGeneralReview";
    }

    @GetMapping("/student/scheduleTeachersGeneralReview")
    public String scheduleTeachersGeneralReview(Model model){
        model.addAttribute("allTeachers", teacherService.findAll(Sort.Direction.ASC, "surname"));
        return "studentScheduleTeachersGeneralReview";
    }

    @GetMapping("/student/diary")
    public String diary(){
        return "studentDiary";
    }

    @GetMapping("/student/settings")
    public String settings(){
        return "studentSettings";
    }
}
