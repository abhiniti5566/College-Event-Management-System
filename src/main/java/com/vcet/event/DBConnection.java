package com.vcet.event;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;
import java.io.InputStream;

public class DBConnection {
    public static Connection getConnection() throws Exception {
        Properties p = new Properties();
        try (InputStream in = DBConnection.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (in == null) throw new IllegalStateException("db.properties not found");
            p.load(in);
        }
        return DriverManager.getConnection(p.getProperty("db.url"),
                p.getProperty("db.user"), p.getProperty("db.password"));
    }
}
