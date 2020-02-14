package com.upgrad.quora.api.controller;


import com.upgrad.quora.api.model.SigninResponse;
import com.upgrad.quora.api.model.SignoutResponse;
import com.upgrad.quora.api.model.SignupUserRequest;
import com.upgrad.quora.api.model.SignupUserResponse;
import com.upgrad.quora.service.business.UserBusinessService;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UsersEntity;
import com.upgrad.quora.service.exception.AuthenticationFailedException;
import com.upgrad.quora.service.exception.SignOutRestrictedException;
import com.upgrad.quora.service.exception.SignUpRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;
import java.util.UUID;

@RestController
@RequestMapping("/")
public class UserController {

    @Autowired
    UserBusinessService userBusinessService;

    /**
     * This method implement's the User Signup endpoint. This method gets the Object of type SignupUserRequest
     * and converts into the UsersEntity object. Then it goes to the service method for creating a new User.
     * Once it receives the response it create the SignupUserResponse object that is to be sent in
     * the Response Body.
     */
    @RequestMapping(path = "/user/signup", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SignupUserResponse> userSignUp(SignupUserRequest signupUserRequest) throws SignUpRestrictedException {
        UsersEntity usersEntity = new UsersEntity();
        usersEntity.setUuid(UUID.randomUUID().toString());
        usersEntity.setFirstName(signupUserRequest.getFirstName());
        usersEntity.setLastName(signupUserRequest.getLastName());
        usersEntity.setUserName(signupUserRequest.getUserName());
        usersEntity.setPassword(signupUserRequest.getPassword());
        usersEntity.setAboutMe(signupUserRequest.getAboutMe());
        usersEntity.setContactNumber(signupUserRequest.getContactNumber());
        usersEntity.setEmail(signupUserRequest.getEmailAddress());
        usersEntity.setCountry(signupUserRequest.getCountry());
        usersEntity.setDob(signupUserRequest.getDob());
        usersEntity.setRole("nonadmin");

        UsersEntity createdUsersEntity = userBusinessService.createUser(usersEntity);
        SignupUserResponse signupUserResponse = new SignupUserResponse().id(createdUsersEntity.getUuid()).status("USER SUCCESSFULLY REGISTERED");
        return new ResponseEntity<SignupUserResponse>(signupUserResponse, HttpStatus.CREATED);
    }

    /**
     * This method implement's the User Sign In Endpoint to Authenticate the User before using the application.
     * This method decodes the contents of the Authorization Header using Base 64 decode method and then calls the
     * service method which is supposed to Authenticate the user using the given Username and Password.
     */
    @RequestMapping(path = "/user/signin", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SigninResponse> userSignIn(@RequestHeader("authorization") final String authorization) throws AuthenticationFailedException {
        byte[] decoded = Base64.getDecoder().decode(authorization.split("Basic ")[1]);
        String decodedAuth = new String(decoded);
        String[] decodedArray = decodedAuth.split(":");
        UserAuthEntity userAuthEntity = userBusinessService.authenticateUser(decodedArray[0], decodedArray[1]);
        UsersEntity usersEntity = userAuthEntity.getUser();
        SigninResponse signinResponse = new SigninResponse().id(userAuthEntity.getUuid()).message("SIGNED IN SUCCESSFULLY");
        HttpHeaders headers = new HttpHeaders();
        headers.add("access_token", userAuthEntity.getAccessToken());
        return new ResponseEntity<SigninResponse>(signinResponse, headers, HttpStatus.OK);
    }

    /**
     * This method implement's User Sign out Endpoint. It calls the signOut service method which implements the sign out
     * and sends the SignoutResponse object with the Signedout user's UUID. After successful sign out it returns
     * "SIGNED OUT SUCCESSFULLY" message.
     */
    @RequestMapping(path = "/user/signout", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SignoutResponse> userSignOut(@RequestHeader("authorization") final String authorization) throws SignOutRestrictedException {
        UserAuthEntity userAuthEntity = userBusinessService.signOut(authorization);
        UsersEntity usersEntity = userAuthEntity.getUser();
        SignoutResponse signoutResponse = new SignoutResponse().id(usersEntity.getUuid()).message("SIGNED OUT SUCCESSFULLY");
        return new ResponseEntity<SignoutResponse>(signoutResponse, HttpStatus.OK);
    }
}