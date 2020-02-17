package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.AnswerEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class AnswerDao {

    @PersistenceContext
    private EntityManager entityManager;

    public AnswerEntity getAnswerByUuid(String answerUuid) {
        try {
            return entityManager.createNamedQuery("getAnswerByUuid", AnswerEntity.class).setParameter("answerUuid", answerUuid).getSingleResult();
        } catch (NoResultException nre){
            return null;
        }

    }

    public AnswerEntity createAnswer(AnswerEntity answerEntity) {
        entityManager.persist(answerEntity);
        return answerEntity;
    }

    public AnswerEntity editAnswerContent(AnswerEntity answerEntity) {
        return entityManager.merge(answerEntity);
    }

    public AnswerEntity deleteAnswer(AnswerEntity answerEntity) {
        entityManager.remove(answerEntity);
        return answerEntity;
    }
}
