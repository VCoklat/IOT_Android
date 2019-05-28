package com.example.myapplication;

public class Data {

    private String durasi;
    private String biaya;
    private String jam;

    public Data(String durasi, String biaya, String jam) {
        this.durasi = durasi;
        this.biaya = biaya;
        this.jam = jam;

    }

    public String getDurasi() {
        return durasi;
    }


    public String getBiaya() {
        return biaya;
    }

    public String getJam() {
        return jam;
    }


}