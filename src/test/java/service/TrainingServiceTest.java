package service;

import org.example.dao.TrainingDao;
import org.example.model.Training;
import org.example.model.TrainingTypeName;
import org.example.service.TrainingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainingServiceTest {

    @Mock
    private TrainingDao trainingDao;

    @InjectMocks
    private TrainingService trainingService;

    @Test
    void shouldFindTrainingById() {
        Training training = createTraining();

        when(trainingDao.findById(1L))
                .thenReturn(Optional.of(training));

        Training result = trainingService.findById(1L);

        assertSame(training, result);
        verify(trainingDao).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenTrainingDoesNotExist() {
        when(trainingDao.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                NoSuchElementException.class,
                () -> trainingService.findById(999L)
        );

        verify(trainingDao).findById(999L);
    }

    @Test
    void shouldReturnAllTrainings() {
        Training first = createTraining();

        Training second = new Training(
                2L,
                11L,
                21L,
                "Cardio training",
                TrainingTypeName.CARDIO,
                LocalDate.of(2026, 8, 11),
                Duration.ofMinutes(45)
        );

        when(trainingDao.findAll())
                .thenReturn(List.of(first, second));

        List<Training> result = trainingService.findAll();

        assertEquals(2, result.size());
        assertTrue(result.contains(first));
        assertTrue(result.contains(second));

        verify(trainingDao).findAll();
    }

    @Test
    void shouldCreateTraining() {
        LocalDate trainingDate = LocalDate.of(2026, 8, 10);
        Duration trainingDuration = Duration.ofMinutes(60);

        when(idGenerator.nextTrainingId()).thenReturn(1L);
        when(trainingDao.save(any(Training.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Training result = trainingService.create(
                10L,
                20L,
                "Strength training",
                TrainingTypeName.STRENGTH,
                trainingDate,
                trainingDuration
        );

        assertEquals(1L, result.getTrainingId());
        assertEquals(10L, result.getTraineeId());
        assertEquals(20L, result.getTrainerId());
        assertEquals(
                "Strength training",
                result.getTrainingName()
        );
        assertEquals(
                TrainingTypeName.STRENGTH,
                result.getTrainingType()
        );
        assertEquals(trainingDate, result.getTrainingDate());
        assertEquals(
                trainingDuration,
                result.getTrainingDuration()
        );

        verify(idGenerator).nextTrainingId();
        verify(trainingDao).save(result);
    }

    private Training createTraining() {
        return new Training(
                1L,
                10L,
                20L,
                "Strength training",
                TrainingTypeName.STRENGTH,
                LocalDate.of(2026, 8, 10),
                Duration.ofMinutes(60)
        );
    }
}