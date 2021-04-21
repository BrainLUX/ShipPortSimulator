package com.dmitriev.oop.web;

import com.dmitriev.oop.entity.Ship;
import com.dmitriev.oop.service.TimetableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/timetable")
public class TimetableController {

    private TimetableService timetableService;

    @GetMapping("/generate")
    public ResponseEntity<List<Ship>> generateTimetable() {
        return new ResponseEntity<>(timetableService.generate(System.currentTimeMillis()), HttpStatus.OK);
    }

    @GetMapping("/get")
    public ResponseEntity<List<Ship>> getTimetable() {
        return new ResponseEntity<>(timetableService.get(), HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<Boolean> addShip(@RequestBody Ship ship, @RequestParam String fileName) {
        return new ResponseEntity<>(timetableService.add(ship, fileName), HttpStatus.OK);
    }

    @Autowired
    public void setApplicationService(TimetableService timetableService) {
        this.timetableService = timetableService;
    }
}
