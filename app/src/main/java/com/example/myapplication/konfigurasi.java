package com.example.myapplication;


public class konfigurasi {

    //Dibawah ini merupakan Pengalamatan dimana Lokasi Skrip CRUD PHP disimpan
    //Pada tutorial Kali ini, karena kita membuat localhost maka alamatnya tertuju ke IP komputer
    //dimana File PHP tersebut berada
    //PENTING! JANGAN LUPA GANTI IP SESUAI DENGAN IP KOMPUTER DIMANA DATA PHP BERADA
    public static final String URL_GET_ALL = "http://skripsiyohanes.000webhostapp.com/list.php";
    public static final String URL_RESET = "http://skripsiyohanes.000webhostapp.com/reset.php";
    public static final String URL_CHART = "http://skripsiyohanes.000webhostapp.com/chart.php";

    //Dibawah ini merupakan Kunci yang akan digunakan untuk mengirim permintaan ke Skrip PHP

    //JSON Tags
    public static final String TAG_JSON_ARRAY="result";
    public static final String TAG_ID = "durasi";
    public static final String TAG_TERAKHIR = "terakhir";
    public static final String TAG_STATUS = "status";
    public static final String TAG_JAM = "jam";

    //ID karyawan
    //emp itu singkatan dari Employee
    public static final String EMP_ID = "emp_id";
}
