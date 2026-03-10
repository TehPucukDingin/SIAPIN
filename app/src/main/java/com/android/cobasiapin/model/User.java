package com.android.cobasiapin.model;

public class User {
    private String uid;
    private String nama;
    private String nim;
    private String email;
    private String programStudi;
    private String role; // "mahasiswa", "pengurus_kelas", "admin"
    private String fotoUrl;

    public User() {}

    public User(String uid, String nama, String nim, String email,
                String programStudi, String role) {
        this.uid = uid;
        this.nama = nama;
        this.nim = nim;
        this.email = email;
        this.programStudi = programStudi;
        this.role = role;
        this.fotoUrl = "";
    }

    // Getters & Setters
    public String getUid() { return uid; }
    public void setUid(String uid) { this.uid = uid; }

    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }

    public String getNim() { return nim; }
    public void setNim(String nim) { this.nim = nim; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getProgramStudi() { return programStudi; }
    public void setProgramStudi(String programStudi) { this.programStudi = programStudi; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getFotoUrl() { return fotoUrl; }
    public void setFotoUrl(String fotoUrl) { this.fotoUrl = fotoUrl; }
}