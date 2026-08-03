package org.example.storage;

import org.example.model.Trainee;
import org.example.model.Trainer;
import org.example.model.Training;
import org.example.model.TrainingType;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Component
public class StorageInitializationBeanPostProcessor
        implements BeanPostProcessor, EnvironmentAware, ResourceLoaderAware {

    private static final String STORAGE_FILE_PROPERTY =
            "storage.data.file";

    private Environment environment;
    private ResourceLoader resourceLoader;
    private List<String> dataLines;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public Object postProcessAfterInitialization(
            Object bean,
            String beanName
    ) throws BeansException {

        switch (beanName) {
            case "traineeStorage" ->
                    initializeTraineeStorage(bean);

            case "trainerStorage" ->
                    initializeTrainerStorage(bean);

            case "trainingStorage" ->
                    initializeTrainingStorage(bean);

            default -> {
            }
        }

        return bean;
    }

    @SuppressWarnings("unchecked")
    private void initializeTraineeStorage(Object bean) {
        Map<Long, Trainee> storage =
                (Map<Long, Trainee>) bean;

        getDataLines().stream()
                .filter(line -> line.startsWith("TRAINEE|"))
                .map(this::parseTrainee)
                .forEach(trainee ->
                        storage.put(
                                trainee.getUserId(),
                                trainee
                        )
                );
    }

    @SuppressWarnings("unchecked")
    private void initializeTrainerStorage(Object bean) {
        Map<Long, Trainer> storage =
                (Map<Long, Trainer>) bean;

        getDataLines().stream()
                .filter(line -> line.startsWith("TRAINER|"))
                .map(this::parseTrainer)
                .forEach(trainer ->
                        storage.put(
                                trainer.getUserId(),
                                trainer
                        )
                );
    }

    @SuppressWarnings("unchecked")
    private void initializeTrainingStorage(Object bean) {
        Map<Long, Training> storage =
                (Map<Long, Training>) bean;

        getDataLines().stream()
                .filter(line -> line.startsWith("TRAINING|"))
                .map(this::parseTraining)
                .forEach(training ->
                        storage.put(
                                training.getTrainingId(),
                                training
                        )
                );
    }

    private Trainee parseTrainee(String line) {
        String[] values = line.split("\\|", -1);

        validateColumnCount(values, 9, line);

        return new Trainee(
                Long.parseLong(values[1]),
                values[2],
                values[3],
                values[4],
                values[5],
                Boolean.parseBoolean(values[6]),
                LocalDate.parse(values[7]),
                values[8]
        );
    }

    private Trainer parseTrainer(String line) {
        String[] values = line.split("\\|", -1);

        validateColumnCount(values, 8, line);

        return new Trainer(
                Long.parseLong(values[1]),
                values[2],
                values[3],
                values[4],
                values[5],
                Boolean.parseBoolean(values[6]),
                TrainingType.valueOf(values[7])
        );
    }

    private Training parseTraining(String line) {
        String[] values = line.split("\\|", -1);

        validateColumnCount(values, 8, line);

        return new Training(
                Long.parseLong(values[1]),
                Long.parseLong(values[2]),
                Long.parseLong(values[3]),
                values[4],
                TrainingType.valueOf(values[5]),
                LocalDate.parse(values[6]),
                Duration.parse(values[7])
        );
    }

    private List<String> getDataLines() {
        if (dataLines == null) {
            dataLines = loadDataLines();
        }

        return dataLines;
    }

    private List<String> loadDataLines() {
        String location = environment.getRequiredProperty(
                STORAGE_FILE_PROPERTY
        );

        Resource resource =
                resourceLoader.getResource(location);

        if (!resource.exists()) {
            throw new BeanInitializationException(
                    "Storage initialization file does not exist: "
                            + location
            );
        }

        try (
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(
                                resource.getInputStream(),
                                StandardCharsets.UTF_8
                        )
                )
        ) {
            return reader.lines()
                    .map(String::trim)
                    .filter(line -> !line.isEmpty())
                    .filter(line -> !line.startsWith("#"))
                    .toList();
        } catch (IOException exception) {
            throw new BeanInitializationException(
                    "Cannot read storage initialization file: "
                            + location,
                    exception
            );
        }
    }

    private void validateColumnCount(
            String[] values,
            int expectedCount,
            String line
    ) {
        if (values.length != expectedCount) {
            throw new BeanInitializationException(
                    "Invalid storage data line. Expected "
                            + expectedCount
                            + " values, but received "
                            + values.length
                            + ": "
                            + line
            );
        }
    }
}