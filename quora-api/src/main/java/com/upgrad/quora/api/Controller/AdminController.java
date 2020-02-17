package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.UserDeleteResponse;
import com.upgrad.quora.service.business.AdminBusinessService;
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
 * AdminController is created for the delete user request.
 * Only admin can delete a user
 * */


@RestController
@RequestMapping("/")
public class AdminController {

    @Autowired
    private AdminBusinessService adminBusinessService;

    /*
     * The userDelete method is called when the user needs to delete a user from DB
     * this method deleted the user from databse when the user whose uuid is provided in request
     * is present in the DB
     * if the user is not available or the logged in user does not have access to this api,it throws AuthorizationFailedException
     * it passed authorization token to adminBusinesssService for user authorization
     * if the user is present and is deleted,it will send a response message and uuid of deleted user*/
    @RequestMapping(method = RequestMethod.DELETE, path = "/admin/user/{userId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<UserDeleteResponse> userDelete(@PathVariable(value = "userId") final String uuid, @RequestHeader("authorization") final String authorization) throws UserNotFoundException, AuthorizationFailedException {

        final UsersEntity deletedUser = adminBusinessService.deleteUser(uuid, authorization);
        UserDeleteResponse userDeleteResponse = new UserDeleteResponse().id(deletedUser.getUuid()).status("USER SUCCESSFULLY DELETED");
        return new ResponseEntity<UserDeleteResponse>(userDeleteResponse, HttpStatus.OK);

    }
}
