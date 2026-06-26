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
 * Akses data tabel `hotels` memakai JDBC + PreparedStatement.
 *
 * Kolom tabel: hotel_id, nama, lokasi, deskripsi, harga, gambar.
 * Fitur: insert, update, delete, get by id, get all, search.
 */
public class HotelDAO {

    /**
     * INSERT — menambah hotel baru.
     * @return hotel_id yang ter-generate, atau -1 bila gagal.
     */
    public int insert(Hotel hotel) {
        String sql = "INSERT INTO hotels (nama, lokasi, deskripsi, harga, gambar) VALUES (?, ?, ?, ?, ?)";
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
     * UPDATE — memperbarui data hotel berdasarkan hotel_id.
     * @return true bila ada baris yang diperbarui.
     */
    public boolean update(Hotel hotel) {
        String sql = "UPDATE hotels SET nama = ?, lokasi = ?, deskripsi = ?, harga = ?, gambar = ? "
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
     * DELETE — menghapus hotel berdasarkan hotel_id.
     * @return true bila ada baris yang terhapus.
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
     * GET HOTEL BY ID — mengambil satu hotel berdasarkan hotel_id.
     * @return Hotel bila ditemukan; null bila tidak ada.
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
            throw new RuntimeException("Failed to book a hotel: " + e.getMessage(), e);
        }
        return null;
    }

    /**
     * GET ALL HOTEL — mengambil seluruh hotel, diurutkan berdasarkan hotel_id.
     * @return daftar Hotel (kosong bila tabel kosong).
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
            throw new RuntimeException("Failed to retrieve the list of hotels: " + e.getMessage(), e);
        }
        return list;
    }

    /**
     * SEARCH HOTEL — mencari hotel berdasarkan nama ATAU lokasi (sebagian cocok).
     * @param keyword kata kunci pencarian.
     * @return daftar Hotel yang cocok (kosong bila tidak ada).
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
            throw new RuntimeException("Unable to find a hotel: " + e.getMessage(), e);
        }
        return list;
    }

    // ----------------------------------------------------------------
    /** Mengubah satu baris ResultSet menjadi objek Hotel. */
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