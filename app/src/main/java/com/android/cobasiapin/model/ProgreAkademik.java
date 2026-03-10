package com.android.cobasiapin.model;

import java.util.Map;

public class ProgreAkademik {
    private String uid;
    private double ipk;
    private int totalSks;
    private int sksSemesterIni;
    private Map<String, Double> ipPerSemester; // key: "Semester 1", value: 3.5

    public ProgreAkademik() {}

    // Getters & Setters
    public String getUid() { return uid; }
    public void setUid(String uid) { this.uid = uid; }

    public double getIpk() { return ipk; }
    public void setIpk(double ipk) { this.ipk = ipk; }

    public int getTotalSks() { return totalSks; }
    public void setTotalSks(int totalSks) { this.totalSks = totalSks; }

    public int getSksSemesterIni() { return sksSemesterIni; }
    public void setSksSemesterIni(int sksSemesterIni) { this.sksSemesterIni = sksSemesterIni; }

    public Map<String, Double> getIpPerSemester() { return ipPerSemester; }
    public void setIpPerSemester(Map<String, Double> ipPerSemester) {
        this.ipPerSemester = ipPerSemester;
    }
}