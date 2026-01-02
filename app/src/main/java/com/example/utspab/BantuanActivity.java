package com.example.utspab;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class BantuanActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bantuan);

        ImageView ivBack = findViewById(R.id.iv_back_bantuan);
        Button btnLapor = findViewById(R.id.btn_lapor_masalah);
        Button btnPusat = findViewById(R.id.btn_pusat_bantuan);
        Button btnPrivasi = findViewById(R.id.btn_privasi);
        Button btnPanduan = findViewById(R.id.btn_panduan);

        ivBack.setOnClickListener(v -> finish());

        btnLapor.setOnClickListener(v ->
                startActivity(new Intent(this, LaporanMasalahActivity.class))
        );

        String faqContent =
                "<b>Q: Apa fungsi utama aplikasi Suara Tangan?</b><br>" +
                        "A: Aplikasi ini berfungsi sebagai jembatan komunikasi real-time antara Teman Tuli dan Teman Dengar menggunakan teknologi Machine Learning berbasis BISINDO (Bahasa Isyarat Indonesia).<br><br>" +

                        "<b>Q: Bagaimana cara menggunakan fitur Kamera?</b><br>" +
                        "A: Fitur kamera menangkap gerakan tangan (BISINDO) secara langsung dan menerjemahkannya menjadi teks di layar.<br><br>" +

                        "<b>Q: Apakah aplikasi ini berbayar?</b><br>" +
                        "A: Tidak, aplikasi ini gratis untuk mendukung inklusi sosial di Indonesia.<br><br>" +

                        "<b>Q: Mengapa saya harus memilih peran saat daftar?</b><br>" +
                        "A: Identifikasi peran (Teman Tuli/Dengar) membantu kami menyesuaikan pengalaman penggunaan yang paling sesuai untuk Anda.";

        btnPusat.setOnClickListener(v -> openDetail("Pusat Bantuan", faqContent));

        String privacyContent =
                "<b>1. Penggunaan Kamera & Mikrofon</b><br>" +
                        "Kami hanya mengakses kamera dan mikrofon saat Anda menggunakan fitur penerjemah. Data visual dan audio diproses secara real-time dan <b>tidak disimpan</b> di server kami.<br><br>" +

                        "<b>2. Keamanan Data Akun</b><br>" +
                        "Informasi pribadi seperti Nama, Email, dan Nomor Telepon dilindungi dengan enkripsi standar industri.<br><br>" +

                        "<b>3. Transparansi Data</b><br>" +
                        "Data laporan masalah yang Anda kirimkan hanya digunakan oleh tim pengembang untuk perbaikan aplikasi (bug fixing).";

        btnPrivasi.setOnClickListener(v -> openDetail("Privasi & Keamanan", privacyContent));

        String guideContent =
                "<b>A. Fitur Kamera (Gesture to Text)</b><br>" +
                        "1. Buka menu utama dan pilih ikon <b>Kamera</b>.<br>" +
                        "2. Arahkan kamera ke lawan bicara yang menggunakan bahasa isyarat.<br>" +
                        "3. Terjemahan teks akan muncul otomatis di layar.<br><br>" +

                        "<b>B. Fitur Suara (Speech to Text)</b><br>" +
                        "1. Tekan ikon <b>Mikrofon</b> besar di tengah layar.<br>" +
                        "2. Ucapkan kalimat dalam Bahasa Indonesia.<br>" +
                        "3. Aplikasi akan mengubah suara Anda menjadi teks agar bisa dibaca oleh Teman Tuli.<br><br>" +

                        "<b>C. Fitur Notifikasi</b><br>" +
                        "Cek ikon <b>Lonceng</b> di menu bawah untuk melihat info terbaru seputar komunitas dan update aplikasi.";

        btnPanduan.setOnClickListener(v -> openDetail("Buku Panduan", guideContent));
    }

    private void openDetail(String title, String content) {
        Intent intent = new Intent(this, DetailBantuanActivity.class);
        intent.putExtra("TITLE", title);
        intent.putExtra("CONTENT", content);
        startActivity(intent);
    }
}