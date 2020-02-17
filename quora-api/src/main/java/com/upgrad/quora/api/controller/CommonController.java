package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.UserDetailsResponse;
import com.upgrad.quora.service.business.CommonBusinessService;
import com.upgrad.quora.service.entity.UsersEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class CommonController {

    @Autowired
    private CommonBusinessService commonBusinessService;

    @RequestMapping(method = RequestMethod.GET, path = "/userprofile/{userId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<UserDetailsResponse> getUserDetails(@PathVariable("id") final String uuid)
    {
        final UsersEntity usersEntity = commonBusinessService.getUser(uuid);
         UserDetailsResponse userDetailsResponse = new UserDetailsResponse()
                 .userName(usersEntity.getUserName())
                 .firstName(usersEntity.getFirstName())
                 .lastName(usersEntity.getLastName())
                 .aboutMe(usersEntity.getAboutMe())
                 .contactNumber(usersEntity.getContactNumber())
                 .country(usersEntity.getCountry())
                 .dob(usersEntity.getDob())
                 .emailAddress(usersEntity.getEmail());

        return new ResponseEntity<UserDetailsResponse>(userDetailsResponse,HttpStatus.OK);
    }
}
