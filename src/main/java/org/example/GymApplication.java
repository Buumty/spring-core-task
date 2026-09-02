package org.example;

import org.example.config.AppConfig;
import org.example.facade.GymFacade;
import org.example.model.Trainee;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.time.LocalDate;

public class GymApplication {

    public static void main(String[] args) {

        try (AnnotationConfigApplicationContext context =
                     new AnnotationConfigApplicationContext(AppConfig.class)) {

            GymFacade gymFacade =
                    context.getBean(GymFacade.class);

            System.out.println("Spring context started successfully.");

            Trainee trainee = gymFacade.createTrainee(
                    "John",
                    "Smith",
                    LocalDate.of(1995, 5, 10),
                    "Example address"
            );

            System.out.println(
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