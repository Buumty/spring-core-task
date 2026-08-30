package org.example.dao.impl;

import org.example.dao.TrainingTypeDao;
import org.example.model.TrainingType;
import org.example.model.TrainingTypeName;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class TrainingTypeDaoImpl implements TrainingTypeDao {

    private final SessionFactory sessionFactory;

    public TrainingTypeDaoImpl(
            SessionFactory sessionFactory
    ) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Optional<TrainingType> findByName(
            TrainingTypeName name
    ) {
        return sessionFactory
                .getCurrentSession()
                .createQuery("""
                        FROM TrainingType tt
                        WHERE tt.trainingTypeName = :name
                        """, TrainingType.class)
                .setParameter("name", name)
                .uniqueResultOptional();
    }
}
