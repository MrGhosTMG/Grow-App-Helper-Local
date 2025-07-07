package org.jdta.growapp.Models;


import org.jdta.growapp.DAO.CycleDAO;
import org.jdta.growapp.DAO.UserDAO;
import org.jdta.growapp.DTO.User;
import org.jdta.growapp.Database.DBConnection;
import org.jdta.growapp.Utils.PreferencesUtils;
import org.jdta.growapp.Views.View;

import java.sql.Connection;

public class Model {

    private static Model instance;

    private final View view;
    private final UserDAO userDAO;
    private User currentUser;
    private final CycleDAO cycleDAO;


    private Model() {
        this.view = new View();

        try {
            Connection connection = DBConnection.getConnection(); // ← СНАЧАЛА получаем соединение

            DBConnection.initTables(); // можно инициализировать структуру
            this.userDAO = new UserDAO(connection);
            this.cycleDAO = new CycleDAO(connection); // ← теперь всё ок

        } catch (Exception e) {
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

    public void logout() {
        PreferencesUtils.clearUser();
        currentUser = null;
        getView().showLoginWindow();
    }


    public CycleDAO getCycleDAO() {
        return cycleDAO;
    }
}
