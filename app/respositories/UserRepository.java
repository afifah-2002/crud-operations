package respositories;

import Exceptions.ErrorDetails;
import models.User;

import java.util.List;
import java.util.Set;

public interface UserRepository {
//    Set<User> getPaginatedUsers(Integer pageNumber, Integer pageSize, String userRole) throws ErrorDetails;

    User getLatestPost(String userRole, long id) throws ErrorDetails;

    User getUserById(long id, String userRole) throws ErrorDetails;
    Set<User> getAllUsers(String userRole) throws ErrorDetails;
    String addUser(User user) throws ErrorDetails;
    String update(String id, String email, String name, String address, String mobileNo, String loggedInUserRole, String loggedInUserIdString) throws ErrorDetails;
    String deleteUser(long id, String userRole) throws ErrorDetails;

    List<User> getAllUsers(int pageNumber, int pageSize, String userRole) throws ErrorDetails;

    long getMaxUserId() throws ErrorDetails;

    // User findByEmail(String email) throws ErrorDetails;
}
