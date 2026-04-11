import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Establishes a connection to a SQlite database using a provided file path.
 @return connection object if connection is successful; null if connection fails
 */
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