/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gui.programinventaris;

/**
 *
 * @author Ichinomiya Mori
 */
public class PeminjamanModel {
    private String nama, namaBarang, noTelp;
    private int idPeminjaman, idbarang, jumlah;
    
    public PeminjamanModel(){
        
    }
    public PeminjamanModel(int idPeminjaman, String nama, String noTelp, int idbarang, String namaBarang, int jumlah){
        this.idPeminjaman = idPeminjaman;
        this.idbarang = idbarang;
        this.namaBarang = namaBarang;
        this.nama = nama;
        this.noTelp = noTelp;
        this.jumlah = jumlah;
    }
    
    public int getIdPeminjaman() {
        return this.idPeminjaman;
    }

    public void setIdPeminjaman(int idPeminjaman) {
        this.idbarang = idPeminjaman;
    }
    
    public int getIdBarang() {
        return this.idbarang;
    }

    public void setIdBarang(int idbarang) {
        this.idbarang = idbarang;
    }
    
    public String getNama() {
        return this.nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }
    
    public String getNamaBarang() {
        return this.namaBarang;
    }

    public void setNamaBarang(String namaBarang) {
        this.namaBarang = namaBarang;
    }
    
    public String getNoTelp() {
        return this.noTelp;
    }

    public void setNoTelp(String noTelp) {
        this.noTelp = noTelp;
    }
    
    public int getJumlah() {
        return this.jumlah;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }
}
