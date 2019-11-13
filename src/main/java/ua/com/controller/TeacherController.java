package ua.com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.com.dao.NoteDAO;
import ua.com.dao.ScheduleDAO;
import ua.com.dao.StudentDAO;
import ua.com.dto.StudentScheduleNoteDTO;
import ua.com.entity.*;
import ua.com.service.*;

import java.security.Principal;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private BandService bandService;

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private NoteService noteService;

    @GetMapping("/teacher")
    public String teacher(Principal principal, Model model) {
        model.addAttribute("journalNavigationSubjectAndBand", teacherService.journalNavigationSubjectAndBand(principal));
        return "teacher";
    }

    @GetMapping("/teacher/journalEditing-{subjectId}-{bandId}-{monthIndex}")
    public String journal(Principal principal, Model model,
                          @PathVariable("subjectId") int subjectId,
                          @PathVariable("bandId") int bandId,
                          @PathVariable("monthIndex") int monthIndex) {
        model.addAttribute("journalNavigationSubjectAndBand", teacherService.journalNavigationSubjectAndBand(principal));
        model.addAttribute("selectedBand", bandService.findOne(bandId));
        model.addAttribute("selectedSubject", subjectService.findOne(subjectId));
        model.addAttribute("availableTeachers", teacherService.findAllBySubjectAndBand(subjectId, bandId));
        model.addAttribute("schedules", scheduleService.journalSchedules(subjectId, bandId, monthIndex));
        model.addAttribute("studentScheduleNoteDTOS", noteService.getStudentScheduleNoteDTOS(bandId, subjectId, monthIndex));

        GregorianCalendar selectedMonth = new GregorianCalendar();
        selectedMonth.set(Calendar.MONTH, monthIndex - 1);
        model.addAttribute("selectedMonth", selectedMonth);
        String[] monthsNames = {"Січень", "Лютий", "Березень", "Квітень", "Травень", "Червень", "Липень", "Серпень",
                "Вересень", "Жовтень", "Листопад", "Грудень"};
        String[] availableMarks = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};
        model.addAttribute("monthsNames", monthsNames);
        model.addAttribute("availableMarks", availableMarks);
        return "journalEditing";
    }

    @PostMapping("/teacher/editJournal-{subjectId}-{bandId}-{monthIndex}")
    public String editJournal(@PathVariable("subjectId") int subjectId,
                              @PathVariable("bandId") int bandId,
                              @PathVariable("monthIndex") int monthIndex,
                              @RequestParam Map<String, String> requestParam) {
        noteService.editJournal(requestParam);
        return "redirect:/teacher/journalEditing-" + subjectId + "-" + bandId + "-" + monthIndex;
    }
}