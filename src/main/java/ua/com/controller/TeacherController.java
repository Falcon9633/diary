package ua.com.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class TeacherController {
    @GetMapping("/teacher")
    public String teacher() {
        return "teacher";
    }
}
