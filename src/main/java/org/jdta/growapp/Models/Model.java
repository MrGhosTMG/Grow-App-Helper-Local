package org.jdta.growapp.Models;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.jdta.growapp.DAO.CycleDAO;
import org.jdta.growapp.DAO.UserDAO;
import org.jdta.growapp.DTO.Cycle;
import org.jdta.growapp.DTO.User;
import org.jdta.growapp.Database.DBConnection;
import org.jdta.growapp.Utils.PreferencesUtils;
import org.jdta.growapp.Views.View;

import java.sql.Connection;

public class Model {

    private static Model instance;
    private final ObservableList<Cycle> finishedCycles = FXCollections.observableArrayList();
    private final View view;
    private final UserDAO userDAO;
    private User currentUser;
    private final CycleDAO cycleDAO;
    private boolean isFinishedCycle = false;
    private Cycle currentCycle;

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

    public void addFinishedCycle(Cycle currentCycle) {
        if (!finishedCycles.contains(currentCycle)) {
            finishedCycles.add(currentCycle);
        }
    }

    public ObservableList<Cycle> getFinishedCycles() {
        return finishedCycles;
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

    public boolean isFinishedCycle() {
        return isFinishedCycle;
    }

    public void setFinishedCycle(boolean finishedCycle) {
        isFinishedCycle = finishedCycle;
    }
    public Cycle getCurrentCycle() {
        return currentCycle;
    }

    public void setCurrentCycle(Cycle currentCycle) {
        this.currentCycle = currentCycle;
    }

    // Mock Cycle loading
    public void mockCycleIfNone() {
        if (currentCycle == null && !finishedCycles.isEmpty()) {
            setCurrentCycle(finishedCycles.get(0));
        }
    }
}
