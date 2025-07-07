package org.jdta.growapp.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {
    private static final String DB_PATH = "src/main/resources/growhelper.db";
    private static final String URL = "jdbc:sqlite:" + DB_PATH;
    private static boolean initializedGlobally = false;
    private static Connection connection;

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(URL);
            initializedGlobally = true;
        }
        return connection;
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void initTables() {
        String sql = """
        CREATE TABLE IF NOT EXISTS users (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            username TEXT NOT NULL,
            email TEXT NOT NULL UNIQUE,
            password TEXT NOT NULL,
            created_at TEXT DEFAULT CURRENT_TIMESTAMP
        );
        CREATE TABLE IF NOT EXISTS cycles (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            user_id INTEGER NOT NULL,
            cycle_name TEXT NOT NULL,
            indoor_outdoor TEXT NOT NULL,
            sort_type TEXT NOT NULL,
            start_date TEXT,
            estimated_end_date TEXT,
            pot_capacity REAL,
            notes TEXT,
            light_day_hours INTEGER,
            light_night_hours INTEGER,
            image_path TEXT,
            FOREIGN KEY(user_id) REFERENCES users(id)
        );
        CREATE TABLE IF NOT EXISTS notes (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            cycle_id INTEGER NOT NULL,
            content TEXT NOT NULL,
            created_at TEXT DEFAULT CURRENT_TIMESTAMP,
            FOREIGN KEY(cycle_id) REFERENCES cycles(id)
        );
        CREATE TABLE IF NOT EXISTS photos (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            cycle_id INTEGER NOT NULL,
            path TEXT NOT NULL,
            timestamp TEXT DEFAULT CURRENT_TIMESTAMP,
            FOREIGN KEY(cycle_id) REFERENCES cycles(id)
        );
    """;

        try {
            // ⚠️ не используем try-with-resources здесь!
            Connection conn = getConnection(); // глобальное соединение
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);
            stmt.close(); // закрываем только Statement, а не Connection!
            System.out.println("Connection open: " + !connection.isClosed());

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Не удалось инициализировать таблицы", e);
        }
    }


}
