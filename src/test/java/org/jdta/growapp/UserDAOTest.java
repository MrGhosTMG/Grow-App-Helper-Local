package org.jdta.growapp;

import org.jdta.growapp.DAO.UserDAO;
import org.jdta.growapp.Database.DBConnection;
import org.jdta.growapp.DTO.User;

import java.sql.Connection;
import java.time.LocalDate;

public class UserDAOTest {
    public static void main(String[] args) {
        try {
            Connection conn = DBConnection.getConnection();
            UserDAO userDAO = new UserDAO(conn);

            User user = new User();
            user.setUsername("testuser");
            user.setEmail("test@example.com");
            user.setPassword("123456");
            user.setCreatedAt(LocalDate.now());

            userDAO.insert(user);
            System.out.println("User inserted!");

            DBConnection.closeConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
