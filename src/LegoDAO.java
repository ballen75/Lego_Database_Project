import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The LegoDAO class acts as the Data Access Object for interacting with the lego_sets table in the SQLite database
 *
 * Provides methods to crate, read, update and delete (CRUD) legoset records.
 */
public class LegoDAO {

    /**
     * Checks if a Lego set with the given ID already exists in the database
     *
     * @param conn active database connection
     * @param legoSetID the ID of the lego set to check
     * @return true if the set exists, false otherwise.
     */
    public static boolean exists(Connection conn, String legoSetID) {
        String sql = "SELECT COUNT(*) FROM lego_sets WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, legoSetID);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Inserts a new Lego set in the database.
     *
     * @param conn active database connection
     * @param set the LegoSet object to insert
     */
    public static void addSet(Connection conn, LegoSet set) {
        String sql = "INSERT INTO lego_sets(id, name, pieces, price, year, age) VALUES(?,?,?,?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, set.getLegoSetID());
            stmt.setString(2, set.getSetName());
            stmt.setInt(3, set.getPieceCount());
            stmt.setDouble(4, set.getPrice());
            stmt.setInt(5, set.getReleaseYear());
            stmt.setInt(6, set.getRecommendedAge());
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes lego set from the database by ID.
     *
     * @param conn active database connection
     * @param id the ID of the Lego set to delete
     */
    public static void deleteSet(Connection conn, String id) {
        String sql = "DELETE FROM lego_sets WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates an existing Lego set in the database.
     *
     * @param conn the active database connection
     * @param set the LegoSet object containing updated values
     */
    public static void updateSet(Connection conn, LegoSet set) {
        String sql = "UPDATE lego_sets SET name=?, pieces=?, price=?, year=?, age=? WHERE id=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, set.getSetName());
            stmt.setInt(2, set.getPieceCount());
            stmt.setDouble(3, set.getPrice());
            stmt.setInt(4, set.getReleaseYear());
            stmt.setInt(5, set.getRecommendedAge());
            stmt.setString(6, set.getLegoSetID());
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieces Lego Sets from the database.
     *
     * @param conn active database connection
     * @return list of all LegoSet objects stored in the database.
     */

    public static List<LegoSet> getAllSets(Connection conn) {
        List<LegoSet> sets = new ArrayList<>();
        String sql = "SELECT * FROM lego_sets";
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                sets.add(new LegoSet(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getInt("pieces"),
                        rs.getDouble("price"),
                        rs.getInt("year"),
                        rs.getInt("age")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sets;
    }
}