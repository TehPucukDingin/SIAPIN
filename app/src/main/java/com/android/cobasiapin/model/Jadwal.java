package com.android.cobasiapin.model;

public class Jadwal {
    private String id;
    private String uid;
    private String mataKuliah;
    private String hari;
    private String jamMulai;
    private String jamSelesai;
    private String ruangan;
    private String gedung;
    private String dosenPengampu;

    public Jadwal() {}

    // Getters & Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUid() { return uid; }
    public void setUid(String uid) { this.uid = uid; }

    public String getMataKuliah() { return mataKuliah; }
    public void setMataKuliah(String mataKuliah) { this.mataKuliah = mataKuliah; }

    public String getHari() { return hari; }
    public void setHari(String hari) { this.hari = hari; }

    public String getJamMulai() { return jamMulai; }
    public void setJamMulai(String jamMulai) { this.jamMulai = jamMulai; }

    public String getJamSelesai() { return jamSelesai; }
    public void setJamSelesai(String jamSelesai) { this.jamSelesai = jamSelesai; }

    public String getRuangan() { return ruangan; }
    public void setRuangan(String ruangan) { this.ruangan = ruangan; }

    public String getGedung() { return gedung; }
    public void setGedung(String gedung) { this.gedung = gedung; }

    public String getDosenPengampu() { return dosenPengampu; }
    public void setDosenPengampu(String dosenPengampu) { this.dosenPengampu = dosenPengampu; }
}