package org.jdta.growapp.Service;

import org.jdta.growapp.DAO.UserDAO;
import org.jdta.growapp.DTO.User;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Base64;

public class UserService {
    private final UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    // Registration (new user)
    public boolean registerUser(User user) throws SQLException {
        // Check if username or email already exists
        if (userDAO.findByUsername(user.getUsername()) != null) {
            throw new IllegalArgumentException("User name is already used ");
        }
        if (userDAO.findByEmail(user.getEmail()) != null) {
            throw new IllegalArgumentException("Email already registered");
        }

        // Hash password before saving
        user.setPassword(hashPassword(user.getPassword()));
        userDAO.insert(user);
        return true;
    }

    // Authorisation
    public User login(String username, String password) throws SQLException {
        User user = userDAO.findByUsername(username);
        if (user != null && checkPassword(password, user.getPassword())) {
            return user;
        }
        // Incorrect username/password
        return null;
    }

    // Password hashing using SHA-256
    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Password hashing error", e);
        }
    }

    // Verify password against stored hash
    private boolean checkPassword(String inputPassword, String storedHash) {
        return hashPassword(inputPassword).equals(storedHash);
    }

    // Other methods remain the same...
    public User getUserById(int id) throws SQLException {
        return userDAO.findById(id);
    }



}