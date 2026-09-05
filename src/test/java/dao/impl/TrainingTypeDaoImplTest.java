package dao.impl;

import org.example.dao.impl.TrainingTypeDaoImpl;
import org.example.model.TrainingType;
import org.example.model.TrainingTypeName;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainingTypeDaoImplTest {

    @Mock
    private SessionFactory sessionFactory;

    @Mock
    private Session session;

    @Mock
    private Query<TrainingType> trainingTypeQuery;

    @InjectMocks
    private TrainingTypeDaoImpl trainingTypeDao;

    @BeforeEach
    void setUp() {
        when(sessionFactory.getCurrentSession())
                .thenReturn(session);
    }

    @Test
    void shouldSaveTrainingType() {
        TrainingType trainingType =
                new TrainingType(
                        TrainingTypeName.STRENGTH
                );

        trainingTypeDao.save(trainingType);

        verify(session).persist(trainingType);
    }

    @Test
    void shouldFindTrainingTypeByName() {
        TrainingType trainingType =
                new TrainingType(
                        TrainingTypeName.STRENGTH
                );

        when(session.createQuery(
                anyString(),
                eq(TrainingType.class)
        )).thenReturn(trainingTypeQuery);

        when(trainingTypeQuery.setParameter(
                "name",
                TrainingTypeName.STRENGTH
        )).thenReturn(trainingTypeQuery);

        when(trainingTypeQuery.uniqueResultOptional())
                .thenReturn(
                        Optional.of(trainingType)
                );

        Optional<TrainingType> result =
                trainingTypeDao.findByName(
                        TrainingTypeName.STRENGTH
                );

        assertTrue(result.isPresent());
        assertSame(trainingType, result.get());

        verify(trainingTypeQuery)
                .setParameter(
                        "name",
                        TrainingTypeName.STRENGTH
                );

        verify(trainingTypeQuery)
                .uniqueResultOptional();
    }

    @Test
    void shouldReturnEmptyWhenTrainingTypeDoesNotExist() {
        when(session.createQuery(
                anyString(),
                eq(TrainingType.class)
        )).thenReturn(trainingTypeQuery);

        when(trainingTypeQuery.setParameter(
                "name",
                TrainingTypeName.YOGA
        )).thenReturn(trainingTypeQuery);

        when(trainingTypeQuery.uniqueResultOptional())
                .thenReturn(Optional.empty());

        Optional<TrainingType> result =
                trainingTypeDao.findByName(
                        TrainingTypeName.YOGA
                );

        assertTrue(result.isEmpty());

        verify(trainingTypeQuery)
                .setParameter(
                        "name",
                        TrainingTypeName.YOGA
                );
    }
}