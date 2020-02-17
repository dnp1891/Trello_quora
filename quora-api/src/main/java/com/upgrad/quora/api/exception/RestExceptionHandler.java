package com.upgrad.quora.api.exception;


import com.upgrad.quora.api.model.ErrorResponse;
import com.upgrad.quora.service.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;


// This class is created to handle the exception from the controller class.
// The error response is created as per the exception and are returned.

@ControllerAdvice
public class RestExceptionHandler {


    @ExceptionHandler(SignUpRestrictedException.class)
    public ResponseEntity<ErrorResponse> signUpRestrictedException(SignUpRestrictedException exc, WebRequest request) {
        return new ResponseEntity<ErrorResponse>(new ErrorResponse()
                .code(exc.getCode())
                .message(exc.getErrorMessage()),
                HttpStatus.CONFLICT);
    }

    @ExceptionHandler(AuthenticationFailedException.class)
    public ResponseEntity<ErrorResponse> authenticationFailedException(AuthenticationFailedException exc, WebRequest request) {
        return new ResponseEntity<ErrorResponse>(new ErrorResponse()
                .code(exc.getCode())
                .message(exc.getErrorMessage()),
                HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(SignOutRestrictedException.class)
    public ResponseEntity<ErrorResponse> signOutRestrictedException(SignOutRestrictedException exc, WebRequest request) {
        return new ResponseEntity<ErrorResponse>(new ErrorResponse()
                .code(exc.getCode())
                .message(exc.getErrorMessage()),
                HttpStatus.UNAUTHORIZED);
    }

    /*
     * *createdBy aiwalia
     * User Not Found ExceptionHandler for when user is not found for common getUser.
     *
     * */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> userNotFoundException(UserNotFoundException exc, WebRequest request) {
        return new ResponseEntity<ErrorResponse>(
                new ErrorResponse()
                        .code(exc.getCode())
                        .message(exc.getErrorMessage()),
                HttpStatus.NOT_FOUND);
    }
    /*
     * *createdBy aiwalia
     * Authorization ExceptionHandler for when user is not found for common getUser.
     *
     * */

    @ExceptionHandler(AuthorizationFailedException.class)
    public ResponseEntity<ErrorResponse> authorizationFailedException(AuthorizationFailedException exc, WebRequest request) {

        return new ResponseEntity<ErrorResponse>(new ErrorResponse()
                .code(exc.getCode())
                .message(exc.getErrorMessage()),
                HttpStatus.FORBIDDEN);
    }

}