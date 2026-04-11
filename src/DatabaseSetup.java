import java.sql.Connection;
import java.sql.Statement;

/**
 * The DatabaseSetup class is responsible for initializing the database structure for the application.
 *
 * Ensures required table for storing Lego Sets exists in the database.
 *
 * @author Brianna Allen
 * @version 1.0
 *
 */
public class DatabaseSetup {

    /**
     * Creates the Lego_sets table in the database if it does not already exists.
     *
     * @param conn the active database connection
     */
    public static void createTable(Connection conn) {
        String sql = "CREATE TABLE IF NOT EXISTS lego_sets (" +
                "id TEXT PRIMARY KEY," +
                "name TEXT NOT NULL," +
                "pieces INTEGER NOT NULL," +
                "price REAL NOT NULL," +
                "year INTEGER NOT NULL," +
                "age INTEGER NOT NULL" +
                ");";

        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table lego_sets is ready.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}