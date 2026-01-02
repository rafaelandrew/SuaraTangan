package com.example.utspab;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private final List<Notification> notificationList;

    public NotificationAdapter(List<Notification> notificationList) {
        this.notificationList = notificationList;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        Notification notification = notificationList.get(position);
        holder.bind(notification);
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    class NotificationViewHolder extends RecyclerView.ViewHolder {
        private final ImageView ivIcon;
        private final TextView tvTitle;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            ivIcon = itemView.findViewById(R.id.iv_notification_icon);
            tvTitle = itemView.findViewById(R.id.tv_notification_title);
        }

        public void bind(final Notification notification) {
            tvTitle.setText(notification.getTitle());
            ivIcon.setImageResource(notification.getIconResId());

            itemView.setOnClickListener(v -> {
                Context context = v.getContext();
                String title = notification.getTitle();
                String content = getNotificationContent(title);

                Intent intent = new Intent(context, NotificationDetailActivity.class);
                intent.putExtra("NOTIFICATION_TITLE", title);
                intent.putExtra("NOTIFICATION_CONTENT", content);
                context.startActivity(intent);
            });
        }

    }

    private String getNotificationContent(String title) {
        if (title != null && title.startsWith("Feedback Anda:")) {
            return title.substring(14);
        }

        switch (title) {

            case "Fitur Baru":
                return "Kami dengan bangga menghadirkan pembaruan signifikan yang dirancang khusus untuk meningkatkan pengalaman Anda dalam berkomunikasi.<br><br>" +

                        "<b>Penambahan Utama:</b><br><br>" +

                        "<ul>" +
                        "<li><b>Mode Percakapan Real-Time</b><br>" +
                        "Komunikasi dua arah dengan penerjemahan otomatis yang lebih responsif untuk interaksi yang lebih natural.</li>" +

                        "<li><b>Kamus BISINDO Diperluas</b><br>" +
                        "Penambahan lebih dari 500 kosakata baru termasuk istilah teknologi dan percakapan sehari-hari.</li>" +

                        "<li><b>Deteksi Gesture Lebih Akurat</b><br>" +
                        "Peningkatan akurasi hingga 25% berkat pengembangan teknologi Machine Learning.</li>" +

                        "<li><b>Tutorial Interaktif</b><br>" +
                        "Panduan langkah demi langkah bagi pengguna baru untuk memahami fitur utama aplikasi.</li>" +
                        "</ul>" +

                        "<br>Pembaruan ini merupakan hasil dari masukan ribuan pengguna. Terima kasih atas dukungan Anda!";

            case "Update Aplikasi":
                return "Versi terbaru aplikasi telah tersedia dengan fokus pada performa dan stabilitas sistem.<br><br>" +

                        "<b>Peningkatan Utama:</b><br><br>" +

                        "<ul>" +
                        "<li><b>Kecepatan Terjemahan</b><br>Proses terjemahan kini 30% lebih cepat dan lebih hemat baterai.</li>" +
                        "<li><b>Mode Offline</b><br>Paket bahasa lebih kecil dan stabil tanpa mengurangi kualitas.</li>" +
                        "<li><b>Antarmuka Baru</b><br>Tampilan menu lebih intuitif dan mudah dijangkau.</li>" +
                        "<li><b>Keamanan Data</b><br>Enkripsi end-to-end dan perbaikan bug penting.</li>" +
                        "</ul>" +

                        "<br>Perbarui aplikasi untuk pengalaman yang lebih lancar dan aman.";

            case "Tips & Trik":
                return "Gunakan aplikasi Suara Tangan secara maksimal dengan tips berikut:<br><br>" +

                        "<ul>" +
                        "<li><b>Pintasan Cepat</b><br>Akses penerjemah langsung dari layar kunci.</li>" +
                        "<li><b>Hemat Baterai</b><br>Gunakan mode offline dan hemat daya.</li>" +
                        "<li><b>Personalisasi</b><br>Atur ukuran font dan aktifkan Dark Mode.</li>" +
                        "<li><b>Riwayat Terjemahan</b><br>Akses kembali hasil terjemahan sebelumnya.</li>" +
                        "<li><b>Kamera untuk Teks</b><br>Terjemahkan teks dari gambar secara instan.</li>" +
                        "</ul>";

            case "Event Komunitas":
                return "Jadilah bagian dari komunitas pembelajar Bahasa Isyarat Indonesia.<br><br>" +

                        "<ul>" +
                        "<li><b>Webinar Ahli</b><br>Sesi diskusi rutin dengan pakar dan aktivis.</li>" +
                        "<li><b>Kompetisi</b><br>Tantangan kreatif dengan hadiah menarik.</li>" +
                        "<li><b>User Showcase</b><br>Kisah inspiratif dari pengguna lain.</li>" +
                        "<li><b>Beta Testing</b><br>Coba fitur baru sebelum rilis publik.</li>" +
                        "</ul>";

            case "Kamus Diperbarui":
                return "Kamus BISINDO terus diperbarui mengikuti perkembangan bahasa.<br><br>" +

                        "<ul>" +
                        "<li><b>Kosakata Terkini</b><br>Istilah populer dari teknologi hingga hiburan.</li>" +
                        "<li><b>Istilah Profesional</b><br>Kosakata khusus bidang medis, hukum, dan pendidikan.</li>" +
                        "<li><b>Ungkapan Budaya</b><br>Idiom khas Indonesia dengan konteks penggunaan.</li>" +
                        "<li><b>Verifikasi Ahli</b><br>Ditinjau oleh tim bahasa isyarat profesional.</li>" +
                        "</ul>";

            case "Feedback Anda":
                return "Kami percaya aplikasi terbaik dibangun bersama penggunanya.<br><br>" +
                        "Melalui menu Feedback, kamu dapat menyampaikan pengalaman, kebingungan, atau harapan terhadap fitur baru. " +
                        "Setiap saran yang kamu kirimkan akan dibaca langsung oleh tim pengembangan dan menjadi bahan utama dalam menentukan arah pembaruan selanjutnya.";

            case "Promo Spesial":
                return "Promo Spesial adalah bentuk apresiasi kami kepada pengguna setia.<br><br>" +

                        "<ul>" +
                        "<li><b>Diskon Berlangganan</b><br>Potongan hingga 50% untuk paket bulanan atau tahunan.</li>" +
                        "<li><b>Paket Pelajar & Pengajar</b><br>Harga spesial dengan verifikasi akademik.</li>" +
                        "<li><b>Flash Sale & Free Trial</b><br>Akses gratis ke fitur premium dalam waktu terbatas.</li>" +
                        "<li><b>Bundling Partner</b><br>Penawaran menarik dari mitra pilihan kami.</li>" +
                        "</ul>";

            case "Info Pemeliharaan":
                return "Untuk menjaga performa dan keamanan aplikasi, kami melakukan pemeliharaan rutin.<br><br>" +

                        "<ul>" +
                        "<li><b>Peningkatan Server</b><br>Menjaga kecepatan akses saat trafik tinggi.</li>" +
                        "<li><b>Keamanan Sistem</b><br>Menutup celah kerentanan data dan privasi.</li>" +
                        "<li><b>Persiapan Fitur Baru</b><br>Implementasi teknis agar fitur baru berjalan lancar.</li>" +
                        "<li><b>Transparansi Jadwal</b><br>Maintenance diumumkan lebih awal di waktu sepi.</li>" +
                        "</ul>";

            case "Kuis Mingguan":
                return "Belajar Bahasa Isyarat dengan cara menyenangkan melalui Kuis Mingguan.<br><br>" +

                        "<ul>" +
                        "<li><b>Kurikulum Berbasis Tren</b><br>Pertanyaan mengikuti kosakata dan fitur terbaru.</li>" +
                        "<li><b>Gamifikasi</b><br>Dapatkan XP dan badge dari setiap jawaban benar.</li>" +
                        "<li><b>Leaderboard Nasional</b><br>Bersaing dengan pengguna di seluruh Indonesia.</li>" +
                        "<li><b>Penukaran Hadiah</b><br>Poin dapat ditukar dengan berbagai keuntungan.</li>" +
                        "</ul>";

            default:
                return "Informasi untuk topik ini akan segera tersedia. Pantau terus notifikasi Anda untuk pembaruan terbaru.";
        }
    }
}