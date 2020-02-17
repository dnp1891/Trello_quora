package com.upgrad.quora.service.business;


import com.upgrad.quora.service.dao.QuestionDao;
import com.upgrad.quora.service.dao.UserAuthDao;
import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UsersEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

//This is service class for QuestionController.

@Service
public class QuestionBusinessService {

    @Autowired
    private QuestionDao questionDao;

    @Autowired
    private UserAuthDao userAuthDao;

    @Autowired
    private UserDao userDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public QuestionEntity createQuestion(QuestionEntity questionEntity,final String authorizationToken) throws AuthorizationFailedException {
        UserAuthEntity userAuthEntity = userAuthDao.getAuthToken(authorizationToken);

        if (userAuthEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }
        if (userAuthEntity.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to post a question");
        }
        questionEntity.setUser(userAuthEntity.getUser());
        questionDao.createQuestion(questionEntity);
        return questionEntity;

    }

    //This method takes the authorization to authorize the request.
    //The access token is sent to getAuthToken in userAuthDao to get userAuthEntity.
    //This entity is checked to authorise. If the parameter are right then questionentities list is created using questionDao.
    //All the related exception are handled.
    public List<QuestionEntity> getAllQuestions(final String authorizationToken)throws AuthorizationFailedException {
        UserAuthEntity userAuthEntity = userAuthDao.getAuthToken(authorizationToken);

        if(userAuthEntity == null){//Checking if user is not signed in.
            throw new AuthorizationFailedException("ATHR-001","User has not signed in");
        }else if(userAuthEntity.getLogoutAt() != null){//Checking if user is logged out.
            throw new AuthorizationFailedException("ATHR-002","User is signed out.Sign in first to get all questions");
        }

        //Returning the list of questionEntities.
        List<QuestionEntity> questionEntities = questionDao.getAllQuestions();
        return questionEntities;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public QuestionEntity editQuestion(QuestionEntity questionEntity,final String authorizationToken,final String questionId) throws AuthorizationFailedException, InvalidQuestionException {
        UserAuthEntity userAuthEntity = userAuthDao.getAuthToken(authorizationToken);


        if (userAuthEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }
        if (userAuthEntity.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to post a question");
        }
        QuestionEntity questionByUuid=questionDao.getQuestionByQuestionUuid(questionId);
        if (questionByUuid==null) {
            throw new InvalidQuestionException("QUES-001", "Entered question uuid does not exist");
        }
//            System.out.println(userAuthEntity.getUser().getUuid());
//        System.out.println(questionByUuid.getUser().getUuid());
        if (userAuthEntity.getUser().getUuid() != questionByUuid.getUser().getUuid()) {
            throw new AuthorizationFailedException("ATHR-003", "Only the question owner can edit the question");
        }
        questionEntity.setId(questionByUuid.getId());
        questionEntity.setUuid(questionByUuid.getUuid());
        questionEntity.setDate(questionByUuid.getDate());
        questionEntity.setUser(questionByUuid.getUser());
        questionDao.editQuestionContent(questionEntity);
        return questionEntity;

    }
    @Transactional(propagation = Propagation.REQUIRED)
    public QuestionEntity deleteQuestion(final String authorizationToken,final String questionId) throws InvalidQuestionException, AuthorizationFailedException {
        UserAuthEntity userAuthEntity = userAuthDao.getAuthToken(authorizationToken);

        if (userAuthEntity == null){//Chekcing if user is not signed in
            throw new AuthorizationFailedException("ATHR-001","User has not signed in");
        }
        if (userAuthEntity.getLogoutAt() != null){//checking if user is signed out
            throw new AuthorizationFailedException("ATHR-002","User is signed out.Sign in first to get all questions posted by a specific user");
        }
        QuestionEntity questionByUuid=questionDao.getQuestionByQuestionUuid(questionId);
        if (questionByUuid==null) {
            throw new InvalidQuestionException("QUES-001", "Entered question uuid does not exist");
        }
        String role = userAuthEntity.getUser().getRole();
        if(!role.contains("admin")) {
            if (userAuthEntity.getUser().getUuid() != questionByUuid.getUser().getUuid()) {
                throw new AuthorizationFailedException("ATHR-003", "Only the question owner can edit the question");
            }
        }
        questionByUuid.setId(questionByUuid.getId());
        questionByUuid.setUuid(questionByUuid.getUuid());
        questionByUuid.setDate(questionByUuid.getDate());
        questionByUuid.setUser(questionByUuid.getUser());
        questionByUuid.setContent(questionByUuid.getContent());
        questionDao.deleteQuestion(questionByUuid);
        return questionByUuid;
    }

    //This method takes the authorization to authorize the request and uuid of the user to get the user detail,
    // using this detail it fetches the question of the user.
    //The access token is sent to getAuthToken in userAuthDao to get userAuthEntity.
    //This entity is checked to authorise.
    // If the parameter are right then user details are fetched using uuid and using that questions are fetched from the database using QuestionDao.
    // Then questionentities list is created using questionDao.
    //All the related exception are handled.
    public List<QuestionEntity> getAllQuestionsByUser(final String uuid, final String authorizationToken) throws UserNotFoundException, AuthorizationFailedException {
        UserAuthEntity userAuthEntity = userAuthDao.getAuthToken(authorizationToken);

        if (userAuthEntity == null){//Chekcing if user is not signed in
            throw new AuthorizationFailedException("ATHR-001","User has not signed in");
        }else if (userAuthEntity.getLogoutAt() != null){//checking if user is signed out
            throw new AuthorizationFailedException("ATHR-002","User is signed out.Sign in first to get all questions posted by a specific user");
        }

        UsersEntity usersEntity = userDao.getUser(uuid);
        if (usersEntity == null){//Checking if user exists.
            throw new UserNotFoundException("USR-001","User with entered uuid whose question details are to be seen does not exist");
        }

        //Returning the list of questionEntities.
        List<QuestionEntity> questionEntities = questionDao.getAllQuestionsByUser(usersEntity);
        return questionEntities;
    }

}
