package dao.impl;

import org.example.dao.impl.TrainerDaoImpl;
import org.example.model.Trainer;
import org.example.model.TrainingType;
import org.example.model.TrainingTypeName;
import org.example.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainerDaoImplTest {

    @Mock
    private SessionFactory sessionFactory;

    @Mock
    private Session session;

    @Mock
    private Query<Trainer> trainerQuery;

    @Mock
    private Query<Long> countQuery;

    @InjectMocks
    private TrainerDaoImpl trainerDao;

    @BeforeEach
    void setUp() {
        when(sessionFactory.getCurrentSession())
                .thenReturn(session);
    }

    @Test
    void shouldSaveTrainer() {
        Trainer trainer = createTrainer(
                "John",
                "Smith",
                "John.Smith",
                TrainingTypeName.STRENGTH
        );

        Trainer result = trainerDao.save(trainer);

        assertSame(trainer, result);

        verify(session).persist(trainer);
    }

    @Test
    void shouldFindTrainerById() {
        Trainer trainer = createTrainer(
                "John",
                "Smith",
                "John.Smith",
                TrainingTypeName.STRENGTH
        );

        when(session.find(
                Trainer.class,
                1L
        )).thenReturn(trainer);

        Optional<Trainer> result =
                trainerDao.findById(1L);

        assertTrue(result.isPresent());
        assertSame(trainer, result.get());

        verify(session).find(
                Trainer.class,
                1L
        );
    }

    @Test
    void shouldReturnEmptyOptionalWhenTrainerDoesNotExist() {
        when(session.find(
                Trainer.class,
                999L
        )).thenReturn(null);

        Optional<Trainer> result =
                trainerDao.findById(999L);

        assertTrue(result.isEmpty());

        verify(session).find(
                Trainer.class,
                999L
        );
    }

    @Test
    void shouldReturnAllTrainers() {
        Trainer first = createTrainer(
                "John",
                "Smith",
                "John.Smith",
                TrainingTypeName.STRENGTH
        );

        Trainer second = createTrainer(
                "Anna",
                "Brown",
                "Anna.Brown",
                TrainingTypeName.YOGA
        );

        when(session.createQuery(
                anyString(),
                eq(Trainer.class)
        )).thenReturn(trainerQuery);

        when(trainerQuery.getResultList())
                .thenReturn(List.of(first, second));

        List<Trainer> result =
                trainerDao.findAll();

        assertEquals(2, result.size());
        assertTrue(result.contains(first));
        assertTrue(result.contains(second));

        verify(trainerQuery).getResultList();
    }

    @Test
    void shouldReturnEmptyListWhenNoTrainersExist() {
        when(session.createQuery(
                anyString(),
                eq(Trainer.class)
        )).thenReturn(trainerQuery);

        when(trainerQuery.getResultList())
                .thenReturn(List.of());

        List<Trainer> result =
                trainerDao.findAll();

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnTrueWhenUsernameExists() {
        when(session.createQuery(
                anyString(),
                eq(Long.class)
        )).thenReturn(countQuery);

        when(countQuery.setParameter(
                "username",
                "John.Smith"
        )).thenReturn(countQuery);

        when(countQuery.getSingleResult())
                .thenReturn(1L);

        boolean result =
                trainerDao.existsByUsername(
                        "John.Smith"
                );

        assertTrue(result);

        verify(countQuery).setParameter(
                "username",
                "John.Smith"
        );
    }

    @Test
    void shouldReturnFalseWhenUsernameDoesNotExist() {
        when(session.createQuery(
                anyString(),
                eq(Long.class)
        )).thenReturn(countQuery);

        when(countQuery.setParameter(
                "username",
                "Unknown.User"
        )).thenReturn(countQuery);

        when(countQuery.getSingleResult())
                .thenReturn(0L);

        boolean result =
                trainerDao.existsByUsername(
                        "Unknown.User"
                );

        assertFalse(result);
    }

    @Test
    void shouldFindTrainerByUsername() {
        Trainer trainer = createTrainer(
                "John",
                "Smith",
                "John.Smith",
                TrainingTypeName.STRENGTH
        );

        when(session.createQuery(
                anyString(),
                eq(Trainer.class)
        )).thenReturn(trainerQuery);

        when(trainerQuery.setParameter(
                "username",
                "John.Smith"
        )).thenReturn(trainerQuery);

        when(trainerQuery.uniqueResultOptional())
                .thenReturn(Optional.of(trainer));

        Optional<Trainer> result =
                trainerDao.findByUsername(
                        "John.Smith"
                );

        assertTrue(result.isPresent());
        assertSame(trainer, result.get());

        verify(trainerQuery).setParameter(
                "username",
                "John.Smith"
        );
    }

    @Test
    void shouldReturnEmptyWhenUsernameDoesNotExist() {
        when(session.createQuery(
                anyString(),
                eq(Trainer.class)
        )).thenReturn(trainerQuery);

        when(trainerQuery.setParameter(
                "username",
                "Unknown.User"
        )).thenReturn(trainerQuery);

        when(trainerQuery.uniqueResultOptional())
                .thenReturn(Optional.empty());

        Optional<Trainer> result =
                trainerDao.findByUsername(
                        "Unknown.User"
                );

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldFindTrainersNotAssignedToTrainee() {
        Trainer first = createTrainer(
                "John",
                "Smith",
                "John.Smith",
                TrainingTypeName.STRENGTH
        );

        Trainer second = createTrainer(
                "Anna",
                "Brown",
                "Anna.Brown",
                TrainingTypeName.YOGA
        );

        when(session.createQuery(
                anyString(),
                eq(Trainer.class)
        )).thenReturn(trainerQuery);

        when(trainerQuery.setParameter(
                "traineeUsername",
                "Mike.Jones"
        )).thenReturn(trainerQuery);

        when(trainerQuery.getResultList())
                .thenReturn(List.of(first, second));

        List<Trainer> result =
                trainerDao.findNotAssignedToTrainee(
                        "Mike.Jones"
                );

        assertEquals(2, result.size());
        assertTrue(result.contains(first));
        assertTrue(result.contains(second));

        verify(trainerQuery).setParameter(
                "traineeUsername",
                "Mike.Jones"
        );
    }

    @Test
    void shouldFindTrainersByUsernames() {
        Set<String> usernames = Set.of(
                "John.Smith",
                "Anna.Brown"
        );

        Trainer first = createTrainer(
                "John",
                "Smith",
                "John.Smith",
                TrainingTypeName.STRENGTH
        );

        Trainer second = createTrainer(
                "Anna",
                "Brown",
                "Anna.Brown",
                TrainingTypeName.YOGA
        );

        when(session.createQuery(
                anyString(),
                eq(Trainer.class)
        )).thenReturn(trainerQuery);

        when(trainerQuery.setParameterList(
                "usernames",
                usernames
        )).thenReturn(trainerQuery);

        when(trainerQuery.getResultList())
                .thenReturn(List.of(first, second));

        List<Trainer> result =
                trainerDao.findByUsernames(usernames);

        assertEquals(2, result.size());
        assertTrue(result.contains(first));
        assertTrue(result.contains(second));

        verify(trainerQuery).setParameterList(
                "usernames",
                usernames
        );
    }

    private Trainer createTrainer(
            String firstName,
            String lastName,
            String username,
            TrainingTypeName specialization
    ) {
        User user = new User(
                firstName,
                lastName,
                username,
                "abcdefghij",
                true
        );

        TrainingType trainingType =
                new TrainingType(specialization);

        return new Trainer(
                trainingType,
                user
        );
    }
}