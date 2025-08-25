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
        String[] sqlStatements = new String[]{
                """
        CREATE TABLE IF NOT EXISTS users (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            username TEXT NOT NULL,
            email TEXT NOT NULL UNIQUE,
            password TEXT NOT NULL,
            created_at TEXT DEFAULT CURRENT_TIMESTAMP
        )
        """,
                """
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
            light_set_time TEXT,
            grow_stage TEXT,
            FOREIGN KEY(user_id) REFERENCES users(id)
        )
        """,
                """
        CREATE TABLE IF NOT EXISTS notes (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            cycle_id INTEGER NOT NULL,
            content TEXT NOT NULL,
            created_at TEXT DEFAULT CURRENT_TIMESTAMP,
            FOREIGN KEY(cycle_id) REFERENCES cycles(id)
        )
        """,
                """
        CREATE TABLE IF NOT EXISTS photos (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            cycle_id INTEGER NOT NULL,
            path TEXT NOT NULL,
            timestamp TEXT DEFAULT CURRENT_TIMESTAMP,
            FOREIGN KEY(cycle_id) REFERENCES cycles(id)
        )
        """,
                """
        CREATE TABLE IF NOT EXISTS Alarm (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            cycle_id INTEGER NOT NULL,
            alarm_type TEXT NOT NULL,
            alarm_date TEXT NOT NULL,
            note TEXT,
            created_at TEXT DEFAULT CURRENT_TIMESTAMP,
            FOREIGN KEY (cycle_id) REFERENCES cycles(id) ON DELETE CASCADE
        )
        """,
                """
        CREATE TABLE IF NOT EXISTS stage_transitions (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            cycle_id INTEGER NOT NULL,
            stage TEXT NOT NULL,
            start_date TEXT NOT NULL,
            duration_days INTEGER,
            FOREIGN KEY (cycle_id) REFERENCES cycles(id)
        )
        """
        };

        try {
            Connection conn = getConnection();
            for (String sql : sqlStatements) {
                try (Statement stmt = conn.createStatement()) {
                    stmt.executeUpdate(sql);
                }
            }
            System.out.println("✅ Все таблицы инициализированы. Connection open: " + !conn.isClosed());
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("❌ Не удалось инициализировать таблицы", e);
        }
    }


}
