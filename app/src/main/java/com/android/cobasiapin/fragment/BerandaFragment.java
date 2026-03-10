package com.android.cobasiapin.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.cobasiapin.R;
import com.android.cobasiapin.model.Jadwal;
import com.android.cobasiapin.utils.FirebaseHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BerandaFragment extends Fragment {

    private TextView tvTanggal, tvSapaan, tvTugasTersisa;
    private TextView tvTugasSelesai, tvProgressPersen, tvNoJadwal;
    private LinearLayout containerJadwal;
    private CircularProgressBar circularProgressBar;

    // Nama hari dalam Bahasa Indonesia
    private final String[] NAMA_HARI = {
            "Minggu", "Senin", "Selasa", "Rabu", "Kamis", "Jum'at", "Sabtu"
    };
    private final String[] NAMA_BULAN = {
            "Januari", "Februari", "Maret", "April", "Mei", "Juni",
            "Juli", "Agustus", "September", "Oktober", "November", "Desember"
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_beranda, container, false);

        initViews(view);
        setTanggalHariIni();
        loadJadwalHariIni();
        loadProgressTugas();
        setPintasanListeners(view);

        return view;
    }

    private void initViews(View view) {
        tvTanggal = view.findViewById(R.id.tvTanggal);
        tvSapaan = view.findViewById(R.id.tvSapaan);
        tvTugasTersisa = view.findViewById(R.id.tvTugasTersisa);
        tvTugasSelesai = view.findViewById(R.id.tvTugasSelesai);
        tvProgressPersen = view.findViewById(R.id.tvProgressPersen);
        tvNoJadwal = view.findViewById(R.id.tvNoJadwal);
        containerJadwal = view.findViewById(R.id.containerJadwal);
        circularProgressBar = view.findViewById(R.id.circularProgressBar);
    }

    private void setTanggalHariIni() {
        java.util.Calendar cal = java.util.Calendar.getInstance();
        int hari = cal.get(java.util.Calendar.DAY_OF_WEEK) - 1;
        int tanggal = cal.get(java.util.Calendar.DAY_OF_MONTH);
        int bulan = cal.get(java.util.Calendar.MONTH);
        int tahun = cal.get(java.util.Calendar.YEAR);

        String tanggalStr = NAMA_HARI[hari] + ", " + tanggal + " "
                + NAMA_BULAN[bulan] + " " + tahun;
        tvTanggal.setText(tanggalStr);
    }

    private void loadJadwalHariIni() {
        String uid = FirebaseHelper.getCurrentUid();
        if (uid == null) return;

        // Ambil nama hari ini
        java.util.Calendar cal = java.util.Calendar.getInstance();
        int hariIndex = cal.get(java.util.Calendar.DAY_OF_WEEK) - 1;
        String hariIni = NAMA_HARI[hariIndex];

        FirebaseHelper.getDatabaseRef("jadwal/" + uid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (!isAdded()) return;

                        containerJadwal.removeAllViews();
                        List<Jadwal> jadwalHariIni = new ArrayList<>();

                        for (DataSnapshot snap : snapshot.getChildren()) {
                            Jadwal jadwal = snap.getValue(Jadwal.class);
                            if (jadwal != null && jadwal.getHari() != null
                                    && jadwal.getHari().equals(hariIni)) {
                                jadwalHariIni.add(jadwal);
                            }
                        }

                        if (jadwalHariIni.isEmpty()) {
                            tvNoJadwal.setVisibility(View.VISIBLE);
                        } else {
                            tvNoJadwal.setVisibility(View.GONE);
                            for (Jadwal jadwal : jadwalHariIni) {
                                addJadwalItem(jadwal);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        if (!isAdded()) return;
                        Toast.makeText(getContext(),
                                "Gagal memuat jadwal",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void addJadwalItem(Jadwal jadwal) {
        View itemView = LayoutInflater.from(getContext())
                .inflate(R.layout.item_jadwal_beranda, containerJadwal, false);

        TextView tvJam = itemView.findViewById(R.id.tvJam);
        TextView tvPagiSore = itemView.findViewById(R.id.tvPagiSore);
        TextView tvNamaMK = itemView.findViewById(R.id.tvNamaMK);
        TextView tvKodeMK = itemView.findViewById(R.id.tvKodeMK);
        TextView tvGedung = itemView.findViewById(R.id.tvGedung);

        // Set jam
        if (jadwal.getJamMulai() != null) {
            String[] jamParts = jadwal.getJamMulai().split(":");
            if (jamParts.length >= 2) {
                int jam = Integer.parseInt(jamParts[0]);
                tvJam.setText(jadwal.getJamMulai());
                tvPagiSore.setText(jam < 12 ? "Pagi" : jam < 17 ? "Siang" : "Sore");
            }
        }

        tvNamaMK.setText(jadwal.getMataKuliah() != null
                ? jadwal.getMataKuliah() : "-");
        tvKodeMK.setText(jadwal.getRuangan() != null
                ? jadwal.getRuangan() : "-");
        tvGedung.setText(jadwal.getGedung() != null
                ? jadwal.getGedung() : "-");

        containerJadwal.addView(itemView);
    }

    private void loadProgressTugas() {
        String uid = FirebaseHelper.getCurrentUid();
        if (uid == null) return;

        FirebaseHelper.getDatabaseRef("tugas/" + uid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (!isAdded()) return;

                        int total = 0, selesai = 0;
                        for (DataSnapshot snap : snapshot.getChildren()) {
                            total++;
                            Boolean isSelesai = snap.child("selesai")
                                    .getValue(Boolean.class);
                            if (Boolean.TRUE.equals(isSelesai)) selesai++;
                        }

                        int tersisa = total - selesai;
                        float progress = total > 0
                                ? (selesai * 100f / total) : 0;

                        circularProgressBar.setProgressWithAnimation(progress, 1000L);
                        tvProgressPersen.setText((int) progress + "%");
                        tvTugasTersisa.setText(tersisa + " Tugas tersisa");
                        tvTugasSelesai.setText(selesai
                                + " Tugas telah dikumpulkan");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });
    }

    private void setPintasanListeners(View view) {
        view.findViewById(R.id.chipTugas).setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity())
                        .bottomNavigation
                        .setSelectedItemId(R.id.nav_kelola);
            }
        });

        view.findViewById(R.id.chipJadwal).setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity())
                        .bottomNavigation
                        .setSelectedItemId(R.id.nav_kelola);
            }
        });

        view.findViewById(R.id.chipAI).setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity())
                        .bottomNavigation
                        .setSelectedItemId(R.id.nav_ai);
            }
        });
    }
}