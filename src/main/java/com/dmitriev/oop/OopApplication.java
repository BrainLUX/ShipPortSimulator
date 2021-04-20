package com.dmitriev.oop;

import com.dmitriev.oop.entity.Application;
import com.dmitriev.oop.repository.ApplicationRepository;
import com.dmitriev.oop.service.TimetableService;
import com.port.controller.Program;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class OopApplication {

    private static final Logger log = LoggerFactory.getLogger(OopApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(OopApplication.class, args);
        Program.start();
    }

    @Bean
    public CommandLineRunner test(ApplicationRepository repository) {
        return args -> {
            repository.save(new Application("FirstApp", "My first app"));
            repository.save(new Application("SecondApp", "My second app"));

            for (Application app : repository.findAll()) {
                log.info("The application is:" + app.toString());
            }
        };
    }

}
