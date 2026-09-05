package org.example.dao.impl;

import org.example.dao.TrainerDao;
import org.example.model.Trainee;
import org.example.model.Trainer;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Repository
public class TrainerDaoImpl implements TrainerDao {

    private final SessionFactory sessionFactory;

    public TrainerDaoImpl(SessionFactory sessionFactory) {

        this.sessionFactory = sessionFactory;
    }

    @Override
    public Trainer save(Trainer trainer) {
        sessionFactory.getCurrentSession().persist(trainer);
        return trainer;
    }

    @Override
    public Trainer update(Trainer trainer) {
        sessionFactory.getCurrentSession().merge(trainer);
        return trainer;
    }

    @Override
    public Optional<Trainer> findById(long id) {
        return Optional.ofNullable(sessionFactory.getCurrentSession().find(Trainer.class, id));
    }

    @Override
    public List<Trainer> findAll() {
        return sessionFactory.getCurrentSession()
                .createQuery("From Trainer", Trainer.class)
                .getResultList();
    }
    @Override
    public boolean existsByUsername(String username) {
        Long count = sessionFactory
                .getCurrentSession()
                .createQuery("""
                        SELECT COUNT(t)
                        FROM Trainer t
                        WHERE t.user.username = :username
                        """, Long.class)
                .setParameter("username", username)
                .getSingleResult();

        return count > 0;
    }

    @Override
    public Optional<Trainer> findByUsername(String username) {
        return sessionFactory
                .getCurrentSession()
                .createQuery("""
                        FROM Trainer t
                        WHERE t.user.username = :username
                        """, Trainer.class)
                .setParameter("username", username)
                .uniqueResultOptional();
    }
    @Override
    public List<Trainer> findNotAssignedToTrainee(
            String traineeUsername
    ) {
        return sessionFactory
                .getCurrentSession()
                .createQuery("""
                    SELECT tr
                    FROM Trainer tr
                    WHERE tr NOT IN (
                        SELECT assignedTrainer
                        FROM Trainee t
                        JOIN t.trainers assignedTrainer
                        WHERE t.user.username = :traineeUsername
                    )
                    """, Trainer.class)
                .setParameter(
                        "traineeUsername",
                        traineeUsername
                )
                .getResultList();
    }
    @Override
    public List<Trainer> findByUsernames(Set<String> usernames) {
        return sessionFactory
                .getCurrentSession()
                .createQuery("""
                    FROM Trainer t
                    WHERE t.user.username IN :usernames
                    """, Trainer.class)
                .setParameterList("usernames", usernames)
                .getResultList();
    }
}
