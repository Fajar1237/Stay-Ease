==================================================================
README - PROYEK AKHIR PEMROGRAMAN BERORIENTASI OBJEK
==================================================================

Mata kuliah    : Pemrograman Berorientasi Objek
Dosen Pengampu : Herika Hayurani

Kelompok [ISI NAMA KELOMPOK]:
  [Nama Fajar Rifai], [1402025021]
  [Nama Diaz Yoga Kurniawan], [1402025016]
  [Nama Malik Agastya Mansha], [1402025030]

------------------------------------------------------------------
DESKRIPSI APLIKASI
------------------------------------------------------------------
Judul Aplikasi : StayEase - Hotel Booking System
Jenis          : Aplikasi Desktop berbasis GUI (Java Swing / NetBeans)

StayEase adalah aplikasi pemesanan hotel berbasis desktop. Aplikasi
memiliki dua peran pengguna:
  - Admin : mengelola data hotel, melihat data user, dan laporan.
  - User  : melakukan registrasi/login, memesan hotel (booking),
            melakukan pembayaran, dan melihat riwayat pemesanan.

Aplikasi terhubung ke database MySQL dan mendukung operasi CRUD penuh
(Create, Read, Update, Delete) pada data hotel, user, booking, dan
pembayaran.

------------------------------------------------------------------
KELENGKAPAN BERKAS
------------------------------------------------------------------
Berkas ini menjelaskan kelengkapan berkas proyek akhir kami yang
berjudul StayEase - Hotel Booking System. Berkas terdiri dari:

1. File database SQL
   - Nama file     : hotel_db.sql
   - Nama database : hotel_db
   - Tabel         : users, hotels, bookings, payments

2. Source code project NetBeans (folder Stay-Ease)
   - Seluruh file gambar berada di dalam project (src/images) dan
     diakses menggunakan relative path / internal resource.
   - Library pendukung berada di src/lib:
       * mysql-connector-j-9.7.0.jar  (koneksi database)
       * jcalendar-1.4.jar            (komponen pemilih tanggal)

3. Tautan Repository GitHub:
   https://github.com/Fajar1237/Stay-Ease

------------------------------------------------------------------
CARA MENJALANKAN
------------------------------------------------------------------
1. Import database: jalankan file hotel_db.sql pada MySQL
   (mis. melalui phpMyAdmin atau MySQL Workbench). Database "hotel_db"
   akan otomatis dibuat.
2. Buka project Stay-Ease menggunakan Apache NetBeans.
3. Pastikan konfigurasi koneksi database pada
   src/stayease/util/DBConnection.java sesuai dengan MySQL Anda
   (host, port, username, password).
4. Jalankan kelas utama: stayease.main.Main
5. Login menggunakan akun yang tersedia di tabel users, atau daftar
   akun baru melalui halaman Register.

------------------------------------------------------------------
STRUKTUR PROJECT (PACKAGE)
------------------------------------------------------------------
stayease.main        -> Main.java (entry point aplikasi)
stayease.model       -> Person, User, Admin, Customer, Hotel,
                        Booking, Payment (kelas data/entitas)
stayease.dao         -> CrudRepository (interface), HotelDAO, UserDAO,
                        BookingDAO, PaymentDAO (akses database / CRUD)
stayease.controller  -> HotelController, BookingController,
                        PaymentController, LoginController
stayease.view        -> seluruh JFrame GUI (NetBeans GUI Builder)
stayease.util        -> DBConnection, PasswordUtil, ImageUtil, Session

------------------------------------------------------------------
PENERAPAN KONSEP OOP (untuk penilaian)
------------------------------------------------------------------
1. Interface (method abstrak, static, default)
   -> stayease.dao.CrudRepository
      - abstract : insert(), update(), delete(), findById(), findAll()
      - default  : getEntityName()
      - static   : formatResult()

2. Class mengimplementasikan interface
   -> HotelDAO implements CrudRepository<Hotel, Integer>

3. Superclass + 2 subclass (inheritance selain extends JFrame)
   -> Superclass : Person (abstract)
      Subclass   : Admin, Customer

4. Overloading
   -> Constructor ter-overload pada User, Hotel, dan Booking
      (beberapa versi constructor dengan parameter berbeda)

5. Overriding
   -> getRole(), getInfo() pada Admin & Customer; toString() pada model

6. Constructor terdefinisi sendiri (bukan default constructor)
   -> tersedia pada seluruh kelas model

7. Enkapsulasi
   -> seluruh field model bersifat private dan diakses melalui
      getter/setter

8. Keyword super (constructor)
   -> Admin & Customer memanggil super(id, username, noTelepon)

9. Keyword super (method)
   -> Admin & Customer memanggil super.getInfo()

10. instanceof + casting (superclass -> subclass)
    -> LoginFrame: if (person instanceof Admin) { Admin a = (Admin) person; }

11. Query CRUD ke database
    -> Insert, Update, Delete, dan Select pada seluruh DAO
       menggunakan JDBC + PreparedStatement.

==================================================================
