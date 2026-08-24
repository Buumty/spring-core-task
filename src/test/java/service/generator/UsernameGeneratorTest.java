package service.generator;

import org.example.dao.TraineeDao;
import org.example.dao.TrainerDao;
import org.example.service.generator.UsernameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsernameGeneratorTest {

    @Mock
    private TraineeDao traineeDao;

    @Mock
    private TrainerDao trainerDao;

    @InjectMocks
    private UsernameGenerator usernameGenerator;

    @Test
    void shouldGenerateUsernameFromFirstAndLastName() {
        when(traineeDao.existsByUsername("John.Smith"))
                .thenReturn(false);

        when(trainerDao.existsByUsername("John.Smith"))
                .thenReturn(false);

        String username = usernameGenerator.generate(
                "John",
                "Smith"
        );

        assertEquals("John.Smith", username);
    }

    @Test
    void shouldAddSuffixWhenUsernameExistsForTrainee() {
        when(traineeDao.existsByUsername("John.Smith"))
                .thenReturn(true);

        when(traineeDao.existsByUsername("John.Smith1"))
                .thenReturn(false);

        when(trainerDao.existsByUsername("John.Smith1"))
                .thenReturn(false);

        String username = usernameGenerator.generate(
                "John",
                "Smith"
        );

        assertEquals("John.Smith1", username);
    }

    @Test
    void shouldAddSuffixWhenUsernameExistsForTrainer() {
        when(traineeDao.existsByUsername("John.Smith"))
                .thenReturn(false);

        when(trainerDao.existsByUsername("John.Smith"))
                .thenReturn(true);

        when(traineeDao.existsByUsername("John.Smith1"))
                .thenReturn(false);

        when(trainerDao.existsByUsername("John.Smith1"))
                .thenReturn(false);

        String username = usernameGenerator.generate(
                "John",
                "Smith"
        );

        assertEquals("John.Smith1", username);
    }

    @Test
    void shouldIncrementSuffixUntilUsernameIsUnique() {
        when(traineeDao.existsByUsername("John.Smith"))
                .thenReturn(true);

        when(traineeDao.existsByUsername("John.Smith1"))
                .thenReturn(true);

        when(traineeDao.existsByUsername("John.Smith2"))
                .thenReturn(false);

        when(trainerDao.existsByUsername("John.Smith2"))
                .thenReturn(false);

        String username = usernameGenerator.generate(
                "John",
                "Smith"
        );

        assertEquals("John.Smith2", username);
    }
}