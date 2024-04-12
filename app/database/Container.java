package database;

import Exceptions.ErrorDetails;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import models.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import respositories.UserRepository;
import utils.TokenUtils;

@Singleton
public class Container implements UserRepository{

    private static final String DB_URL = "jdbc:postgresql://localhost:5432/TestDB";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "Afifah123@";


    private final UserRepository userRepository; // Inject UserRepository for database operations

    @Inject
    public Container(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //public Container(){}

    public Set<User> getAllUsers(String userRole) throws ErrorDetails {
        // Call the instance method from the UserRepository implementation
        return userRepository.getAllUsers(userRole);
    }


    @Override
    public User getLatestPost(String loggedInUserRole, long id) throws ErrorDetails {
        // Ensure that loggedInUserRole is not null or empty
        if (loggedInUserRole == null || loggedInUserRole.isEmpty()) {
            throw new ErrorDetails("User role not found in token", "", 0);
        }
        return userRepository.getLatestPost(loggedInUserRole, id);
    }
    @Override
    public List<User> getAllUsers(int pageNumber, int pageSize, String userRole) throws ErrorDetails {
        return userRepository.getAllUsers(pageNumber, pageSize, userRole);
    }


    public User getUserById(long id, String userRole) throws ErrorDetails {
        return userRepository.getUserById(id, userRole);
    }

    public String addUser(User user) throws ErrorDetails {
        return userRepository.addUser(user);
    }

   /* public String update(String id, String email, String name, String address, String mobileNo) throws ErrorDetails {
        userRepository.update(id, email, name, address, mobileNo);
        return "Update was successful! Please check the Database.";
    }*/

    public String update(String id, String email, String name, String address, String mobileNo, String loggedInUserRole, String loggedInUserId) throws ErrorDetails {
        long userId = Long.parseLong(id);
        //String loggedInUserIdString = String.valueOf(loggedInUserId);
       //long loggedInUserIdInt = Long.parseLong(loggedInUserId);
        
        User user = new User(0, null, null, null, mobileNo, null, null);

        // Check if the logged-in user is an ADMIN
        if (loggedInUserRole.equals("ADMIN")) {
            // ADMIN can edit any record, so proceed with the update
            String mobileValidationMsg = user.validateMobileNumber();
            if (mobileValidationMsg != null) {
                return (mobileValidationMsg);
            }
            userRepository.update(id, email, name, address, mobileNo, loggedInUserRole, loggedInUserId);
            return "Update was successful! Please check the Database.";
        } else if (loggedInUserRole.equals("CUSTOMER")) {
            // CUSTOMER can only edit their own record
            if (!id.equals(loggedInUserId)) {
                return("You are not authorized to update this user's record");
            } else {
                String mobileValidationMsg = user.validateMobileNumber();
                if (mobileValidationMsg != null) {
                    return (mobileValidationMsg);
                }
                userRepository.update(id, email, name, address, mobileNo, loggedInUserRole, loggedInUserId);
                return "Update was successful! Please check the Database.";
            }
        } else {
            return("Invalid user role");
        }
    }





    public String deleteUser(long id, String userRole) throws ErrorDetails {
        return userRepository.deleteUser(id, userRole);
    }

  /*  @Override
    public User findByEmail(String email) throws ErrorDetails {
        User user = null;
        try {
            // Implement logic to fetch user from the database based on the email
            user = userRepository.findByEmail(email);
        } catch (Exception e) {
            // Handle any potential errors or exceptions
            throw new ErrorDetails("Error fetching user by email: " + e.getMessage(), "", 0);
        }
        return user;
    }*/

    public static String login(String email, String password) throws ErrorDetails {
        TokenUtils tokenUtils = new TokenUtils();
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT * FROM users WHERE email = ? AND password = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, email);
                statement.setString(2, password);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        User user = new User(
                                resultSet.getLong("id"),
                                resultSet.getString("name"),
                                resultSet.getString("email"),
                                resultSet.getString("password"),
                                resultSet.getString("mobile_no"),
                                resultSet.getString("address"),
                                resultSet.getString("role")
                        );
                        String token = tokenUtils.generateJwtToken(user);
                        return token;
                    } else {
                        throw new ErrorDetails("Invalid email or password", "", 0);
                    }
                }
            }
        } catch (SQLException e) {
            throw new ErrorDetails("Error accessing database: " + e.getMessage(), "", 0);
        }
    }

    public static User authenticate(String email, String password) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT * FROM users WHERE email = ? AND password = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, email);
                statement.setString(2, password);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return new User(
                                resultSet.getLong("id"),
                                resultSet.getString("name"),
                                resultSet.getString("email"),
                                resultSet.getString("password"),
                                resultSet.getString("mobile_no"),
                                resultSet.getString("address"),
                                resultSet.getString("role")
                        );
                    }
                }
            }
        } catch (SQLException e) {
            // Log or handle the exception as needed
        }
        return null; // Return null if authentication fails
    }

    @Override
    public long getMaxUserId() throws ErrorDetails {
        return userRepository.getMaxUserId();
    }

}
