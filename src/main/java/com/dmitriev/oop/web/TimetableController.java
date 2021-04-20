package com.dmitriev.oop.web;

import com.dmitriev.oop.service.ApplicationService;
import com.dmitriev.oop.service.TicketService;
import com.dmitriev.oop.entity.Ship;
import com.dmitriev.oop.service.TimetableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @Autowired
    public void setApplicationService(TimetableService timetableService) {
        this.timetableService = timetableService;
    }
}
