package org.jdta.growapp;


import org.jdta.growapp.DAO.CycleDAO;
import org.jdta.growapp.DTO.Cycle;
import org.jdta.growapp.Database.DBConnection;

import java.sql.Connection;
import java.time.LocalDate;

public class CycleDAOTest {
    public static void main(String[] args) {
        try {
            Connection conn = DBConnection.getConnection();
            CycleDAO dao = new CycleDAO(conn);

            Cycle cycle = new Cycle();
            cycle.setUserId(1);
            cycle.setName("Amnesia Auto");
            cycle.setIndoor(true);
            cycle.setPotCapacity(5.5);
            cycle.setStartDate(LocalDate.now());
            cycle.setEtaDate(LocalDate.now().plusDays(70));
            cycle.setImagePath("/photos/amnesia.png");

            dao.insert(cycle);
            System.out.println("Cycle inserted!");

            for (Cycle c : dao.findAll()) {
                System.out.println(c.getName() + " (" + c.getStartDate() + ")");
            }

            DBConnection.closeConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
