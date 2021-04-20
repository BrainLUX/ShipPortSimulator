package com.dmitriev.oop.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "timetable not found")
public class TimetableNotFoundException extends RuntimeException {
}
