package org.example.dao.impl;

import org.example.dao.TrainingDao;
import org.example.model.Training;
import org.example.model.TrainingTypeName;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class TrainingDaoImpl implements TrainingDao {
    private final SessionFactory sessionFactory;

    public TrainingDaoImpl(SessionFactory sessionFactory) {

        this.sessionFactory = sessionFactory;
    }

    @Override
    public Training save(Training training) {
        sessionFactory.getCurrentSession().persist(training);
        return training;
    }

    @Override
    public Optional<Training> findById(long id) {
        return Optional.ofNullable(sessionFactory.getCurrentSession().find(Training.class,id));
    }

    @Override
    public List<Training> findAll() {
        return sessionFactory.getCurrentSession()
                .createQuery("FROM Training", Training.class)
                .getResultList();
    }
    @Override
    public List<Training> findTraineeTrainings(
            String traineeUsername,
            LocalDate fromDate,
            LocalDate toDate,
            String trainerName,
            TrainingTypeName trainingType
    ) {

        StringBuilder hql = new StringBuilder("""
            FROM Training t
            WHERE t.trainee.user.username = :traineeUsername
            """);

        if (fromDate != null) {
            hql.append("""
                 AND t.trainingDate >= :fromDate
                """);
        }

        if (toDate != null) {
            hql.append("""
                 AND t.trainingDate <= :toDate
                """);
        }

        if (trainerName != null && !trainerName.isBlank()) {
            hql.append("""
                 AND (
                         LOWER(t.trainer.user.firstName) = LOWER(:trainerName)
                         OR LOWER(t.trainer.user.lastName) = LOWER(:trainerName)
                         OR LOWER(CONCAT(
                             CONCAT(t.trainer.user.firstName, ' '),
                             t.trainer.user.lastName
                         )) = LOWER(:trainerName)
                     )
                """);
        }

        if (trainingType != null) {
            hql.append("""
                 AND t.trainingType.trainingTypeName = :trainingType
                """);
        }

        Query<Training> query = sessionFactory
                .getCurrentSession()
                .createQuery(hql.toString(), Training.class);

        query.setParameter(
                "traineeUsername",
                traineeUsername
        );

        if (fromDate != null) {
            query.setParameter("fromDate", fromDate);
        }

        if (toDate != null) {
            query.setParameter("toDate", toDate);
        }

        if (trainerName != null && !trainerName.isBlank()) {
            query.setParameter("trainerName", trainerName);
        }

        if (trainingType != null) {
            query.setParameter("trainingType", trainingType);
        }

        return query.getResultList();
    }
    @Override
    public List<Training> findTrainerTrainings(
            String trainerUsername,
            LocalDate fromDate,
            LocalDate toDate,
            String traineeName
    ) {
        StringBuilder hql = new StringBuilder("""
            FROM Training t
            WHERE t.trainer.user.username = :trainerUsername
            """);

        if (fromDate != null) {
            hql.append("""
                 AND t.trainingDate >= :fromDate
                """);
        }

        if (toDate != null) {
            hql.append("""
                 AND t.trainingDate <= :toDate
                """);
        }

        if (traineeName != null && !traineeName.isBlank()) {
            hql.append("""
                 AND (
                    LOWER(t.trainee.user.firstName) = LOWER(:traineeName)
                    OR LOWER(t.trainee.user.lastName) = LOWER(:traineeName)
                    OR LOWER(CONCAT(
                        CONCAT(t.trainee.user.firstName, ' '),
                        t.trainee.user.lastName
                    )) = LOWER(:traineeName)
                 )
                """);
        }

        Query<Training> query = sessionFactory
                .getCurrentSession()
                .createQuery(
                        hql.toString(),
                        Training.class
                );

        query.setParameter(
                "trainerUsername",
                trainerUsername
        );

        if (fromDate != null) {
            query.setParameter("fromDate", fromDate);
        }

        if (toDate != null) {
            query.setParameter("toDate", toDate);
        }

        if (traineeName != null && !traineeName.isBlank()) {
            query.setParameter(
                    "traineeName",
                    traineeName
            );
        }

        return query.getResultList();
    }
}
