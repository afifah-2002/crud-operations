package respositories;

import Exceptions.ErrorDetails;
import models.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import java.sql.SQLException;



public class UserRepositoryImpl implements UserRepository {

    User user = new User();
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/TestDB";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "Afifah123@";


    @Override
    public User getLatestPost(String userRole, long id) throws ErrorDetails {
        if (userRole.equals("ADMIN")) {
            try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                String sql = "SELECT * FROM users WHERE id = ?";
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setLong(1, id);
                    try (ResultSet resultSet = statement.executeQuery()) {
                        if (resultSet.next()) {
                            long userId = resultSet.getLong("id");
                            String name = resultSet.getString("name");
                            String email = resultSet.getString("email");
                            String password = resultSet.getString("password");
                            String mobile_no = resultSet.getString("mobile_no");
                            String address = resultSet.getString("address");
                            String role = resultSet.getString("role");
                            // Similarly, retrieve other fields from the database

                           return new User(userId, name, email, password, mobile_no, address, role);
                        } else {
                            //throw new ErrorDetails("No user found with ID: " + id, "", 0);
                            return null;
                        }
                    }
                }
            } catch (SQLException e) {
                // Handle database errors
                throw new ErrorDetails("Error accessing database: " + e.getMessage(), "", 0);
            }
        } else {
            throw new ErrorDetails("Access denied: Only admins can access user details", "", 0);
        }
    }



    /*  public Set<User> getAllUsers(String userRole) throws ErrorDetails {
        if (userRole.equals("ADMIN")) {
            Set<User> users = new HashSet<>();
            try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                String sql = "SELECT * FROM users ORDER BY name ASC";
                try (PreparedStatement statement = connection.prepareStatement(sql);
                     ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        long id = resultSet.getLong("id");
                        String name = resultSet.getString("name");
                        String email = resultSet.getString("email");
                        String password = resultSet.getString("password");
                        String mobile_no = resultSet.getString("mobile_no");
                        String address = resultSet.getString("address");
                        String role = resultSet.getString("role");

                        users.add(new User(id, name, email, password, mobile_no, address, role));
                    }
                }
            } catch (SQLException e) {
                // Handle database errors
                throw new ErrorDetails("Error accessing database: " + e.getMessage(), "", 0);
            }
            if (users.isEmpty()) {
                throw new ErrorDetails("Users Do Not Exist", "", 0);
            }
            return users;
        } else {
            throw new ErrorDetails("Access denied: Only admins can access all users", "", 0);
        }
    }
*/
    public User getUserById(long id, String userRole) throws ErrorDetails {
        if (userRole.equals("ADMIN")) {
            Set<User> users = new HashSet<>();
            try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                String sql = "SELECT * FROM users WHERE id = ?";
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setLong(1, id);
                    try (ResultSet resultSet = statement.executeQuery()) {
                        if (resultSet.next()) {
                            String name = resultSet.getString("name");
                            String email = resultSet.getString("email");
                            String password = resultSet.getString("password");
                            String mobile_no = resultSet.getString("mobile_no");
                            String address = resultSet.getString("address");
                            String role = resultSet.getString("role");
                            // Similarly, retrieve other fields from the database

                           return new User(id, name, email, password, mobile_no, address, role);
                        } else {
                            throw new ErrorDetails("User does not exist with ID " + id, "", 0);
                        }
                    }
                }
            } catch (SQLException e) {
                // Handle database errors
                throw new ErrorDetails("Error accessing database: " + e.getMessage(), "", 0);
            }
        } else {
            throw new ErrorDetails("Access denied: Only admins can access user details", "", 0);
        }
    }




    @Override
    public List<User> getAllUsers(int pageNumber, int pageSize, String userRole) throws ErrorDetails {
        if (pageNumber <= 0 || pageSize <= 0) {
            throw new IllegalArgumentException("Page number and page size must be greater than zero");
        }

        if (!userRole.equals("ADMIN")) {
            throw new ErrorDetails("Access denied: Only admins can access all users", "", 0);
        }

        List<User> users = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT * FROM users ORDER BY id ASC LIMIT ? OFFSET ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, pageSize);
                statement.setInt(2, (pageNumber - 1) * pageSize); // Calculate the offset
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        // Retrieve user data and add to the list
                        long id = resultSet.getLong("id");
                        String name = resultSet.getString("name");
                        String email = resultSet.getString("email");
                        String password = resultSet.getString("password");
                        String mobileNo = resultSet.getString("mobile_no");
                        String address = resultSet.getString("address");
                        String role = resultSet.getString("role");

                       // users.add(new User(id, name, email, password, mobileNo, address, role));
                    }
                }
            }
        } catch (SQLException e) {
            throw new ErrorDetails("Error accessing database: " + e.getMessage(), "", 0);
        }

        return users;
    }


    public Set<User> getAllUsers(String userRole) throws ErrorDetails {
        if (userRole.equals("ADMIN")) {
            Set<User> users = new HashSet<>();
            try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                String sql = "SELECT * FROM users ORDER BY name ASC";
                try (PreparedStatement statement = connection.prepareStatement(sql);
                     ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        long id = resultSet.getLong("id");
                        String name = resultSet.getString("name");
                        String email = resultSet.getString("email");
                        String password = resultSet.getString("password");
                        String mobile_no = resultSet.getString("mobile_no");
                        String address = resultSet.getString("address");
                        String role = resultSet.getString("role");

                        System.out.println("ID: " + id + ", Name: " + name + ", Email: " + email + ", Password: " + password +
                                        ", Mobile No: " + mobile_no + ", Address: " + address + ", Role: "+role);

                        User user = new User();
                        user.setId(id);
                        user.setName(name);
                        user.setEmail(email);
                        user.setPassword(password);
                        user.setMobileNo(mobile_no);
                        user.setAddress(address);
                        user.setRole(role);

                        users.add(user);

                    }
                }
            } catch (SQLException e) {
                // Handle database errors
                throw new ErrorDetails("Error accessing database: " + e.getMessage(), "", 0);
            }
            // Check if users set is empty and return empty set if so
            if (users.isEmpty()) {
                return users;
            } else {
                return users;
            }
        } else {
            throw new ErrorDetails("Access denied: Only admins can access all users", "", 0);
        }
    }



    public String addUser(User user) throws ErrorDetails {
        // Validate user ID
//        if (user.getId() == 0) {
//            throw new ErrorDetails("User ID cannot be zero", "", 0);
//        }


        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            // Check if the user with the same ID already exists
            String checkSql = "SELECT COUNT(*) FROM users";
//            try (PreparedStatement checkStatement = connection.prepareStatement(checkSql)) {
//                checkStatement.setLong(1, user.getId());
//                try (ResultSet resultSet = checkStatement.executeQuery()) {
//                    resultSet.next();
//                    int count = resultSet.getInt(1);
//                    if (count > 0) {
//                        return "User ID already exists";
//                    }
//                }
//            }

            // Check if the user with the same email already exists
            String checkEmailSql = "SELECT COUNT(*) FROM users WHERE email = ?";
            try (PreparedStatement checkEmailStatement = connection.prepareStatement(checkEmailSql)) {
                checkEmailStatement.setString(1, user.getEmail());
                try (ResultSet emailResultSet = checkEmailStatement.executeQuery()) {
                    emailResultSet.next();
                    int emailCount = emailResultSet.getInt(1);
                    if (emailCount > 0) {
                        return "Email already exists";
                    }
                }
            }

            // Insert the new user into the database
            String sql = "INSERT INTO users (name, email, password, mobile_no, address, role) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                //statement.setLong(1, user.getId());
                statement.setString(2, user.getName());
                statement.setString(3, user.getEmail());
                statement.setString(4, user.getPassword());
                statement.setString(5, user.getMobileNo());
                statement.setString(6, user.getAddress());
                statement.setString(7, user.getRole());

                int rowsInserted = statement.executeUpdate();
                if (rowsInserted > 0) {
                    return "User added successfully";
                } else {
                    return "Failed to add user";
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return ("error accessing database");
        }
    }


    public String update(String id, String email, String name, String address, String mobileNo, String loggedInUserRole, String loggedInUserId) throws ErrorDetails {
        long userId = Long.parseLong(id);
        if (userId == 0) {
            throw new ErrorDetails("User ID cannot be zero", "", 0);
        }

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            // Check if the user exists
            String checkSql = "SELECT COUNT(*) FROM users WHERE id = ?";
            try (PreparedStatement checkStatement = connection.prepareStatement(checkSql)) {
                checkStatement.setLong(1, userId);
                try (ResultSet resultSet = checkStatement.executeQuery()) {
                    resultSet.next();
                    int count = resultSet.getInt(1);
                    if (count == 0) {
                        throw new ErrorDetails("User does not exist", "", 0);
                    }
                }
            }

            // Construct the SQL update statement
            StringBuilder sqlBuilder = new StringBuilder("UPDATE users SET ");
            List<Object> params = new ArrayList<>();
            if (email != null) {
                sqlBuilder.append("email = ?, ");
                params.add(email);
            }
            if (name != null) {
                sqlBuilder.append("name = ?, ");
                params.add(name);
            }
            if (address != null) {
                sqlBuilder.append("address = ?, ");
                params.add(address);
            }
            if (mobileNo != null) {
                sqlBuilder.append("mobile_no = ?, ");
                params.add(mobileNo);
            }
            // Remove the trailing comma and space
            sqlBuilder.delete(sqlBuilder.length() - 2, sqlBuilder.length());
            // Add the WHERE clause
            sqlBuilder.append(" WHERE id = ?");
            params.add(userId);

            // Execute the update statement
            try (PreparedStatement statement = connection.prepareStatement(sqlBuilder.toString())) {
                // Set the parameters
                for (int i = 0; i < params.size(); i++) {
                    statement.setObject(i + 1, params.get(i));
                }
                // Execute the update
                int rowsUpdated = statement.executeUpdate();
                if (rowsUpdated == 0) {
                    throw new ErrorDetails("User update failed", "", 0);
                }
            }

        } catch (SQLException e) {
            // Handle database errors
            throw new ErrorDetails("Error accessing database: " + e.getMessage(), "", 0);
        }

        return "Update was successful! please check Database"; // Return the updated user
    }

    public String deleteUser(long id, String userRole) throws ErrorDetails {
        if (userRole.equals("ADMIN")) {
            try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                // Check if the user exists
                String checkSql = "SELECT COUNT(*) FROM users WHERE id = ?";
                try (PreparedStatement checkStatement = connection.prepareStatement(checkSql)) {
                    checkStatement.setLong(1, id);
                    try (ResultSet resultSet = checkStatement.executeQuery()) {
                        resultSet.next();
                        int count = resultSet.getInt(1);
                        if (count == 0) {
                            throw new ErrorDetails("User does not exist with ID " + id, "", 0);
                        }
                    }
                }

                // Delete the user from the database
                String sql = "DELETE FROM users WHERE id = ?";
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setLong(1, id);
                    int rowsDeleted = statement.executeUpdate();
                    if (rowsDeleted > 0) {
                        return "User deleted successfully";
                    } else {
                        return "Failed to delete user";
                    }
                }
            } catch (SQLException e) {
                // Handle database errors
                throw new ErrorDetails("Error accessing database: " + e.getMessage(), "", 0);
            }
        } else {
            throw new ErrorDetails("Access denied: Only admins can delete user records", "", 0);
        }
    }

 /*   @Override
    public User findByEmail(String email) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        User user = null;

        try {
            // Establish a database connection
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            // Prepare and execute the SQL query
            String sql = "SELECT * FROM users WHERE email = ?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, email);
            resultSet = statement.executeQuery();

            // Process the query result
            if (resultSet.next()) {
                user = new User(
                        resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getString("password"),
                        resultSet.getString("mobile_no"),
                        resultSet.getString("address"),
                        resultSet.getString("role")
                );
            }
        } catch (SQLException e) {
            // Handle database errors
            e.printStackTrace();
        } finally {
            // Close resources in the finally block
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace(); // Log or handle the exception as needed
            }
        }

        return user;
    }*/

    @Override
    public long getMaxUserId() throws ErrorDetails {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT MAX(id) AS max_id FROM users";
            try (PreparedStatement statement = connection.prepareStatement(sql);
                 ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getLong("max_id");
                } else {
                    throw new ErrorDetails("No users found", "", 0);
                }
            }
        } catch (SQLException e) {
            // Handle database errors
            throw new ErrorDetails("Error accessing database: " + e.getMessage(), "", 0);
        }
    }

}
