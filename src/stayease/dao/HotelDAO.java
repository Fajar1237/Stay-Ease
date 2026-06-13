package stayease.dao;

import stayease.model.Hotel;

import java.util.ArrayList;
import java.util.List;

/**
 * Akses data tabel hotels (CRUD hotel + tampilan daftar hotel).
 * TODO: lengkapi isi setiap method.
 */
public class HotelDAO {

    public List<Hotel> getAllHotels() {
        // TODO: SELECT * FROM hotels
        return new ArrayList<>();
    }

    public Hotel getHotelById(int hotelId) {
        // TODO: SELECT * FROM hotels WHERE hotel_id=?
        return null;
    }

    public boolean addHotel(Hotel hotel) {
        // TODO: INSERT INTO hotels (...)
        return false;
    }

    public boolean updateHotel(Hotel hotel) {
        // TODO: UPDATE hotels SET ... WHERE hotel_id=?
        return false;
    }

    public boolean deleteHotel(int hotelId) {
        // TODO: DELETE FROM hotels WHERE hotel_id=?
        return false;
    }
}
