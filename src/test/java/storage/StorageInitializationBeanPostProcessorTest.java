package storage;

import org.example.model.Trainee;
import org.example.model.Trainer;
import org.example.model.Training;
import org.example.model.TrainingType;
import org.example.storage.StorageInitializationBeanPostProcessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.DefaultResourceLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class StorageInitializationBeanPostProcessorTest {

    @TempDir
    Path tempDir;

    private StorageInitializationBeanPostProcessor processor;
    private StandardEnvironment environment;

    @BeforeEach
    void setUp() {
        processor = new StorageInitializationBeanPostProcessor();

        environment = new StandardEnvironment();

        processor.setEnvironment(environment);
        processor.setResourceLoader(new DefaultResourceLoader());
    }

    @Test
    void shouldInitializeTraineeStorage() throws IOException {
        Path file = createDataFile();

        setStorageFileProperty(file);

        Map<Long, Trainee> storage = new HashMap<>();

        Object result = processor.postProcessAfterInitialization(
                storage,
                "traineeStorage"
        );

        assertSame(storage, result);
        assertEquals(1, storage.size());

        Trainee trainee = storage.get(1L);

        assertNotNull(trainee);
        assertEquals("John", trainee.getFirstName());
        assertEquals("Smith", trainee.getLastName());
        assertEquals("John.Smith", trainee.getUsername());
        assertTrue(trainee.isActive());
        assertEquals(
                LocalDate.of(1995, 5, 10),
                trainee.getDateOfBirth()
        );
        assertEquals("Example address", trainee.getAddress());
    }

    @Test
    void shouldInitializeTrainerStorage() throws IOException {
        Path file = createDataFile();

        setStorageFileProperty(file);

        Map<Long, Trainer> storage = new HashMap<>();

        processor.postProcessAfterInitialization(
                storage,
                "trainerStorage"
        );

        assertEquals(1, storage.size());

        Trainer trainer = storage.get(2L);

        assertNotNull(trainer);
        assertEquals("Anna", trainer.getFirstName());
        assertEquals("Brown", trainer.getLastName());
        assertEquals("Anna.Brown", trainer.getUsername());
        assertTrue(trainer.isActive());
        assertEquals(
                TrainingType.YOGA,
                trainer.getSpecialization()
        );
    }

    @Test
    void shouldInitializeTrainingStorage() throws IOException {
        Path file = createDataFile();

        setStorageFileProperty(file);

        Map<Long, Training> storage = new HashMap<>();

        processor.postProcessAfterInitialization(
                storage,
                "trainingStorage"
        );

        assertEquals(1, storage.size());

        Training training = storage.get(1L);

        assertNotNull(training);
        assertEquals(1L, training.getTraineeId());
        assertEquals(2L, training.getTrainerId());
        assertEquals("Morning yoga", training.getTrainingName());
        assertEquals(
                TrainingType.YOGA,
                training.getTrainingType()
        );
        assertEquals(
                LocalDate.of(2026, 8, 10),
                training.getTrainingDate()
        );
        assertEquals(
                Duration.ofHours(1),
                training.getTrainingDuration()
        );
    }

    @Test
    void shouldIgnoreUnknownBean() throws IOException {
        Path file = createDataFile();

        setStorageFileProperty(file);

        Object bean = new Object();

        Object result = processor.postProcessAfterInitialization(
                bean,
                "someOtherBean"
        );

        assertSame(bean, result);
    }

    @Test
    void shouldThrowExceptionWhenFileDoesNotExist() {
        environment.getPropertySources().addFirst(
                new MapPropertySource(
                        "testProperties",
                        Map.of(
                                "storage.data.file",
                                "file:/file-that-does-not-exist.txt"
                        )
                )
        );

        Map<Long, Trainee> storage = new HashMap<>();

        assertThrows(
                BeanInitializationException.class,
                () -> processor.postProcessAfterInitialization(
                        storage,
                        "traineeStorage"
                )
        );
    }

    @Test
    void shouldThrowExceptionWhenDataHasInvalidColumnCount()
            throws IOException {

        Path file = tempDir.resolve("invalid-data.txt");

        Files.writeString(
                file,
                "TRAINEE|1|John|Smith"
        );

        setStorageFileProperty(file);

        Map<Long, Trainee> storage = new HashMap<>();

        assertThrows(
                BeanInitializationException.class,
                () -> processor.postProcessAfterInitialization(
                        storage,
                        "traineeStorage"
                )
        );
    }

    private Path createDataFile() throws IOException {
        Path file = tempDir.resolve("initial-data.txt");

        Files.writeString(
                file,
                """
                # Test data

                TRAINEE|1|John|Smith|John.Smith|abcdefghij|true|1995-05-10|Example address
                TRAINER|2|Anna|Brown|Anna.Brown|qwertyuiop|true|YOGA
                TRAINING|1|1|2|Morning yoga|YOGA|2026-08-10|PT1H
                """
        );

        return file;
    }

    private void setStorageFileProperty(Path file) {
        environment.getPropertySources().addFirst(
                new MapPropertySource(
                        "testProperties",
                        Map.of(
                                "storage.data.file",
                                file.toUri().toString()
                        )
                )
        );
    }
}