package org.example.dao.impl;

import jakarta.persistence.FindOption;
import org.example.dao.TraineeDao;
import org.example.model.Trainee;
import org.example.model.Training;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class TraineeDaoImpl implements TraineeDao {

    private final SessionFactory sessionFactory;

    public TraineeDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Trainee save(Trainee trainee) {
        sessionFactory.getCurrentSession().persist(trainee);
        return trainee;
    }

    @Override
    public Trainee update(Trainee trainee) {
        sessionFactory.getCurrentSession().merge(trainee);
        return trainee;
    }

    @Override
    public void deleteById(long id) {
        Trainee trainee = sessionFactory.getCurrentSession().getReference(Trainee.class, id);

        sessionFactory.getCurrentSession().remove(trainee);
    }

    @Override
    public Optional<Trainee> findById(long id) {
        return Optional.ofNullable(
                sessionFactory.getCurrentSession()
                        .find(Trainee.class, id)
        );
    }

    @Override
    public List<Trainee> findAll() {
        return sessionFactory.getCurrentSession()
                .createQuery(
                        "FROM Trainee",
                        Trainee.class
                ).getResultList();
    }

    @Override
    public boolean existsByUsername(String username) {
        Long count = sessionFactory
                .getCurrentSession()
                .createQuery("""
                        SELECT COUNT(t)
                        FROM Trainee t
                        WHERE t.user.username = :username
                        """, Long.class)
                .setParameter("username", username)
                .getSingleResult();

        return count > 0;
    }
    @Override
    public Optional<Trainee> findByUsername(String username) {
        return sessionFactory
                .getCurrentSession()
                .createQuery("""
                        FROM Trainee t
                        WHERE t.user.username = :username
                        """, Trainee.class)
                .setParameter("username", username)
                .uniqueResultOptional();
    }

    @Override
    public void deleteByUsername(String username) {
        sessionFactory
                .getCurrentSession()
                .createMutationQuery("""
                    DELETE FROM Trainee t
                    WHERE t.user.username = :username
                    """)
                .setParameter("username", username)
                .executeUpdate();
    }
}
