package config;

import org.example.config.AppConfig;
import org.example.facade.GymFacade;
import org.example.service.TraineeService;
import org.example.service.TrainerService;
import org.example.service.TrainingService;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SpringContextTest {

    @Test
    void shouldStartSpringContext() {
        try (AnnotationConfigApplicationContext context =
                     new AnnotationConfigApplicationContext(AppConfig.class)) {

            assertNotNull(context);
            assertTrue(context.isActive());
        }
    }

    @Test
    void shouldCreateFacadeAndServices() {
        try (AnnotationConfigApplicationContext context =
                     new AnnotationConfigApplicationContext(AppConfig.class)) {

            assertNotNull(
                    context.getBean(GymFacade.class)
            );

            assertNotNull(
                    context.getBean(TraineeService.class)
            );

            assertNotNull(
                    context.getBean(TrainerService.class)
            );

            assertNotNull(
                    context.getBean(TrainingService.class)
            );
        }
    }

    @Test
    void shouldInitializeStoragesFromFile() {
        try (AnnotationConfigApplicationContext context =
                     new AnnotationConfigApplicationContext(AppConfig.class)) {

            Map<?, ?> traineeStorage =
                    context.getBean(
                            "traineeStorage",
                            Map.class
                    );

            Map<?, ?> trainerStorage =
                    context.getBean(
                            "trainerStorage",
                            Map.class
                    );

            Map<?, ?> trainingStorage =
                    context.getBean(
                            "trainingStorage",
                            Map.class
                    );

            assertFalse(traineeStorage.isEmpty());
            assertFalse(trainerStorage.isEmpty());
            assertFalse(trainingStorage.isEmpty());
        }
    }
}