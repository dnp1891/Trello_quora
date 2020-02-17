package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.UserDetailsResponse;
import com.upgrad.quora.service.business.CommonBusinessService;
import com.upgrad.quora.service.entity.UsersEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/*
 * *createdBy aiwalia
 *
 * CommonController is the controller class for GET Request to get user details

 * */

@RestController
@RequestMapping("/")
public class CommonController {

    @Autowired
    private CommonBusinessService commonBusinessService;

    /*
     * Here, we check for Session of the logged in user,
     * if the user is not logged in,AuthorizationException is thrown
     * if the user does not exist in the database, NOT FOUND status is returned
     * if the user existx OK status is returned
     * */
    @RequestMapping(method = RequestMethod.GET, path = "/userprofile/{userId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<UserDetailsResponse> getUserDetails(@PathVariable("userId") final String uuid, @RequestHeader("authorization") final String authorization) throws UserNotFoundException, AuthorizationFailedException {
        final UsersEntity usersEntity = commonBusinessService.getUser(uuid, authorization);
        UserDetailsResponse userDetailsResponse = new UserDetailsResponse()
                .userName(usersEntity.getUserName())
                .firstName(usersEntity.getFirstName())
                .lastName(usersEntity.getLastName())
                .aboutMe(usersEntity.getAboutMe())
                .contactNumber(usersEntity.getContactNumber())
                .country(usersEntity.getCountry())
                .dob(usersEntity.getDob())
                .emailAddress(usersEntity.getEmail());

        return new ResponseEntity<UserDetailsResponse>(userDetailsResponse, HttpStatus.OK);
    }
}
