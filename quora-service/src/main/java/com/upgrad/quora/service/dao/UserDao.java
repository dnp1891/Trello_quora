package com.upgrad.quora.service.dao;


import com.upgrad.quora.service.entity.UsersEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class UserDao {


    @PersistenceContext
    private EntityManager entityManager;



    public UsersEntity getUser(final String uuid) {
        try {
            UsersEntity user = entityManager.createNamedQuery("userByUuid", UsersEntity.class).setParameter("uuid", uuid).getSingleResult();
            return user;
        } catch (NoResultException nre) {
            return null;
        }
    }


    public UsersEntity getUserByUsername(final String username) {
        try {
            UsersEntity user = entityManager.createNamedQuery("userByUsername", UsersEntity.class).setParameter("username", username).getSingleResult();
            return user;
        } catch (NoResultException nre) {
            return null;
        }
    }


    public UsersEntity getUserByEmail(final String email) {
        try {
            UsersEntity user = entityManager.createNamedQuery("userByEmail", UsersEntity.class).setParameter("email", email).getSingleResult();
            return user;
        } catch (NoResultException nre) {
            return null;
        }
    }


    public UsersEntity createUser(UsersEntity usersEntity) {
        entityManager.persist(usersEntity);
        return usersEntity;
    }


}