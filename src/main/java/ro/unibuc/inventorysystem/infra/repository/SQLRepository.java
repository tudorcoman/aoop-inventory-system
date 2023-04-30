package ro.unibuc.inventorysystem.infra.repository;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public enum SQLRepository {
    INSTANCE;

    private Connection connection;
    SQLRepository() {
        final Properties properties = new Properties();

        try (InputStream is = getClass().getResourceAsStream("/application.properties")) {
            properties.load(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            final String url = properties.getProperty("database.url");
            final String user = properties.getProperty("database.username");
            final String password = properties.getProperty("database.password");

            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    ResultSet getObjects(String tableName) {
        try {
            return connection.createStatement().executeQuery("SELECT * FROM " + tableName);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

//    int executeInsert(PreparedStatement ps) {
//        try {
//            ps.executeUpdate();
//            final ResultSet rs = ps.getGeneratedKeys();
//            rs.next();
//            return rs.getInt(1);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    void executeUpdate(PreparedStatement ps) {
//        try {
//            ps.executeUpdate();
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }

    public PreparedStatement createPreparedStatement(String query) throws SQLException {
        return connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
    }

    public Connection getConnection() {
        return connection;
    }
}
