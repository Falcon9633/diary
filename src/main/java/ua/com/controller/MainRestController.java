package ua.com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ua.com.entity.Band;
import ua.com.service.BandService;

import java.util.List;

@RestController
public class MainRestController {
    @Autowired
    private BandService bandService;

    @GetMapping("/getAllBands")
    public List<Band> getAllBands() {
        return bandService.findAllWithAllNested();
    }

    @RequestMapping(value = "/editBand", method = RequestMethod.POST)
    public void editBand(@RequestBody Band band) {
        bandService.save(band);
    }
}
