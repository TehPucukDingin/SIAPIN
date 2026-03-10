package com.android.cobasiapin.model;

public class Tugas {
    private String id;
    private String uid;
    private String mataKuliah;
    private String namaTugas;
    private String catatan;
    private long deadlineMillis;
    private boolean selesai;
    private String prioritas; // "normal", "darurat"

    public Tugas() {}

    // Getters & Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUid() { return uid; }
    public void setUid(String uid) { this.uid = uid; }

    public String getMataKuliah() { return mataKuliah; }
    public void setMataKuliah(String mataKuliah) { this.mataKuliah = mataKuliah; }

    public String getNamaTugas() { return namaTugas; }
    public void setNamaTugas(String namaTugas) { this.namaTugas = namaTugas; }

    public String getCatatan() { return catatan; }
    public void setCatatan(String catatan) { this.catatan = catatan; }

    public long getDeadlineMillis() { return deadlineMillis; }
    public void setDeadlineMillis(long deadlineMillis) { this.deadlineMillis = deadlineMillis; }

    public boolean isSelesai() { return selesai; }
    public void setSelesai(boolean selesai) { this.selesai = selesai; }

    public String getPrioritas() { return prioritas; }
    public void setPrioritas(String prioritas) { this.prioritas = prioritas; }
}