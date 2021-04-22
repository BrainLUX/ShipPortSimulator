package com.dmitriev.oop.web;

import com.dmitriev.oop.service.MainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/main")
public class MainController {

    private MainService mainService;

    @GetMapping("/timetable")
    public ResponseEntity<String> getTimetable() {
        return new ResponseEntity<>(mainService.getTimetable(), HttpStatus.OK);
    }

    @GetMapping("/timetable/{fileName}")
    public ResponseEntity<String> getTimetableByFile(@PathVariable("fileName") String fileName) {
        return new ResponseEntity<>(mainService.getTimetableByFile(fileName), HttpStatus.OK);
    }

    @PostMapping("/statistic")
    public ResponseEntity<String> saveStatistic(@RequestBody String statisticObject) {
        return new ResponseEntity<>(mainService.saveStatistic(statisticObject), HttpStatus.OK);
    }

    @Autowired
    public void setApplicationService(MainService mainService) {
        this.mainService = mainService;
    }
}
