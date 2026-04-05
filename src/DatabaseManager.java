import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseManager {

    public static Connection connect(String dbPath) {
        try {
            String url = "jdbc:sqlite:" + dbPath;
            Connection conn = DriverManager.getConnection(url);
            System.out.println("Connected to SQLite: " + dbPath);
            return conn;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}