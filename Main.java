import java.util.ArrayList;
import java.util.Scanner;


public class Main {
    static ArrayList<Menu> daftarMenu = new ArrayList<>();
    static Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        // Inisialisasi data awal (4 makanan dan 4 minuman)
        inisialisasiMenuAwal();

        boolean berjalan = true;
        while (berjalan) {
            System.out.println("\n=============================================");
            System.out.println("          SISTEM APLIKASI RESTORAN           ");
            System.out.println("=============================================");
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

    static void inisialisasiMenuAwal() {
        daftarMenu.add(new Menu("Nasi Goreng Spesial", 25000, "Makanan"));
        daftarMenu.add(new Menu("Mie Ayam Pangsit", 18000, "Makanan"));
        daftarMenu.add(new Menu("Ayam Bakar Taliwang", 30000, "Makanan"));
        daftarMenu.add(new Menu("Sate Ayam Madura", 22000, "Makanan"));

        daftarMenu.add(new Menu("Es Teh Manis", 5000, "Minuman"));
        daftarMenu.add(new Menu("Jus Alpukat Kocok", 15000, "Minuman"));
        daftarMenu.add(new Menu("Es Jeruk Peras", 7000, "Minuman"));
        daftarMenu.add(new Menu("Kopi Susu Gula Aren", 12000, "Minuman"));
    }

    /**
     * METHOD: Menampilkan Daftar Menu
     * Layout dirapikan dengan pembatasan lebar tabel agar rapi di monitor.
     */
    static void tampilkanDaftarMenu() {
        System.out.println("\n=============================================");
        System.out.println("            DAFTAR MENU RESTORAN             ");
        System.out.println("=============================================");
        
        System.out.println("[ KATEGORI: MAKANAN ]");
        for (int i = 0; i < daftarMenu.size(); i++) {
            if (daftarMenu.get(i).kategori.equalsIgnoreCase("Makanan")) {
                System.out.printf("%2d. %-26s | Rp %,.0f\n", (i + 1), daftarMenu.get(i).nama, daftarMenu.get(i).harga);
            }
        }

        System.out.println("\n[ KATEGORI: MINUMAN ]");
        for (int i = 0; i < daftarMenu.size(); i++) {
            if (daftarMenu.get(i).kategori.equalsIgnoreCase("Minuman")) {
                System.out.printf("%2d. %-26s | Rp %,.0f\n", (i + 1), daftarMenu.get(i).nama, daftarMenu.get(i).harga);
            }
        }
        System.out.println("=============================================");
    }

    static void menuPelanggan() {
        ArrayList<Menu> pesananItem = new ArrayList<>();
        ArrayList<Integer> pesananJumlah = new ArrayList<>();

        while (true) {
            tampilkanDaftarMenu();
            System.out.println("Ketik 'Selesai' atau 'selesai' jika sudah cukup memesan.");
            System.out.print("Masukkan nomor menu yang ingin dipesan: ");
            String inputUser = input.nextLine().trim();

            if (inputUser.equalsIgnoreCase("selesai") || inputUser.equalsIgnoreCase("Selesai")) {
                break;
            }

            try {
                int nomorMenu = Integer.parseInt(inputUser);
                if (nomorMenu < 1 || nomorMenu > daftarMenu.size()) {
                    System.out.println("Nomor menu tidak terdaftar! Silakan ulangi.");
                    continue;
                }

                System.out.print("Masukkan jumlah porsi/gelas: ");
                int jumlah = Integer.parseInt(input.nextLine());
                if (jumlah <= 0) {
                    System.out.println("Jumlah pesanan minimal 1! Pilihan dibatalkan.");
                    continue;
                }

                pesananItem.add(daftarMenu.get(nomorMenu - 1));
                pesananJumlah.add(jumlah);
                System.out.println("-> Berhasil menambahkan ke daftar pesanan.");

            } catch (NumberFormatException e) {
                System.out.println("Input tidak valid! Masukkan angka nomor menu atau ketik 'selesai'.");
            }
        }

        if (!pesananItem.isEmpty()) {
            prosesTransaksi(pesananItem, pesananJumlah);
        } else {
            System.out.println("Anda tidak memesan apapun. Kembali ke menu utama.");
        }
    }

    /**
     * METHOD: Proses Transaksi & Cetak Struk (Perbaikan Layout Kompleks)
     * Mengatur agar nama menu panjang tidak merusak struktur layout struk dengan lebar 45 karakter.
     */
    static void prosesTransaksi(ArrayList<Menu> items, ArrayList<Integer> jumlahs) {
        double subtotalBelanja = 0;
        
        for (int i = 0; i < items.size(); i++) {
            subtotalBelanja += items.get(i).harga * jumlahs.get(i);
        }

        // Evaluasi Promo Beli 1 Gratis 1 Kategori Minuman (> Rp 50.000)
        double totalPotonganB1G1 = 0;
        ArrayList<String> logPromoB1G1 = new ArrayList<>();
        if (subtotalBelanja > 50000) {
            for (int i = 0; i < items.size(); i++) {
                if (items.get(i).kategori.equalsIgnoreCase("Minuman")) {
                    int jumlahBeli = jumlahs.get(i);
                    int itemGratis = jumlahBeli / 2;
                    if (itemGratis > 0) {
                        double potongan = itemGratis * items.get(i).harga;
                        totalPotonganB1G1 += potongan;
                        logPromoB1G1.add(String.format(" * B1G1 %s (%dx)", items.get(i).nama, itemGratis));
                        logPromoB1G1.add(String.format("  -Rp %,.0f", potongan));
                    }
                }
            }
        }

        // Evaluasi Diskon 10% (> Rp 100.000)
        double diskonUmum = 0;
        if (subtotalBelanja > 100000) {
            diskonUmum = 0.10 * subtotalBelanja;
        }

        double subtotalSetelahDiskon = subtotalBelanja - totalPotonganB1G1 - diskonUmum;
        double pajak = 0.10 * subtotalSetelahDiskon;
        double biayaPelayanan = 20000;
        double totalAkhirBayar = subtotalSetelahDiskon + pajak + biayaPelayanan;

        // CETAK STRUK PEMBAYARAN 
        System.out.println("\n=============================================");
        System.out.println("                STRUK PESANAN                ");
        System.out.println("=============================================");
        
        for (int i = 0; i < items.size(); i++) {
            String namaMenu = items.get(i).nama;
            int qty = jumlahs.get(i);
            double totalPerItem = items.get(i).harga * qty;
            String infoQtyHarga = String.format("x%d ", qty);
            String infoTotalItem = String.format("Rp %,.0f", totalPerItem);

            if (namaMenu.length() > 22) {
                System.out.printf("%-22s\n", namaMenu.substring(0, 22));
                System.out.printf("%-22s %5s %15s\n", " " + namaMenu.substring(22), infoQtyHarga, infoTotalItem);
            } else {
                System.out.printf("%-22s %5s %15s\n", namaMenu, infoQtyHarga, infoTotalItem);
            }
        }
        
        System.out.println("---------------------------------------------");
        System.out.printf("%-28s : Rp %,11.0f\n", "Total Biaya Item", subtotalBelanja);
        
        if (totalPotonganB1G1 > 0) {
            for (String info : logPromoB1G1) {
                System.out.println(info);
            }
        }
        
        if (diskonUmum > 0) {
            String labelDiskon = " * Diskon Khusus 10%";
            System.out.printf("%-28s : -Rp %,10.0f\n", labelDiskon, diskonUmum);
        }
        
        if (totalPotonganB1G1 > 0 || diskonUmum > 0) {
            System.out.printf("%-28s : Rp %,11.0f\n", "Total Setelah Diskon", subtotalSetelahDiskon);
        }
        
        String labelPajak = "Pajak Restoran (10%)";
        System.out.printf("%-28s : Rp %,11.0f\n", labelPajak, pajak);
        System.out.printf("%-28s : Rp %,11.0f\n", "Biaya Pelayanan", biayaPelayanan);
        System.out.println("---------------------------------------------");
        System.out.printf("%-28s : Rp %,11.0f\n", "TOTAL AKHIR TAGIHAN", totalAkhirBayar);
        System.out.println("=============================================");
    }

    static void menuPemilik() {
        boolean diMenuAdmin = true;
        while (diMenuAdmin) {
            System.out.println("\n=============================================");
            System.out.println("       MANAJEMEN PENGELOLAAN RESTORAN        ");
            System.out.println("=============================================");
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
                    diMenuAdmin = false;
                    break;
                default:
                    System.out.println("Input salah! Silakan masukkan pilihan nomor 1 sampai 4.");
            }
        }
    }

    /**
     * PERBAIKAN 1A: Tambah Menu Baru (Dengan Fitur Cancel)
     */
    static void tambahMenuBaru() {
        boolean lanjutTambah = true;
        while (lanjutTambah) {
            System.out.println("\n--- Fitur Tambah Menu Baru (Ketik 'cancel' untuk batal) ---");
            System.out.print("Masukkan Nama Menu Baru: ");
            String nama = input.nextLine().trim();
            if (nama.equalsIgnoreCase("cancel")) {
                System.out.println("-> Pengisian dibatalkan. Kembali ke menu pengelolaan.");
                break;
            }

            double harga = 0;
            boolean hargaBatal = false;
            while (true) {
                System.out.print("Masukkan Harga Menu: Rp ");
                String inputHarga = input.nextLine().trim();
                if (inputHarga.equalsIgnoreCase("cancel")) {
                    hargaBatal = true;
                    break;
                }
                try {
                    harga = Double.parseDouble(inputHarga);
                    if (harga < 0) throw new NumberFormatException();
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("Harga harus berupa nilai angka positif!");
                }
            }
            if (hargaBatal) {
                System.out.println("-> Pengisian dibatalkan. Kembali ke menu pengelolaan.");
                break;
            }

            String kategori = "";
            boolean kategoriBatal = false;
            while (true) {
                System.out.print("Masukkan Kategori (Makanan / Minuman): ");
                kategori = input.nextLine().trim();
                if (kategori.equalsIgnoreCase("cancel")) {
                    kategoriBatal = true;
                    break;
                }
                if (kategori.equalsIgnoreCase("Makanan") || kategori.equalsIgnoreCase("Minuman")) {
                    kategori = kategori.equalsIgnoreCase("Makanan") ? "Makanan" : "Minuman";
                    break;
                }
                System.out.println("Kategori tidak valid! Ketik secara tepat: Makanan atau Minuman.");
            }
            if (kategoriBatal) {
                System.out.println("-> Pengisian dibatalkan. Kembali ke menu pengelolaan.");
                break;
            }

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
     * PERBAIKAN 1B: Ubah Harga Menu (Dengan Fitur Cancel angka '0')
     */
    static void ubahHargaMenu() {
        System.out.println("\n--- Fitur Ubah Harga Menu ---");
        tampilkanDaftarMenu();
        
        while (true) {
            System.out.print("Masukkan nomor urut menu (atau ketik '0' untuk cancel): ");
            String inputUser = input.nextLine().trim();
            if (inputUser.equals("0") || inputUser.equalsIgnoreCase("cancel")) {
                System.out.println("-> Aksi dibatalkan.");
                break;
            }

            try {
                int nomor = Integer.parseInt(inputUser);
                if (nomor < 1 || nomor > daftarMenu.size()) {
                    System.out.println("Nomor menu di luar jangkauan data restoran! Silakan ulangi.");
                    continue;
                }

                Menu menuDipilih = daftarMenu.get(nomor - 1);
                System.out.printf("Menu Terpilih: %s (Harga Sekarang: Rp %,.0f)\n", menuDipilih.nama, menuDipilih.harga);
                
                System.out.print("Masukkan besaran Harga Baru (atau ketik '0' untuk cancel): Rp ");
                String inputHargaBaru = input.nextLine().trim();
                if (inputHargaBaru.equals("0") || inputHargaBaru.equalsIgnoreCase("cancel")) {
                    System.out.println("-> Aksi dibatalkan.");
                    break;
                }
                
                double hargaBaru = Double.parseDouble(inputHargaBaru);

                System.out.printf("Apakah Anda yakin ingin mengubah harga %s menjadi Rp %,.0f? (Ya/Tidak): ", menuDipilih.nama, hargaBaru);
                String konfirmasi = input.nextLine();

                if (konfirmasi.equalsIgnoreCase("Ya")) {
                    menuDipilih.harga = hargaBaru;
                    System.out.println("-> Perubahan berhasil disimpan ke database memori.");
                } else {
                    System.out.println("-> Perubahan dibatalkan oleh Pemilik.");
                }
                break;

            } catch (NumberFormatException e) {
                System.out.println("Input terdeteksi tidak valid! Mohon masukkan format angka.");
            }
        }
    }

    /**
     * PERBAIKAN 1C: Hapus Menu Restoran (Dengan Fitur Cancel angka '0')
     */
    static void hapusMenuRestoran() {
        System.out.println("\n--- Fitur Hapus Menu Restoran ---");
        tampilkanDaftarMenu();

        while (true) {
            System.out.print("Masukkan nomor urut menu yang ingin dihapus (atau ketik '0' untuk cancel): ");
            String inputUser = input.nextLine().trim();
            if (inputUser.equals("0") || inputUser.equalsIgnoreCase("cancel")) {
                System.out.println("-> Aksi pembatalan berhasil.");
                break;
            }

            try {
                int nomor = Integer.parseInt(inputUser);
                if (nomor < 1 || nomor > daftarMenu.size()) {
                    System.out.println("Nomor menu tidak ditemukan di list! Silakan ulangi.");
                    continue;
                }

                Menu menuDipilih = daftarMenu.get(nomor - 1);
                
                System.out.printf("APAKAH ANDA YAKIN INGIN MENGHAPUS '%s' DARI RESTORAN? (Ya/Tidak): ", menuDipilih.nama);
                String konfirmasi = input.nextLine();

                if (konfirmasi.equalsIgnoreCase("Ya")) {
                    daftarMenu.remove(nomor - 1);
                    System.out.println("-> Menu telah berhasil dihapus dari daftar master.");
                } else {
                    System.out.println("-> Proses penghapusan dibatalkan.");
                }
                break;

            } catch (NumberFormatException e) {
                System.out.println("Masukan salah! Masukkan dalam format angka indeks.");
            }
        }
    }
}