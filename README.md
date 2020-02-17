Trello_quora
--------

Instructions:

1. JSON files have been given in the stub file. You need to run these JSON files on Swagger UI and observe the details related to each endpoint. Observe the HTTP methods to make a call to that endpoint, request URL pattern, input and output parameters of the endpoint, and the status codes in each endpoint to handle the exceptions related to that endpoint.

2. All the exceptions have been implemented. You need to throw the same exception as described below in the explanation of each endpoint along with the same error code and same message. Since the test cases designed depends on the error code, so the test case would not pass if you use different error code.

3. Build the project in the main directory of the project using "mvn clean install -DskipTests". In order to activate the profile setup, move to quora-db folder using "cd quora-db" command in the terminal and then run "mvn clean install -Psetup" command to activate the profile setup.

4. Since the database is not mocked, "quora_test.sql" file is given in the stub to create the records in the database to pass all the test cases. All the test cases would only pass if you have these records in the database. Therefore, before running each test case you need to ensure that the database contains all the records given in "quora_test.sql" file. As otherwise, the test case may not pass even if the code implementation is correct.

5. In the Quora project, we will always refer to the resource's uuid whenever id of the resource is mentioned.

UserController
--------
The following API endpoints must be implemented in 'UserController' class:

1. signup - "/user/signup"

This endpoint is used to register a new user in the Quora Application.

- It should be a POST request
- This endpoint requests for all the attributes in 'SignupUserRequest' about the user.
- If the username provided already exists in the current database, throw ‘SignUpRestrictedException’ with the message code -'SGR-001' and message - 'Try any other Username, this Username has already been taken'.
- If the email Id provided by the user already exists in the current database, throw ‘SignUpRestrictedException’ with the message code -'SGR-002' and message -'This user has already been registered, try with any other emailId'.
- If the information is provided by a non-existing user, then save the user information in the database and return the 'uuid' of the registered user and message 'USER SUCCESSFULLY REGISTERED' in the JSON response with the corresponding HTTP status. Also, make sure to save the password after encrypting it using 'PasswordCryptographyProvider' class given in the stub file.
- Also, when a user signs up using this endpoint then the role of the person will be 'nonadmin' by default. You can add users with 'admin' role only by executing database queries or with pgAdmin.
 

2. signin - "/user/signin"

This endpoint is used for user authentication. The user authenticates in the application and after successful authentication, JWT token is given to a user.

- It should be a POST request
- This endpoint requests for the User credentials to be passed in the authorization field of header as part of Basic authentication. You need to pass "Basic username:password" (where username:password of the String is encoded to Base64 format) in the authorization header.
- If the username provided by the user does not exist, throw "AuthenticationFailedException" with the message code -'ATH-001' and message-'This username does not exist'.
- If the password provided by the user does not match the password in the existing database, throw 'AuthenticationFailedException' with the message code -'ATH-002' and message -'Password failed'.
- If the credentials provided by the user match the details in the database, save the user login information in the database and return the 'uuid' of the authenticated user from 'users' table and message 'SIGNED IN SUCCESSFULLY' in the JSON response with the corresponding HTTP status. Note that 'JwtAccessToken' class has been given in the stub file to generate an access token.
- Also, return the access token in the access_token field of the Response Header, which will be used by the user for any further operation in the Quora Application.
 

3. signout - "/user/signout"

This endpoint is used to sign out from the Quora Application. The user cannot access any other endpoint once he is signed out of the application.

- It should be a POST request.
- This endpoint must request the access token of the signed in user in the authorization field of the Request Header.
- If the access token provided by the user does not exist in the database, throw 'SignOutRestrictedException' with the message code -'SGR-001' and message - 'User is not Signed in'.
- If the access token provided by the user is valid, update the LogoutAt time of the user in the database and return the 'uuid' of the signed out user from 'users' table and message 'SIGNED OUT SUCCESSFULLY' in the JSON response with the corresponding HTTP status.

CommonController
--------
The following API endpoints must be implemented in 'CommonController' class:

1. userProfile - "/userprofile/{userId}"

This endpoint is used to get the details of any user in the Quora Application. This endpoint can be accessed by any user in the application.

- It should be a GET request
- This endpoint must request the path variable 'userId' as a string for the corresponding user profile to be retrieved and access token of the signed in user as a string in authorization Request Header.
- If the access token provided by the user does not exist in the database throw 'AuthorizationFailedException' with the message code - 'ATHR-001' and message - 'User has not signed in'.
- If the user has signed out, throw "AuthorizationFailedException" with the message code -'ATHR-002' and message -'User is signed out.Sign in first to get user details' .
- If the user with uuid whose profile is to be retrieved does not exist in the database, throw 'UserNotFoundException' with the message code -'USR-001' and message -'User with entered uuid does not exist'.
- Else, return all the details of the user from the database in the JSON response with the corresponding HTTP status.

AdminController
--------
The following API endpoints must be implemented in 'AdminController' class:

1. userDelete - "/admin/user/{userId}"

This endpoint is used to delete a user from the Quora Application. Only an admin is authorized to access this endpoint.

- It should be a DELETE request.
- This endpoint requests the path variable 'userId' as a string for the corresponding user which is to be deleted from the database and access token of the signed in user as a string in authorization Request Header.
- If the access token provided by the user does not exist in the database throw 'AuthorizationFailedException' with the message code-'ATHR-001' and message -'User has not signed in'.
- If the user has signed out, throw 'AuthorizationFailedException' with the message code- 'ATHR-002' and message -'User is signed out'.
- If the role of the user is 'nonadmin',  throw 'AuthorizationFailedException' with the message code-'ATHR-003' and message -'Unauthorized Access, Entered user is not an admin'.
- If the user with uuid whose profile is to be deleted does not exist in the database, throw 'UserNotFoundException' with the message code -'USR-001' and message -'User with entered uuid to be deleted does not exist'.
- Else, delete the records from all the tables related to that user and return 'uuid' of the deleted user from 'users' table and message 'USER SUCCESSFULLY DELETED' in the JSON response with the corresponding HTTP status.


REST API endpoints - 2
--------

QuestionController
--------
The following API endpoints must be implemented in 'QuestionController' class:

1. createQuestion - "/question/create"

This endpoint is used to create a question in the Quora Application which will be shown to all the users. Any user can access this endpoint.

- It should be a POST request.
- This endpoint requests for all the attributes in 'QuestionRequest' about the question and access token of the signed in user as a string in the authorization field of the Request Header.
- If the access token provided by the user does not exist in the database throw "AuthorizationFailedException" with the message code - 'ATHR-001' and message - 'User has not signed in'.
- If the user has signed out, throw 'AuthorizationFailedException' with the message code- 'ATHR-002' and message -'User is signed out.Sign in first to post a question'.
- Else, save the question information in the database and return the 'uuid' of the question and message 'QUESTION CREATED' in the JSON response with the corresponding HTTP status.
 

2. getAllQuestions - "/question/all"

This endpoint is used to fetch all the questions that have been posted in the application by any user. Any user can access this endpoint.

- It should be a GET request.
- This endpoint requests for access token of the signed in user as a string in authorization Request Header.
- If the access token provided by the user does not exist in the database throw 'AuthorizationFailedException' with the message code - 'ATHR-001' and message - 'User has not signed in'.
- If the user has signed out, throw 'AuthorizationFailedException' with the message code-'ATHR-002' and message-'User is signed out.Sign in first to get all questions'.
- Else, return 'uuid' and 'content' of all the questions from the database in the JSON response with the corresponding HTTP status.
 

3. editQuestionContent - "/question/edit/{questionId}"

This endpoint is used to edit a question that has been posted by a user. Note, only the owner of the question can edit the question.  

- It should be a PUT request.
- This endpoint requests for all the attributes in 'QuestionEditRequest', the path variable 'questionId' as a string for the corresponding question which is to be edited in the database and access token of the signed in user as a string in the authorization field of the Request Header.
- If the access token provided by the user does not exist in the database throw 'AuthorizationFailedException' with the message code-'ATHR-001' and message-'User has not signed in'.
- If the user has signed out, throw 'AuthorizationFailedException' with the message code-'ATHR-002' and message-'User is signed out.Sign in first to edit the question'.
- Only the question owner can edit the question. Therefore, if the user who is not the owner of the question tries to edit the question throw "AuthorizationFailedException" with the message code-'ATHR-003' and message-'Only the question owner can edit the question'.
- If the question with uuid which is to be edited does not exist in the database, throw 'InvalidQuestionException' with the message code - 'QUES-001' and message -'Entered question uuid does not exist'.
- Else, edit the question in the database and return 'uuid' of the edited question and message 'QUESTION EDITED' in the JSON response with the corresponding HTTP status.
 

4. deleteQuestion - "/question/delete/{questionId}"

This endpoint is used to delete a question that has been posted by a user. Note, only the question owner of the question or admin can delete a question.

- It should be a DELETE request.
- This endpoint requests for the path variable 'questionId' as a string for the corresponding question which is to be deleted from the database and access token of the signed in user as a string in the authorization field of the Request Header.
- If the access token provided by the user does not exist in the database throw 'AuthorizationFailedException' with the message code - 'ATHR-001' and message - 'User has not signed in'.
- If the user has signed out, throw 'AuthorizationFailedException' with the message code- 'ATHR-002' and message -'User is signed out.Sign in first to delete a question'.
- Only the question owner or admin can delete the question. Therefore, if the user who is not the owner of the question or the role of the user is ‘nonadmin’ and tries to delete the question, throw 'AuthorizationFailedException' with the message code-'ATHR-003' and message -'Only the question owner or admin can delete the question'.
- If the question with uuid which is to be deleted does not exist in the database, throw 'InvalidQuestionException' with the message code-'QUES-001' and message-'Entered question uuid does not exist'.
- Else, delete the question from the database and return 'uuid' of the deleted question and message -'QUESTION DELETED' in the JSON response with the corresponding HTTP status.
 

5. getAllQuestionsByUser - "question/all/{userId}"

This endpoint is used to fetch all the questions posed by a specific user. Any user can access this endpoint.

- It should be a GET request.
- This endpoint requests the path variable 'userId' as a string for the corresponding user whose questions are to be retrieved from the database and access token of the signed in user as a string in authorization Request Header.
- If the access token provided by the user does not exist in the database throw 'AuthorizationFailedException' with the message code-'ATHR-001' and message -'User has not signed in'.
- If the user has signed out, throw 'AuthorizationFailedException' with the message code-'ATHR-002' and message-'User is signed out.Sign in first to get all questions posted by a specific user'.
- If the user with uuid whose questions are to be retrieved from the database does not exist in the database, throw 'UserNotFoundException' with the message code -'USR-001' and message -'User with entered uuid whose question details are to be seen does not exist'.
- Else, return 'uuid' and 'content' of all the questions posed by the corresponding user from the database in the JSON response with the corresponding HTTP status.

REST API endpoints - 3
--------

AnswerController
--------
The following API endpoints must be implemented in 'AnswerController' class:

1. createAnswer - "/question/{questionId}/answer/create"

This endpoint is used to create an answer to a particular question. Any user can access this endpoint.

- It should be a POST request.
- This endpoint requests for the attribute in "Answer Request", the path variable 'questionId ' as a string for the corresponding question which is to be answered in the database and access token of the signed in user as a string in authorization Request Header.
- If the question uuid entered by the user whose answer is to be posted does not exist in the database, throw "InvalidQuestionException" with the message code - 'QUES-001' and message - 'The question entered is invalid'.
- If the access token provided by the user does not exist in the database throw "AuthorizationFailedException" with the message code - 'ATHR-001' and message - 'User has not signed in'.
- If the user has signed out, throw "AuthorizationFailedException" with the message code - 'ATHR-002' and message - 'User is signed out.Sign in first to post an answer'.
- Else, save the answer information in the database and return the "uuid" of the answer and message "ANSWER CREATED" in the JSON response with the corresponding HTTP status.
 

2. editAnswerContent - "/answer/edit/{answerId}"

This endpoint is used to edit an answer. Only the owner of the answer can edit the answer.  

- It should be a PUT request.
- This endpoint requests for all the attributes in "AnswerEditRequest", the path variable 'answerId' as a string for the corresponding answer which is to be edited in the database and access token of the signed in user as a string in authorization Request Header.
- If the access token provided by the user does not exist in the database throw "AuthorizationFailedException" with the message code - 'ATHR-001' and message - 'User has not signed in'.
- If the user has signed out, throw "AuthorizationFailedException" with the message code - 'ATHR-002' and message 'User is signed out.Sign in first to edit an answer'.
- Only the answer owner can edit the answer. Therefore, if the user who is not the owner of the answer tries to edit the answer throw "AuthorizationFailedException" with the message code - 'ATHR-003' and message - 'Only the answer owner can edit the answer'.
- If the answer with uuid which is to be edited does not exist in the database, throw "AnswerNotFoundException" with the message code - 'ANS-001' and message - 'Entered answer uuid does not exist'.
- Else, edit the answer in the database and return "uuid" of the edited answer and message "ANSWER EDITED" in the JSON response with the corresponding HTTP status.
 

3. deleteAnswer - "/answer/delete/{answerId}"

This endpoint is used to delete an answer. Only the owner of the answer or admin can delete an answer.

- It should be a DELETE request.
- This endpoint requests for the path variable 'answerId' as a string for the corresponding answer which is to be deleted from the database and access token of the signed in user as a string in authorization Request Header.
- If the access token provided by the user does not exist in the database throw "AuthorizationFailedException" with the message code - 'ATHR-001' and message - 'User has not signed in'.
- If the user has signed out, throw "AuthorizationFailedException" with the message code - 'ATHR-002' and message - 'User is signed out.Sign in first to delete an answer'.
- Only the answer owner or admin can delete the answer. Therefore, if the user who is not the owner of the answer or the role of the user is ‘nonadmin’ and tries to delete the answer throw "AuthorizationFailedException" with the message code - 'ATHR-003' and message - 'Only the answer owner or admin can delete the answer'.
- If the answer with uuid which is to be deleted does not exist in the database, throw "AnswerNotFoundException" with the message code - 'ANS-001' and message - 'Entered answer uuid does not exist'.
- Else, delete the answer from the database and return "uuid" of the deleted answer and message "ANSWER DELETED" in the JSON response with the corresponding HTTP status.
 

4. getAllAnswersToQuestion - "answer/all/{questionId}"

This endpoint is used to get all answers to a particular question. Any user can access this endpoint.

- It should be a GET request.
- This endpoint requests the path variable 'questionId' as a string for the corresponding question whose answers are to be retrieved from the database and access token of the signed in user as a string in authorization Request Header.
- If the access token provided by the user does not exist in the database throw "AuthorizationFailedException" with the message code - 'ATHR-001' and message - 'User has not signed in'.
- If the user has signed out, throw "AuthorizationFailedException" with the message code - 'ATHR-002' and message - 'User is signed out.Sign in first to get the answers'.
- If the question with uuid whose answers are to be retrieved from the database does not exist in the database, throw "InvalidQuestionException" with the message code - 'QUES-001' and message - 'The question with entered uuid whose details are to be seen does not exist'.
- Else, return "uuid" of the answer, "content" of the question and "content" of all the answers posted for that particular question from the database in the JSON response with the corresponding HTTP status.