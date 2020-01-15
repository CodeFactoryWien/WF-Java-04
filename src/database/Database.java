package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

public class Database {

    public static Connection c;

    public static Connection getConnection() throws Exception {
        if (c == null) {
            Class.forName("com.mysql.cj.jdbc.Driver");
            c = DriverManager.getConnection("jdbc:mysql://localhost:3306/wf_java04_hotel", "root", "");
        }
        return c;
    }

    // Send data TO Database
    public static void setData(String sql) throws Exception {
        Database.getConnection().createStatement().executeUpdate(sql);
    }

    // Get Data From Database
    public static ResultSet getData(String sql) throws Exception {
        ResultSet rs = Database.getConnection().createStatement().executeQuery(sql);
        return rs;
    }
}