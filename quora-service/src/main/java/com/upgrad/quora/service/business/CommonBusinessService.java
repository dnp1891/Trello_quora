package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserAuthDao;
import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UsersEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommonBusinessService {

    /*
     *
     * *createdBy aiwalia
     * This is a Service class that authorizes the user
     * */

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserAuthDao userAuthDao;


    /* if the user is not signed in it passed ATHR-001 code with Authorization Exception,
     * if the user has logged out, it passes ATHR-001 code with Authorization Exception
     * if the user being searched for is not found, it thows NOT FOUND exception
     * if the user is found, it returns user details
     * */
    @Transactional(propagation = Propagation.REQUIRED)
    public UsersEntity getUser(final String userUuid, final String authorizationToken) throws UserNotFoundException, AuthorizationFailedException {

        UserAuthEntity userAuthEntity = userAuthDao.getAuthToken(authorizationToken);

        if (userAuthEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }
        if (userAuthEntity.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to post a question");
        }
        UsersEntity usersEntity = userDao.getUser(userUuid);
        if (usersEntity == null) {
            throw new UserNotFoundException("USR-001", "User with entered uuid does not exist");
        }
        return usersEntity;
    }
}
