import java.sql.Connection;
import java.sql.Statement;

//This class sets up the database structure.
public class DatabaseSetup {

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