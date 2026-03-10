package com.android.cobasiapin.model;

public class ChatMessage {
    private String id;
    private String senderId;
    private String senderNama;
    private String pesan;
    private long timestamp;
    private String tipe; // "text"

    public ChatMessage() {}

    // Getters & Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getSenderId() { return senderId; }
    public void setSenderId(String senderId) { this.senderId = senderId; }

    public String getSenderNama() { return senderNama; }
    public void setSenderNama(String senderNama) { this.senderNama = senderNama; }

    public String getPesan() { return pesan; }
    public void setPesan(String pesan) { this.pesan = pesan; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    public String getTipe() { return tipe; }
    public void setTipe(String tipe) { this.tipe = tipe; }
}