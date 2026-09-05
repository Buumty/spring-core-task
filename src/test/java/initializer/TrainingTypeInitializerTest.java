package initializer;

import org.example.dao.TrainingTypeDao;
import org.example.initializer.TrainingTypeInitializer;
import org.example.model.TrainingType;
import org.example.model.TrainingTypeName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainingTypeInitializerTest {

    @Mock
    private TrainingTypeDao trainingTypeDao;

    @InjectMocks
    private TrainingTypeInitializer initializer;

    @Test
    void shouldInitializeAllTrainingTypesWhenDatabaseIsEmpty() {
        for (TrainingTypeName name : TrainingTypeName.values()) {
            when(trainingTypeDao.findByName(name))
                    .thenReturn(Optional.empty());
        }

        initializer.initializeTrainingTypes();

        ArgumentCaptor<TrainingType> captor =
                ArgumentCaptor.forClass(TrainingType.class);

        verify(
                trainingTypeDao,
                times(TrainingTypeName.values().length)
        ).save(captor.capture());

        List<TrainingTypeName> savedTypes =
                captor.getAllValues()
                        .stream()
                        .map(TrainingType::getTrainingTypeName)
                        .toList();

        assertEquals(
                TrainingTypeName.values().length,
                savedTypes.size()
        );

        for (TrainingTypeName name : TrainingTypeName.values()) {
            assertTrue(savedTypes.contains(name));

            verify(trainingTypeDao)
                    .findByName(name);
        }
    }

    @Test
    void shouldNotSaveTrainingTypesWhenTheyAlreadyExist() {
        for (TrainingTypeName name : TrainingTypeName.values()) {
            when(trainingTypeDao.findByName(name))
                    .thenReturn(
                            Optional.of(
                                    new TrainingType(name)
                            )
                    );
        }

        initializer.initializeTrainingTypes();

        verify(trainingTypeDao, never())
                .save(any(TrainingType.class));
    }

    @Test
    void shouldSaveOnlyMissingTrainingTypes() {
        TrainingType fitness =
                new TrainingType(
                        TrainingTypeName.FITNESS
                );

        TrainingType strength =
                new TrainingType(
                        TrainingTypeName.STRENGTH
                );

        when(trainingTypeDao.findByName(
                TrainingTypeName.FITNESS
        )).thenReturn(Optional.of(fitness));

        when(trainingTypeDao.findByName(
                TrainingTypeName.STRENGTH
        )).thenReturn(Optional.of(strength));

        when(trainingTypeDao.findByName(
                TrainingTypeName.CARDIO
        )).thenReturn(Optional.empty());

        when(trainingTypeDao.findByName(
                TrainingTypeName.YOGA
        )).thenReturn(Optional.empty());

        initializer.initializeTrainingTypes();

        ArgumentCaptor<TrainingType> captor =
                ArgumentCaptor.forClass(
                        TrainingType.class
                );

        verify(trainingTypeDao, times(2))
                .save(captor.capture());

        List<TrainingTypeName> savedTypes =
                captor.getAllValues()
                        .stream()
                        .map(TrainingType::getTrainingTypeName)
                        .toList();

        assertEquals(2, savedTypes.size());
        assertTrue(
                savedTypes.contains(
                        TrainingTypeName.CARDIO
                )
        );
        assertTrue(
                savedTypes.contains(
                        TrainingTypeName.YOGA
                )
        );

        assertFalse(
                savedTypes.contains(
                        TrainingTypeName.FITNESS
                )
        );
        assertFalse(
                savedTypes.contains(
                        TrainingTypeName.STRENGTH
                )
        );
    }
}