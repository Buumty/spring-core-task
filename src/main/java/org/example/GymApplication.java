package org.example;

import org.example.config.AppConfig;
import org.example.facade.GymFacade;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class GymApplication {

    public static void main(String[] args) {
        try (AnnotationConfigApplicationContext context =
                     new AnnotationConfigApplicationContext(AppConfig.class)) {

            GymFacade gymFacade = context.getBean(GymFacade.class);

            System.out.println(
                    "Trainees: " + gymFacade.findAllTrainees().size()
            );
            System.out.println(
                    "Trainers: " + gymFacade.findAllTrainers().size()
            );
            System.out.println(
                    "Trainings: " + gymFacade.findAllTrainings().size()
            );
        }
    }
}