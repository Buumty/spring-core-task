package org.example.dao.impl;

import org.example.dao.TrainingDao;
import org.example.model.Training;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

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
}
