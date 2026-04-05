import java.sql.*;
import java.util.ArrayList;
import java.util.List;

//Data Access Object for interacting with lego set tables
public class LegoDAO {

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

    public static void deleteSet(Connection conn, String id) {
        String sql = "DELETE FROM lego_sets WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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