package config;

import org.example.config.AppConfig;
import org.example.facade.GymFacade;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class SpringContextTest {

    @Test
    void shouldLoadSpringContext() {
        try (AnnotationConfigApplicationContext context =
                     new AnnotationConfigApplicationContext(AppConfig.class)) {

            GymFacade gymFacade =
                    context.getBean(GymFacade.class);

            assertNotNull(gymFacade);
        }
    }
}