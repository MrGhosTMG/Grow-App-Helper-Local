package org.jdta.growapp.Models;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.jdta.growapp.DAO.AlarmDAO;
import org.jdta.growapp.DAO.CycleDAO;
import org.jdta.growapp.DAO.StageTransitionDAO;
import org.jdta.growapp.DAO.UserDAO;
import org.jdta.growapp.DTO.Cycle;
import org.jdta.growapp.DTO.StageTransition;
import org.jdta.growapp.DTO.User;
import org.jdta.growapp.Database.DBConnection;
import org.jdta.growapp.Enums.GrowStages;
import org.jdta.growapp.Utils.PreferencesUtils;
import org.jdta.growapp.Views.View;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Model {

    private static Model instance;
    private final ObservableList<Cycle> finishedCycles = FXCollections.observableArrayList();
    private final View view;
    private final UserDAO userDAO;
    private User currentUser;
    private final CycleDAO cycleDAO;
    private final StageTransitionDAO stageTransition;
    private boolean isFinishedCycle = false;
    private Cycle selectedCycle;

    private Model() {
        this.view = new View();

        try {
            Connection connection = DBConnection.getConnection(); // ← СНАЧАЛА получаем соединение

            DBConnection.initTables();
            this.userDAO = new UserDAO(connection);
            this.cycleDAO = new CycleDAO(connection);
            this.stageTransition = new StageTransitionDAO(connection);

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
    public Cycle getSelectedCycle() {
        return selectedCycle;
    }

    public void setSelectedCycle(Cycle selectedCycle) {
        this.selectedCycle = selectedCycle;
    }

    // Mock Cycle loading
    public void mockCycleIfNone() {
        if (selectedCycle == null && !finishedCycles.isEmpty()) {
            setSelectedCycle(finishedCycles.get(0));
        }
    }

    public void loadCycleTransitions(Cycle cycle) {

        if (cycle == null) return;

        StageTransitionDAO dao = getStageTransitionDAO();
        List<StageTransition> transitions = dao.findAllByCycleId(cycle.getId());

        cycle.getStageStartDates().clear();
        cycle.getStageDurationDays().clear();

        for (StageTransition t : transitions) {
            cycle.getStageStartDates().put(t.getGrowStages(), t.getStartDate());
            cycle.getStageDurationDays().put(t.getGrowStages(), t.getDurationsDays());
        }

        System.out.println("Transitions loaded for cycle " + cycle.getId() + ": " + transitions.size());

    }
//    public void loadCycleTransitions(Cycle cycle) {
//
//            List<StageTransition> transitions = getStageTransitionDAO().findAllByCycleId(cycle.getId());
//
//            Map<GrowStages, LocalDate> startDates = new HashMap<>();
//            Map<GrowStages, Integer> durations = new HashMap<>();
//
//            for (StageTransition t : transitions) {
//                startDates.put(t.getGrowStages(), t.getStartDate());
//                durations.put(t.getGrowStages(), t.getDurationsDays());
//            }
//            cycle.getStageStartDates().clear();
//            cycle.getStageStartDates().putAll(startDates);
//
//            cycle.getStageDurationDays().clear();
//            cycle.getStageDurationDays().putAll(durations);
//
//        System.out.println("Transitions loaded for cycle " + cycle.getId() + ": " + transitions.size());
//
//    }

    public StageTransitionDAO getStageTransitionDAO() {return stageTransition;}
}
