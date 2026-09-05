package dao.impl;

import org.example.dao.impl.TrainingDaoImpl;
import org.example.model.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainingDaoImplTest {

    @Mock
    private SessionFactory sessionFactory;

    @Mock
    private Session session;

    @Mock
    private Query<Training> trainingQuery;

    @InjectMocks
    private TrainingDaoImpl trainingDao;

    @BeforeEach
    void setUp() {
        when(sessionFactory.getCurrentSession())
                .thenReturn(session);
    }

    @Test
    void shouldSaveTraining() {
        Training training = createTraining();

        Training result = trainingDao.save(training);

        assertSame(training, result);

        verify(session).persist(training);
    }

    @Test
    void shouldFindTrainingById() {
        Training training = createTraining();

        when(session.find(
                Training.class,
                1L
        )).thenReturn(training);

        Optional<Training> result =
                trainingDao.findById(1L);

        assertTrue(result.isPresent());
        assertSame(training, result.get());

        verify(session).find(
                Training.class,
                1L
        );
    }

    @Test
    void shouldReturnEmptyOptionalWhenTrainingDoesNotExist() {
        when(session.find(
                Training.class,
                999L
        )).thenReturn(null);

        Optional<Training> result =
                trainingDao.findById(999L);

        assertTrue(result.isEmpty());

        verify(session).find(
                Training.class,
                999L
        );
    }

    @Test
    void shouldReturnAllTrainings() {
        Training first = createTraining();
        Training second = createTraining();

        when(session.createQuery(
                "FROM Training",
                Training.class
        )).thenReturn(trainingQuery);

        when(trainingQuery.getResultList())
                .thenReturn(List.of(first, second));

        List<Training> result =
                trainingDao.findAll();

        assertEquals(2, result.size());
        assertTrue(result.contains(first));
        assertTrue(result.contains(second));

        verify(trainingQuery).getResultList();
    }

    @Test
    void shouldReturnEmptyListWhenNoTrainingsExist() {
        when(session.createQuery(
                "FROM Training",
                Training.class
        )).thenReturn(trainingQuery);

        when(trainingQuery.getResultList())
                .thenReturn(List.of());

        List<Training> result =
                trainingDao.findAll();

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldFindTraineeTrainingsWithoutOptionalFilters() {
        Training training = createTraining();

        when(session.createQuery(
                anyString(),
                eq(Training.class)
        )).thenReturn(trainingQuery);

        when(trainingQuery.setParameter(
                "traineeUsername",
                "John.Smith"
        )).thenReturn(trainingQuery);

        when(trainingQuery.getResultList())
                .thenReturn(List.of(training));

        List<Training> result =
                trainingDao.findTraineeTrainings(
                        "John.Smith",
                        null,
                        null,
                        null,
                        null
                );

        assertEquals(1, result.size());
        assertSame(training, result.get(0));

        verify(trainingQuery).setParameter(
                "traineeUsername",
                "John.Smith"
        );

        verify(trainingQuery, never())
                .setParameter(eq("fromDate"), any());

        verify(trainingQuery, never())
                .setParameter(eq("toDate"), any());

        verify(trainingQuery, never())
                .setParameter(eq("trainerName"), any());

        verify(trainingQuery, never())
                .setParameter(eq("trainingType"), any());
    }

    @Test
    void shouldFindTraineeTrainingsWithAllFilters() {
        Training training = createTraining();

        LocalDate fromDate =
                LocalDate.of(2026, 8, 1);

        LocalDate toDate =
                LocalDate.of(2026, 8, 31);

        when(session.createQuery(
                anyString(),
                eq(Training.class)
        )).thenReturn(trainingQuery);

        when(trainingQuery.setParameter(
                anyString(),
                any()
        )).thenReturn(trainingQuery);

        when(trainingQuery.getResultList())
                .thenReturn(List.of(training));

        List<Training> result =
                trainingDao.findTraineeTrainings(
                        "John.Smith",
                        fromDate,
                        toDate,
                        "Anna",
                        TrainingTypeName.STRENGTH
                );

        assertEquals(1, result.size());

        verify(trainingQuery).setParameter(
                "traineeUsername",
                "John.Smith"
        );

        verify(trainingQuery).setParameter(
                "fromDate",
                fromDate
        );

        verify(trainingQuery).setParameter(
                "toDate",
                toDate
        );

        verify(trainingQuery).setParameter(
                "trainerName",
                "Anna"
        );

        verify(trainingQuery).setParameter(
                "trainingType",
                TrainingTypeName.STRENGTH
        );
    }

    @Test
    void shouldFindTrainerTrainingsWithoutOptionalFilters() {
        Training training = createTraining();

        when(session.createQuery(
                anyString(),
                eq(Training.class)
        )).thenReturn(trainingQuery);

        when(trainingQuery.setParameter(
                "trainerUsername",
                "Anna.Brown"
        )).thenReturn(trainingQuery);

        when(trainingQuery.getResultList())
                .thenReturn(List.of(training));

        List<Training> result =
                trainingDao.findTrainerTrainings(
                        "Anna.Brown",
                        null,
                        null,
                        null
                );

        assertEquals(1, result.size());
        assertSame(training, result.get(0));

        verify(trainingQuery).setParameter(
                "trainerUsername",
                "Anna.Brown"
        );

        verify(trainingQuery, never())
                .setParameter(eq("fromDate"), any());

        verify(trainingQuery, never())
                .setParameter(eq("toDate"), any());

        verify(trainingQuery, never())
                .setParameter(eq("traineeName"), any());
    }

    @Test
    void shouldFindTrainerTrainingsWithAllFilters() {
        Training training = createTraining();

        LocalDate fromDate =
                LocalDate.of(2026, 8, 1);

        LocalDate toDate =
                LocalDate.of(2026, 8, 31);

        when(session.createQuery(
                anyString(),
                eq(Training.class)
        )).thenReturn(trainingQuery);

        when(trainingQuery.setParameter(
                anyString(),
                any()
        )).thenReturn(trainingQuery);

        when(trainingQuery.getResultList())
                .thenReturn(List.of(training));

        List<Training> result =
                trainingDao.findTrainerTrainings(
                        "Anna.Brown",
                        fromDate,
                        toDate,
                        "John Smith"
                );

        assertEquals(1, result.size());

        verify(trainingQuery).setParameter(
                "trainerUsername",
                "Anna.Brown"
        );

        verify(trainingQuery).setParameter(
                "fromDate",
                fromDate
        );

        verify(trainingQuery).setParameter(
                "toDate",
                toDate
        );

        verify(trainingQuery).setParameter(
                "traineeName",
                "John Smith"
        );
    }

    private Training createTraining() {

        User traineeUser = new User(
                "John",
                "Smith",
                "John.Smith",
                "abcdefghij",
                true
        );

        Trainee trainee = new Trainee(
                traineeUser,
                LocalDate.of(1995, 5, 10),
                "Example address"
        );

        TrainingType trainingType =
                new TrainingType(
                        TrainingTypeName.STRENGTH
                );

        User trainerUser = new User(
                "Anna",
                "Brown",
                "Anna.Brown",
                "abcdefghij",
                true
        );

        Trainer trainer = new Trainer(
                trainingType,
                trainerUser
        );

        return new Training(
                trainee,
                trainer,
                "Strength training",
                trainingType,
                LocalDate.of(2026, 8, 10),
                60
        );
    }
}