/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gui.programinventaris;

/**
 *
 * @author Ichinomiya Mori
 */
public class BarangModel {
    private String nama;
    private int idbarang, jumlah;
    
    public BarangModel(){
        
    }
    public BarangModel(int idbarang, String nama, int jumlah){
        this.idbarang = idbarang;
        this.nama = nama;
        this.jumlah = jumlah;
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
    
    public int getJumlah() {
        return this.jumlah;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }
}
