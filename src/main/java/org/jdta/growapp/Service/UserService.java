package org.jdta.growapp.Service;

import org.jdta.growapp.DAO.UserDAO;
import org.jdta.growapp.DTO.User;

import java.sql.SQLException;
import java.time.LocalDate;

public class UserService {


    private final UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }



    //Registration (new user)
    public boolean registerUser(User user) throws SQLException {
        //Check if username is allowed
        if (userDAO.findByUsername(user.getUsername()) != null) {
            return false ; //user_name is already used
        }
        userDAO.insert(user);
        return true;
    }

    //Authorisation
    public User login(String username, String password) throws SQLException {
        User user = userDAO.findByUsername(username);

        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null; //Incorrect username/password
    }
    //get user by ID
    public User getUserById(int id) throws SQLException {
        return userDAO.findById(id);
    }

    //get user by username


}
