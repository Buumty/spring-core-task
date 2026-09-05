package org.example;

import org.example.config.AppConfig;
import org.example.facade.GymFacade;
import org.example.model.Trainee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.time.LocalDate;

public class GymApplication {

    private static final Logger log = LoggerFactory.getLogger(GymApplication.class);

    public static void main(String[] args) {

        try (AnnotationConfigApplicationContext context =
                     new AnnotationConfigApplicationContext(AppConfig.class)) {

            GymFacade gymFacade =
                    context.getBean(GymFacade.class);

            log.debug("Spring context started successfully.");

            Trainee trainee = gymFacade.createTrainee(
                    "John",
                    "Smith",
                    LocalDate.of(1995, 5, 10),
                    "Example address"
            );

            log.info(
                    "Created trainee: "
                            + trainee.getUser().getUsername()
            );

            Trainee foundTrainee =
                    gymFacade.findTraineeByUsername(
                            trainee.getUser().getUsername(),
                            trainee.getUser().getPassword()
                    );

            System.out.println(
                    "Found trainee: "
                            + foundTrainee.getUser().getUsername()
            );
        }
    }
}