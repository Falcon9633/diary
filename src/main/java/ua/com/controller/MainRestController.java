package ua.com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ua.com.entity.Band;
import ua.com.entity.Student;
import ua.com.entity.Subject;
import ua.com.service.BandService;
import ua.com.service.SubjectService;

import java.util.List;
import java.util.Set;

@RestController
public class MainRestController {
    @Autowired
    private BandService bandService;

    @Autowired
    private SubjectService subjectService;

    @GetMapping("/getAllBands")
    public List<Band> getAllBands() {
        return bandService.findAllWithAllNested();
    }

    @RequestMapping(value = "/editBand", method = RequestMethod.POST)
    public void editBand(@RequestBody Band band) {
        bandService.save(band);
    }

    @GetMapping("/getAllSubjects")
    public List<Subject> getAllSubjects(){
        return subjectService.findAllWithAllNested();
    }

    @RequestMapping(value = "/editSubject", method = RequestMethod.POST)
    public void editSubject(@RequestBody Subject subject){
        subjectService.save(subject);
    }
}
