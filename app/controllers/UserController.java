package controllers;

import Exceptions.ErrorDetails;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import database.Container;
import models.User;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;


import java.util.*;

import static play.mvc.Http.*;
import static utils.TokenUtils.extractUserRoleFromToken;

import play.mvc.Http;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import respositories.UserRepository;
import utils.TokenUtils;

public class UserController extends Controller {

    private int lastUserId = 0; // Variable to store the last ID returned

    private final Container container;
    private final FormFactory formFactory;

    @Inject
    public UserController(Container cn,FormFactory fm){
        this.container = cn;
        this.formFactory = fm;
    }
    @Inject
    private UserRepository userRepository;

    //    public UserController() {
//        this.container = new Container(); // Instantiate the Container
//    }
    public Result createUser(Request request) throws ErrorDetails {
        Form<User> userForm = formFactory.form(User.class).bindFromRequest(request);
        if (userForm.hasErrors()) {
            return badRequest(userForm.errorsAsJson());
        }

        User user = userForm.get();  // if validation is successful it will retrieve the data in user
//        if (user.getId() == 0) {
//            return badRequest("User ID cannot be zero");
//        }

        String mesg = container.addUser(user); // add User to databasee
        return created(mesg); //201 created
    }

    public Result getUserById(long id, Http.Request request) {
        try {
            // Extract user role from JWT token in request headers
            String userRole = TokenUtils.extractUserRoleFromToken(request);

            // Validate user role and perform action accordingly
            if (userRole.equals("ADMIN")) {
                // Call Container.getUserById(id, userRole) if authorized
                User user = container.getUserById(id, userRole);
                JsonNode userJson = Json.toJson(user);
                return ok(userJson);
            } else {
                return unauthorized("Access denied: Only admins can access user details");
            }
        } catch (ErrorDetails e) {
            return notFound("User not found");
        } catch (Exception e) {
            return internalServerError(e.getMessage());
        }
    }



  public Result getLatestPost(Request request) {
      try {
          // Extract the user role from the request using TokenUtils or any other mechanism
          String loggedInUserRole = TokenUtils.extractUserRoleFromToken(request);

          // Ensure that loggedInUserRole is not null or empty
          if (loggedInUserRole == null || loggedInUserRole.isEmpty()) {
              return internalServerError("User role not found in token");
          }

          // Perform authorization checks based on the user role
          if (!loggedInUserRole.equals("ADMIN")) {
              return unauthorized("Access denied: Only admins can access the latest post");
          }

          // Increment the ID for the next request
          lastUserId++;

          // Get the maximum user ID
          long maxId = userRepository.getMaxUserId();

          // If maxId is less than lastUserId, reset lastUserId to 0 to start over
          if (maxId < lastUserId) {
              lastUserId = 0;
          }

          // Search for the next user starting from lastUserId until reaching maxId
          for (long userId = lastUserId; userId <= maxId; userId++) {
              User latestUser = userRepository.getLatestPost(loggedInUserRole, userId);
              if (latestUser != null) {
                  // Convert the user to JSON
                  ObjectMapper objectMapper = new ObjectMapper();
                  JsonNode userJson = objectMapper.valueToTree(latestUser);

                  // Return the JSON response
                  return ok(userJson);
              }
              else{

                  continue;
              }
          }

          // If no user found within the valid range, reset lastUserId to 0
          lastUserId = 0;
          throw new ErrorDetails("No user found within the valid range", "", 0);
      } catch (ErrorDetails error) {
          // Reset lastUserId to 0 on error
          lastUserId = 0;
          // Return an error response if there's an exception
          return internalServerError(error.getMessage());
      } catch (Exception e) {
          // Reset lastUserId to 0 on error
          lastUserId = 0;
          // Handle any other unexpected exceptions
          return internalServerError("An unexpected error occurred: " + e.getMessage());
      }
  }




    public Result deleteById(long id, Http.Request request) {
        try {
            // Extract user role from JWT token in request headers
            String userRole = TokenUtils.extractUserRoleFromToken(request);

            // Validate user role and perform action accordingly
            if (userRole.equals("ADMIN")) {
                // Call Container.deleteUser(id, userRole) if authorized
                String message = container.deleteUser(id, userRole);
                return ok(message);
            } else {
                return unauthorized("Access denied: Only admins can delete user records");
            }
        } catch (ErrorDetails e) {
            return notFound("User not found");
        } catch (Exception e) {
            return internalServerError(e.getMessage());
        }
    }



    public Result getPaginatedUsers(Integer pageNumber, Integer pageSize, Request request) {
        try {
            // Extract user role from the token
            String loggedInUserRole = TokenUtils.extractUserRoleFromToken(request);

            // Ensure that loggedInUserRole is not null or empty
            if (loggedInUserRole == null || loggedInUserRole.isEmpty()) {
                return unauthorized("User role not found in token");
            }

            // Fetch paginated users from the repository based on the extracted role
            List<User> users = userRepository.getAllUsers(pageNumber, pageSize, loggedInUserRole);

            // Convert the users to JSON
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode usersJson = objectMapper.valueToTree(users);

            // Return the JSON response
            return ok(usersJson);
        } catch (ErrorDetails | Exception error) {
            // Return an error response if there's an exception
            return internalServerError(error.getMessage());
        }
    }


    public Result findAll(Http.Request request) {
        try {
            // Extract user role from JWT token in request headers
            String userRole = TokenUtils.extractUserRoleFromToken(request);

            // Validate user role and perform action accordingly
            if (userRole.equals("ADMIN")) {
                // Call Container.getAllUsers(userRole) if authorized
                Set<User> users = container.getAllUsers(userRole);
                JsonNode usersJson = Json.toJson(users);
                return ok(usersJson);
            } else {
                return unauthorized("Access denied: Only admins can access all users");
            }
        } catch (ErrorDetails e) {
            return notFound("No users found");
        } catch (Exception e) {
            return internalServerError(e.getMessage());
        }
    }


    public Result loginUser(String email, String password, Http.Request request) throws ErrorDetails {
        // Authenticate user
        User authenticatedUser = Container.authenticate(email, password);

        if (authenticatedUser != null) {
            // Generate JWT token
            String token = TokenUtils.generateJwtToken(authenticatedUser);

            // Return the token as JSON response
            return ok(Json.toJson(token));
        } else {
            // Return unauthorized response if authentication fails
            return unauthorized("Invalid credentials");
        }
    }

    public Result update (String id, Http.Request request) throws ErrorDetails, Exception {
        JsonNode json = request.body().asJson();
        String token = TokenUtils.extractTokenFromRequest(request); // Extract token from request headers
        if (token == null) {
            return badRequest("Token not found in request headers");
        }

        String loggedInUserRole = TokenUtils.extractUserRoleFromToken(request); // Extract user role from token
        String loggedInUserEmail = TokenUtils.extractUserIdFromToken(token); // Extract user email from token


        long userId = Long.parseLong(id);
        if (userId == 0) {
            return badRequest("User ID cannot be zero");
        }

        String email = json.get("email").asText();
        String name = json.get("name").asText();
        String address = json.get("address").asText();
        String mobileNo = json.get("mobileNo").asText();


        try {
            System.out.println("Before update call");
            String msg = container.update(id, email, name, address, mobileNo, loggedInUserRole, loggedInUserEmail);
            return ok(msg);
        } catch (Exception e) {
            e.printStackTrace(); // Print stack trace for debugging
            return internalServerError("An error occurred while updating the user");
        }


    }
}



