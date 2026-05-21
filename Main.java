import java.util.ArrayList;
import java.util.Scanner;

/**
 * Kelas Utama (Main) yang mengendalikan seluruh alur aplikasi Restoran.
 * Kelas ini memuat logika navigasi menu (Pelanggan & Pemilik), manajemen data (CRUD)
 * menggunakan ArrayList, serta kalkulasi kasir beserta pajaknya.
 */
public class Main {
    // Menggunakan ArrayList bawaan java.util agar alokasi memori menu bersifat dinamis (bisa tambah/hapus)
    static ArrayList<Menu> daftarMenu = new ArrayList<>();
    static Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        // 1. Inisialisasi data awal (Minimal 4 makanan dan 4 minuman sesuai instruksi soal)
        inisialisasiMenuAwal();

        // 2. Loop Utama Aplikasi (Aplikasi berjalan terus hingga dipilih opsi Keluar)
        boolean berjalan = true;
        while (berjalan) {
            System.out.println("\n====================================");
            System.out.println("     SISTEM APLIKASI RESTORAN       ");
            System.out.println("====================================");
            System.out.println("1. Menu Pelanggan (Pemesanan)");
            System.out.println("2. Menu Pemilik Restoran (Pengelolaan)");
            System.out.println("3. Keluar Aplikasi");
            System.out.print("Pilih Hak Akses (1-3): ");
            
            String pilihan = input.nextLine();
            switch (pilihan) {
                case "1":
                    menuPelanggan();
                    break;
                case "2":
                    menuPemilik();
                    break;
                case "3":
                    System.out.println("Terima kasih telah menggunakan aplikasi ini.");
                    berjalan = false;
                    break;
                default:
                    System.out.println("Input tidak valid! Silakan pilih nomor 1 sampai 3.");
            }
        }
    }

    /**
     * METHOD: Inisialisasi Menu Awal
     * Berfungsi untuk mengisi data default restoran ke dalam ArrayList saat aplikasi pertama kali dimuat.
     * Menjamin batas minimal pemenuhan tugas (4 makanan & 4 minuman).
     */
    static void inisialisasiMenuAwal() {
        // Kategori Makanan
        daftarMenu.add(new Menu("Nasi Goreng Spesial", 25000, "Makanan"));
        daftarMenu.add(new Menu("Mie Ayam Pangsit", 18000, "Makanan"));
        daftarMenu.add(new Menu("Ayam Bakar Taliwang", 30000, "Makanan"));
        daftarMenu.add(new Menu("Sate Ayam Madura", 22000, "Makanan"));

        // Kategori Minuman
        daftarMenu.add(new Menu("Es Teh Manis", 5000, "Minuman"));
        daftarMenu.add(new Menu("Jus Alpukat Kocok", 15000, "Minuman"));
        daftarMenu.add(new Menu("Es Jeruk Peras", 7000, "Minuman"));
        daftarMenu.add(new Menu("Kopi Susu Gula Aren", 12000, "Minuman"));
    }

    /**
     * METHOD: Menampilkan Daftar Menu
     * Menampilkan isi ArrayList secara terstruktur ke layar monitor.
     * Memenuhi syarat format khusus: Data wajib dikelompokkan berdasarkan kategorinya.
     */
    static void tampilkanDaftarMenu() {
        System.out.println("\n====================================");
        System.out.println("        DAFTAR MENU RESTORAN        ");
        System.out.println("====================================");
        
        System.out.println("[ KATEGORI: MAKANAN ]");
        int nomor = 1;
        // Perulangan untuk memfilter dan menampilkan Makanan
        for (int i = 0; i < daftarMenu.size(); i++) {
            if (daftarMenu.get(i).kategori.equalsIgnoreCase("Makanan")) {
                System.out.printf("%d. %-25s | Rp %,.0f\n", (i + 1), daftarMenu.get(i).nama, daftarMenu.get(i).harga);
            }
        }

        System.out.println("\n[ KATEGORI: MINUMAN ]");
        // Perulangan untuk memfilter dan menampilkan Minuman
        for (int i = 0; i < daftarMenu.size(); i++) {
            if (daftarMenu.get(i).kategori.equalsIgnoreCase("Minuman")) {
                System.out.printf("%d. %-25s | Rp %,.0f\n", (i + 1), daftarMenu.get(i).nama, daftarMenu.get(i).harga);
            }
        }
        System.out.println("====================================");
    }

    /**
     * METHOD: Menu Pelanggan (Fitur Pemesanan)
     * Mengelola proses transaksi pemesanan konsumen. 
     * Memiliki loop tak terbatas (while) yang hanya akan berhenti jika mengetik kata 'selesai'.
     * Dilengkapi validasi ketat (input di luar daftar menu akan diminta ulang secara terus-menerus).
     */
    static void menuPelanggan() {
        // Array lokal untuk menampung pesanan aktif pada sesi ini
        ArrayList<Menu> pesananItem = new ArrayList<>();
        ArrayList<Integer> pesananJumlah = new ArrayList<>();

        while (true) {
            tampilkanDaftarMenu();
            System.out.println("Ketik 'selesai' jika sudah cukup memesan.");
            System.out.print("Masukkan nomor menu yang ingin dipesan: ");
            String inputUser = input.nextLine();

            if (inputUser.equalsIgnoreCase("selesai")) {
                break; // Keluar dari loop pemesanan jika mengetik selesai
            }

            try {
                int nomorMenu = Integer.parseInt(inputUser);
                // Validasi rentang indeks nomor menu yang terdaftar di ArrayList
                if (nomorMenu < 1 || nomorMenu > daftarMenu.size()) {
                    System.out.println("Nomor menu tidak terdaftar! Silakan ulangi.");
                    continue; // Putar kembali loop untuk meminta input yang benar
                }

                System.out.print("Masukkan jumlah porsi/gelas: ");
                int jumlah = Integer.parseInt(input.nextLine());
                if (jumlah <= 0) {
                    System.out.println("Jumlah pesanan minimal 1! Pilihan dibatalkan.");
                    continue;
                }

                // Simpan ke daftar belanjaan sementara
                pesananItem.add(daftarMenu.get(nomorMenu - 1));
                pesananJumlah.add(jumlah);
                System.out.println("-> Berhasil menambahkan ke daftar pesanan.");

            } catch (NumberFormatException e) {
                System.out.println("Input tidak valid! Masukkan angka nomor menu atau ketik 'selesai'.");
            }
        }

        // Jika pelanggan memiliki pesanan, eksekusi perhitungan biaya dan struk
        if (!pesananItem.isEmpty()) {
            prosesTransaksi(pesananItem, pesananJumlah);
        } else {
            System.out.println("Anda tidak memesan apapun. Kembali ke menu utama.");
        }
    }

    /**
     * METHOD: Proses Transaksi & Hitung Biaya
     * Menghitung subtotal belanja dasar, mengecek kelayakan diskon 10%, 
     * kalkulasi bonus promo Beli 1 Gratis 1 minuman (>Rp 50rb), menghitung pajak 10%, 
     * menambahkan biaya pelayanan tetap Rp 20.000, serta mencetak struk komprehensif ke layar.
     */
    static void prosesTransaksi(ArrayList<Menu> items, ArrayList<Integer> jumlahs) {
        double subtotalBelanja = 0;
        
        // 1. Hitung total dasar item terjual
        for (int i = 0; i < items.size(); i++) {
            subtotalBelanja += items.get(i).harga * jumlahs.get(i);
        }

        // 2. Evaluasi Potongan Promo Beli 1 Gratis 1 Kategori Minuman (Syarat: Subtotal > Rp 50.000)
        double totalPotonganB1G1 = 0;
        String infoPromoB1G1 = "";
        if (subtotalBelanja > 50000) {
            for (int i = 0; i < items.size(); i++) {
                if (items.get(i).kategori.equalsIgnoreCase("Minuman")) {
                    int jumlahBeli = jumlahs.get(i);
                    int itemGratis = jumlahBeli / 2; // Setiap kelipatan 2 dpt 1 gratis
                    if (itemGratis > 0) {
                        double potongan = itemGratis * items.get(i).harga;
                        totalPotonganB1G1 += potongan;
                        infoPromoB1G1 += String.format("   * Promo B1G1 %s (%d gratis): -Rp %,.0f\n", items.get(i).nama, itemGratis, potongan);
                    }
                }
            }
        }

        // 3. Evaluasi Diskon 10% (Syarat: Subtotal > Rp 100.000)
        double diskonUmum = 0;
        if (subtotalBelanja > 100000) {
            diskonUmum = 0.10 * subtotalBelanja;
        }

        // 4. Kalkulasi Komponen Biaya Tambahan Operasional Restoran
        double subtotalSetelahDiskon = subtotalBelanja - totalPotonganB1G1 - diskonUmum;
        double pajak = 0.10 * subtotalSetelahDiskon;
        double biayaPelayanan = 20000;
        double totalAkhirBayar = subtotalSetelahDiskon + pajak + biayaPelayanan;

        // 5. PENYUSUNAN STRUK PEMBAYARAN KELAYAR MONITOR (Sesuai Struktur Keputusan Tugas)
        System.out.println("\n========================================");
        System.out.println("             STRUK PESANAN              ");
        System.out.println("========================================");
        for (int i = 0; i < items.size(); i++) {
            double totalPerItem = items.get(i).harga * jumlahs.get(i);
            System.out.printf("%-22s x%d  Rp %,.0f\n", items.get(i).nama, jumlahs.get(i), totalPerItem);
        }
        System.out.println("----------------------------------------");
        System.out.printf("Total Biaya Item       : Rp %,.0f\n", subtotalBelanja);
        
        // Cetak Info Promo Jika Kondisi Terpenuhi
        if (totalPotonganB1G1 > 0) {
            System.out.print(infoPromoB1G1);
        }
        if (diskonUmum > 0) {
            System.out.printf("   * Diskon Khusus 10%% : -Rp %,.0f\n", diskonUmum);
        }
        if (totalPotonganB1G1 > 0 || diskonUmum > 0) {
            System.out.printf("Total Setelah Diskon   : Rp %,.0f\n", subtotalSetelahDiskon);
        }
        
        System.out.printf("Pajak Restoran (10%%)   : Rp %,.0f\n", pajak);
        System.out.printf("Biaya Pelayanan        : Rp %,.0f\n", biayaPelayanan);
        System.out.println("----------------------------------------");
        System.out.printf("TOTAL AKHIR TAGIHAN    : Rp %,.0f\n", totalAkhirBayar);
        System.out.println("========================================");
    }

    /**
     * METHOD: Menu Pemilik (Fitur Pengelolaan / Admin)
     * Gerbang navigasi kontrol untuk pemilik usaha. Menyediakan fungsionalitas CRUD internal 
     * untuk memanipulasi data master menu restoran. 
     * Memiliki fitur pengembalian arah menu (*parent navigation back*).
     */
    static void menuPemilik() {
        boolean diMenuAdmin = true;
        while (diMenuAdmin) {
            System.out.println("\n====================================");
            System.out.println("    MANAJEMEN PENGELOLAAN RESTORAN  ");
            System.out.println("====================================");
            System.out.println("1. Tambah Menu Baru");
            System.out.println("2. Ubah Harga Menu");
            System.out.println("3. Hapus Menu");
            System.out.println("4. Kembali ke Menu Utama Parent");
            System.out.print("Pilih Aksi Pengelolaan (1-4): ");

            String pilihan = input.nextLine();
            switch (pilihan) {
                case "1":
                    tambahMenuBaru();
                    break;
                case "2":
                    ubahHargaMenu();
                    break;
                case "3":
                    hapusMenuRestoran();
                    break;
                case "4":
                    diMenuAdmin = false; // Memutuskan loop untuk kembali ke menu parent utama
                    break;
                default:
                    System.out.println("Input salah! Silakan masukkan pilihan nomor 1 sampai 4.");
            }
        }
    }

    /**
     * METHOD: Tambah Menu Baru
     * Memungkinkan penambahan item menu baru secara dinamis ke dalam ArrayList.
     * Menggunakan pengulangan sehingga pemilik bisa menambahkan beberapa menu sekaligus tanpa harus keluar menu.
     */
    static void tambahMenuBaru() {
        boolean lanjutTambah = true;
        while (lanjutTambah) {
            System.out.println("\n--- Fitur Tambah Menu Baru ---");
            System.out.print("Masukkan Nama Menu Baru: ");
            String nama = input.nextLine();

            double harga = 0;
            // Loop validasi input angka harga agar tidak crash
            while (true) {
                try {
                    System.out.print("Masukkan Harga Menu: Rp ");
                    harga = Double.parseDouble(input.nextLine());
                    if (harga < 0) throw new NumberFormatException();
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("Harga harus berupa nilai angka positif!");
                }
            }

            String kategori = "";
            // Loop validasi penguncian teks kategori agar wajib "Makanan" atau "Minuman"
            while (true) {
                System.out.print("Masukkan Kategori (Makanan / Minuman): ");
                kategori = input.nextLine();
                if (kategori.equalsIgnoreCase("Makanan") || kategori.equalsIgnoreCase("Minuman")) {
                    // Standardisasi huruf kapital di awal kata agar rapi di struk
                    kategori = kategori.equalsIgnoreCase("Makanan") ? "Makanan" : "Minuman";
                    break;
                }
                System.out.println("Kategori tidak valid! Ketik secara tepat: Makanan atau Minuman.");
            }

            // Memasukkan data ke dalam ArrayList (ukuran array bertambah otomatis)
            daftarMenu.add(new Menu(nama, harga, kategori));
            System.out.println("-> Menu baru berhasil didaftarkan ke sistem.");

            System.out.print("Apakah ingin menambahkan menu baru lagi? (Ya/Tidak): ");
            String respon = input.nextLine();
            if (!respon.equalsIgnoreCase("Ya")) {
                lanjutTambah = false;
            }
        }
    }

    /**
     * METHOD: Ubah Harga Menu
     * Menampilkan daftar menu, menerima input nomor indeks menu, 
     * meminta konfirmasi tertulis ('Ya'), lalu mengubah data harga pada objek di dalam ArrayList.
     */
    static void ubahHargaMenu() {
        System.out.println("\n--- Fitur Ubah Harga Menu ---");
        tampilkanDaftarMenu();
        
        while (true) {
            System.out.print("Masukkan nomor urut menu yang harganya ingin diganti: ");
            try {
                int nomor = Integer.parseInt(input.nextLine());
                if (nomor < 1 || nomor > daftarMenu.size()) {
                    System.out.println("Nomor menu di luar jangkauan data restoran! Silakan ulangi.");
                    continue;
                }

                Menu menuDipilih = daftarMenu.get(nomor - 1);
                System.out.printf("Menu Terpilih: %s (Harga Sekarang: Rp %,.0f)\n", menuDipilih.nama, menuDipilih.harga);
                
                System.out.print("Masukkan besaran Harga Baru: Rp ");
                double hargaBaru = Double.parseDouble(input.nextLine());

                // Alur Konfirmasi Layar Sebelum Eksekusi Sesuai Perintah Soal
                System.out.printf("Apakah Anda yakin ingin mengubah harga %s menjadi Rp %,.0f? (Ya/Tidak): ", menuDipilih.nama, hargaBaru);
                String konfirmasi = input.nextLine();

                if (konfirmasi.equalsIgnoreCase("Ya")) {
                    menuDipilih.harga = hargaBaru; // Manipulasi/Update harga objek
                    System.out.println("-> Perubahan berhasil disimpan ke database memori.");
                } else {
                    System.out.println("-> Perubahan dibatalkan oleh Pemilik.");
                }
                break; // Keluar ke menu pengelolaan

            } catch (NumberFormatException e) {
                System.out.println("Input terdeteksi tidak valid! Mohon masukkan format angka.");
            }
        }
    }

    /**
     * METHOD: Hapus Menu Restoran
     * Menampilkan daftar item, meminta nomor pilihan, memunculkan konfirmasi eksplisit,
     * kemudian meremove data objek dari ArrayList menggunakan fungsi bawaan `.remove(index)`.
     */
    static void hapusMenuRestoran() {
        System.out.println("\n--- Fitur Hapus Menu Restoran ---");
        tampilkanDaftarMenu();

        while (true) {
            System.out.print("Masukkan nomor urut menu yang ingin dihapus permanen: ");
            try {
                int nomor = Integer.parseInt(input.nextLine());
                if (nomor < 1 || nomor > daftarMenu.size()) {
                    System.out.println("Nomor menu tidak ditemukan di list! Silakan ulangi.");
                    continue;
                }

                Menu menuDipilih = daftarMenu.get(nomor - 1);
                
                // Alur Konfirmasi Layar Sebelum Eksekusi Sesuai Perintah Soal
                System.out.printf("APAKAH ANDA YAKIN INGIN MENGHAPUS '%s' DARI RESTORAN? (Ya/Tidak): ", menuDipilih.nama);
                String konfirmasi = input.nextLine();

                if (konfirmasi.equalsIgnoreCase("Ya")) {
                    daftarMenu.remove(nomor - 1); // Menghapus dari ArrayList, indeks belakang otomatis maju
                    System.out.println("-> Menu telah berhasil dihapus dari daftar master.");
                } else {
                    System.out.println("-> Proses penghapusan dibatalkan.");
                }
                break; // Kembali ke menu pengelolaan

            } catch (NumberFormatException e) {
                System.out.println("Masukan salah! Masukkan dalam format angka indeks.");
            }
        }
    }
}