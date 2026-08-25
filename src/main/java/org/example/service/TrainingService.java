package org.example.service;

import org.example.dao.TrainingDao;
import org.example.model.Training;
import org.example.model.TrainingTypeName;
import org.example.service.generator.IdGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.List;

@Service
public class TrainingService {
    private static final Logger log =
            LoggerFactory.getLogger(TrainingService.class);

    private final TrainingDao trainingDao;
    private final IdGenerator idGenerator;

    public TrainingService(TrainingDao trainingDao, IdGenerator idGenerator) {
        this.trainingDao = trainingDao;
        this.idGenerator = idGenerator;
    }

    public Training findById(long id) {
        log.debug("Searching for training with id={}", id);
        return trainingDao.findById(id).orElseThrow(() -> {
            log.warn("Training with id={} was not found", id);
            return new NoSuchElementException("Training with id " + id + " was not found");
        });
    }

    public List<Training> findAll() {
        log.debug("Retrieving all trainings");
        return trainingDao.findAll();
    }

    public Training create(long traineeId, long trainerId, String trainingName, TrainingTypeName trainingType, LocalDate trainingDate, Duration trainingDuration) {
        Training savedTraining = trainingDao.save(new Training(
                idGenerator.nextTrainingId(),
                traineeId,
                trainerId,
                trainingName,
                trainingType,
                trainingDate,
                trainingDuration
        ));

        log.info(
                "Created training id={}, traineeId={}, trainerId={}, name={}",
                savedTraining.getTrainingId(),
                savedTraining.getTraineeId(),
                savedTraining.getTrainerId(),
                savedTraining.getTrainingName()
        );
        return savedTraining;
    }
}
