package stayease.dao;

import stayease.model.Hotel;
import stayease.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * HotelDAO
 * ---------------------------------------------------------------------------
 * Handles all database operations for the `hotels` table using JDBC and
 * PreparedStatement.
 *
 * Table columns: hotel_id, nama, lokasi, deskripsi, harga, gambar.
 * Operations  : insert, update, delete, getHotelById, getAllHotels, searchHotels.
 *
 * [OOP ADDITION] HotelDAO now implements CrudRepository<Hotel, Integer>.
 *
 * Why HotelDAO was chosen to implement the interface:
 *   - It already has insert(), update(), and delete() with matching names.
 *   - Its CRUD operations are the most complete of all DAO classes.
 *   - Only two bridge methods (findById, deleteById) need to be added.
 *
 * IMPORTANT: All existing methods are kept UNCHANGED so that every View and
 * Controller that already calls them continues to work without any modification.
 */
public class HotelDAO implements CrudRepository<Hotel, Integer> {

    // =========================================================================
    // ORIGINAL METHODS  —  unchanged; still used by all Views and Controllers
    // =========================================================================

    /**
     * INSERT — adds a new hotel to the database.
     *
     * @param hotel the Hotel object to save
     * @return the generated hotel_id, or -1 if the operation failed
     */
    @Override
    public int insert(Hotel hotel) {
        String sql = "INSERT INTO hotels (nama, lokasi, deskripsi, harga, gambar) "
                   + "VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, hotel.getNama());
            ps.setString(2, hotel.getLokasi());
            ps.setString(3, hotel.getDeskripsi());
            ps.setBigDecimal(4, hotel.getHarga());
            ps.setString(5, hotel.getGambar());
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save hotel: " + e.getMessage(), e);
        }
        return -1;
    }

    /**
     * UPDATE — updates an existing hotel record by hotel_id.
     *
     * @param hotel the Hotel object with updated data
     * @return true if at least one row was updated
     */
    @Override
    public boolean update(Hotel hotel) {
        String sql = "UPDATE hotels "
                   + "SET nama = ?, lokasi = ?, deskripsi = ?, harga = ?, gambar = ? "
                   + "WHERE hotel_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, hotel.getNama());
            ps.setString(2, hotel.getLokasi());
            ps.setString(3, hotel.getDeskripsi());
            ps.setBigDecimal(4, hotel.getHarga());
            ps.setString(5, hotel.getGambar());
            ps.setInt(6, hotel.getHotelId());
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to update the hotel: " + e.getMessage(), e);
        }
    }

    /**
     * DELETE — removes a hotel by hotel_id (primitive int version).
     * Used directly by KelolaHotel.java and other Views — kept unchanged.
     *
     * @param hotelId the hotel_id to delete
     * @return true if at least one row was deleted
     */
    public boolean delete(int hotelId) {
        String sql = "DELETE FROM hotels WHERE hotel_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, hotelId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete the hotel: " + e.getMessage(), e);
        }
    }

    /**
     * GET HOTEL BY ID — retrieves a single hotel by hotel_id.
     * Used directly by Views — kept unchanged.
     *
     * @param hotelId the primary key to look up
     * @return the Hotel object, or null if not found
     */
    public Hotel getHotelById(int hotelId) {
        String sql = "SELECT * FROM hotels WHERE hotel_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, hotelId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to retrieve hotel: " + e.getMessage(), e);
        }
        return null;
    }

    /**
     * GET ALL HOTELS — retrieves all hotels ordered by hotel_id.
     * Used directly by Views — kept unchanged.
     *
     * @return a List of all Hotel objects; empty if the table is empty
     */
    public List<Hotel> getAllHotels() {
        String sql = "SELECT * FROM hotels ORDER BY hotel_id";
        List<Hotel> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to retrieve hotel list: " + e.getMessage(), e);
        }
        return list;
    }

    /**
     * SEARCH HOTELS — finds hotels by name or location (partial match).
     *
     * @param keyword the search keyword
     * @return a List of matching Hotel objects; empty if none found
     */
    public List<Hotel> searchHotels(String keyword) {
        String sql = "SELECT * FROM hotels WHERE nama LIKE ? OR lokasi LIKE ? ORDER BY nama";
        List<Hotel> list = new ArrayList<>();
        String like = "%" + (keyword == null ? "" : keyword.trim()) + "%";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, like);
            ps.setString(2, like);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to search hotels: " + e.getMessage(), e);
        }
        return list;
    }

    // =========================================================================
    // [OOP ADDITION]  CrudRepository<Hotel, Integer> bridge methods
    //
    // These methods satisfy the interface contract WITHOUT changing any logic.
    // They simply delegate to the original methods above.
    // Existing Views and Controllers do NOT need to be updated.
    // =========================================================================

    /**
     * Satisfies CrudRepository.findById(ID id).
     * Delegates to getHotelById(int) — no logic change.
     *
     * @param id hotel_id wrapped as Integer
     * @return the Hotel object, or null if not found
     */
    @Override
    public Hotel findById(Integer id) {
        return getHotelById(id);   // unboxing handled automatically by Java
    }

    /**
     * Satisfies CrudRepository.findAll().
     * Delegates to getAllHotels() — no logic change.
     *
     * @return a List of all Hotel objects
     */
    @Override
    public List<Hotel> findAll() {
        return getAllHotels();
    }

    /**
     * Satisfies CrudRepository.delete(ID id).
     * Delegates to delete(int) — no logic change.
     *
     * @param id hotel_id wrapped as Integer
     * @return true if the deletion was successful
     */
    @Override
    public boolean delete(Integer id) {
        return delete((int) id);   // unbox Integer → int, then call original method
    }

    // =========================================================================
    // PRIVATE HELPER
    // =========================================================================

    /** Maps one ResultSet row to a Hotel object. */
    private Hotel mapRow(ResultSet rs) throws SQLException {
        return new Hotel(
                rs.getInt("hotel_id"),
                rs.getString("nama"),
                rs.getString("lokasi"),
                rs.getString("deskripsi"),
                rs.getBigDecimal("harga"),
                rs.getString("gambar"));
    }
}
