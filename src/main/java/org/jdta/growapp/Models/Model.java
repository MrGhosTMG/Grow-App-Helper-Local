package org.jdta.growapp.Models;

import org.jdta.growapp.DAO.UserDAO;
import org.jdta.growapp.DTO.User;
import org.jdta.growapp.Database.DBConnection;
import org.jdta.growapp.Views.View;

import java.sql.Connection;

public class Model {

    private static Model instance;

    private final View view;
    private final UserDAO userDAO;
    private User currentUser;


    private Model() {
        this.view = new View();
        try {
            Connection connection = DBConnection.getConnection();
            this.userDAO = new UserDAO(connection);
        }catch (Exception e) {
            throw new RuntimeException("Failed connect to database", e);
        }
    }

    public static synchronized Model getInstance() {
        if (instance == null) {
            instance = new Model();
        }
        return instance;
    }



    public View getView() {
        return view;
    }


    public UserDAO getUserDAO() {
        return userDAO;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
}
