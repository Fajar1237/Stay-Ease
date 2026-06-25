package stayease.controller;

import stayease.dao.HotelDAO;
import stayease.model.Hotel;

import java.util.List;

/**
 * Jembatan antara view (dashboard user & kelola hotel admin) dan HotelDAO.
 */
public class HotelController {

    private final HotelDAO hotelDAO = new HotelDAO();

    /** Semua hotel — untuk kartu di UserDashboardFrame. */
    public List<Hotel> getSemuaHotel() {
        return hotelDAO.getAllHotels();
    }

    /** Cari hotel berdasarkan nama/lokasi. */
    public List<Hotel> cariHotel(String keyword) {
        return hotelDAO.searchHotels(keyword);
    }

    /** Ambil satu hotel berdasarkan id. */
    public Hotel getHotel(int hotelId) {
        return hotelDAO.getHotelById(hotelId);
    }

    /** Tambah hotel (admin). @return id baru, -1 bila gagal. */
    public int tambahHotel(Hotel hotel) {
        return hotelDAO.insert(hotel);
    }

    /** Ubah data hotel (admin). */
    public boolean ubahHotel(Hotel hotel) {
        return hotelDAO.update(hotel);
    }

    /** Hapus hotel (admin). */
    public boolean hapusHotel(int hotelId) {
        return hotelDAO.delete(hotelId);
    }
}
