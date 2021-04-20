package com.dmitriev.oop.service;

import com.dmitriev.oop.entity.Application;

import java.util.List;

public interface ApplicationService {
    List<Application> listApplication();

    Application findApplication(long id);

    Application addApplication(Application application);
}
