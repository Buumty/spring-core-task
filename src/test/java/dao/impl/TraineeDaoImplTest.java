package dao.impl;

import org.example.dao.impl.TraineeDaoImpl;
import org.example.model.Trainee;
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

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TraineeDaoImplTest {

    @Mock
    private SessionFactory sessionFactory;

    @Mock
    private Session session;

    @Mock
    private Query<Trainee> traineeQuery;

    @Mock
    private Query<Long> countQuery;

    @InjectMocks
    private TraineeDaoImpl traineeDao;

    @BeforeEach
    void setUp() {
        when(sessionFactory.getCurrentSession())
                .thenReturn(session);
    }

    @Test
    void shouldSaveTrainee() {
        Trainee trainee = createTrainee(
                "John",
                "Smith",
                "John.Smith"
        );

        Trainee result = traineeDao.save(trainee);

        assertSame(trainee, result);

        verify(session).persist(trainee);
    }

    @Test
    void shouldFindTraineeById() {
        Trainee trainee = createTrainee(
                "John",
                "Smith",
                "John.Smith"
        );

        when(session.find(Trainee.class, 1L))
                .thenReturn(trainee);

        Optional<Trainee> result =
                traineeDao.findById(1L);

        assertTrue(result.isPresent());
        assertSame(trainee, result.get());

        verify(session).find(
                Trainee.class,
                1L
        );
    }

    @Test
    void shouldReturnEmptyOptionalWhenTraineeDoesNotExist() {
        when(session.find(Trainee.class, 999L))
                .thenReturn(null);

        Optional<Trainee> result =
                traineeDao.findById(999L);

        assertTrue(result.isEmpty());

        verify(session).find(
                Trainee.class,
                999L
        );
    }

    @Test
    void shouldReturnAllTrainees() {
        Trainee first = createTrainee(
                "John",
                "Smith",
                "John.Smith"
        );

        Trainee second = createTrainee(
                "Anna",
                "Brown",
                "Anna.Brown"
        );

        when(session.createQuery(
                "FROM Trainee",
                Trainee.class
        )).thenReturn(traineeQuery);

        when(traineeQuery.getResultList())
                .thenReturn(List.of(first, second));

        List<Trainee> result =
                traineeDao.findAll();

        assertEquals(2, result.size());
        assertTrue(result.contains(first));
        assertTrue(result.contains(second));

        verify(traineeQuery).getResultList();
    }

    @Test
    void shouldDeleteTrainee() {
        Trainee trainee = createTrainee(
                "John",
                "Smith",
                "John.Smith"
        );

        traineeDao.delete(trainee);

        verify(session).remove(trainee);
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
                traineeDao.existsByUsername(
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
                traineeDao.existsByUsername(
                        "Unknown.User"
                );

        assertFalse(result);
    }

    @Test
    void shouldFindTraineeByUsername() {
        Trainee trainee = createTrainee(
                "John",
                "Smith",
                "John.Smith"
        );

        when(session.createQuery(
                anyString(),
                eq(Trainee.class)
        )).thenReturn(traineeQuery);

        when(traineeQuery.setParameter(
                "username",
                "John.Smith"
        )).thenReturn(traineeQuery);

        when(traineeQuery.uniqueResultOptional())
                .thenReturn(Optional.of(trainee));

        Optional<Trainee> result =
                traineeDao.findByUsername(
                        "John.Smith"
                );

        assertTrue(result.isPresent());
        assertSame(trainee, result.get());

        verify(traineeQuery).setParameter(
                "username",
                "John.Smith"
        );
    }

    @Test
    void shouldReturnEmptyWhenUsernameDoesNotExist() {
        when(session.createQuery(
                anyString(),
                eq(Trainee.class)
        )).thenReturn(traineeQuery);

        when(traineeQuery.setParameter(
                "username",
                "Unknown.User"
        )).thenReturn(traineeQuery);

        when(traineeQuery.uniqueResultOptional())
                .thenReturn(Optional.empty());

        Optional<Trainee> result =
                traineeDao.findByUsername(
                        "Unknown.User"
                );

        assertTrue(result.isEmpty());
    }

    private Trainee createTrainee(
            String firstName,
            String lastName,
            String username
    ) {
        User user = new User(
                firstName,
                lastName,
                username,
                "abcdefghij",
                true
        );

        return new Trainee(
                user,
                LocalDate.of(1995, 5, 10),
                "Example address"
        );
    }
}