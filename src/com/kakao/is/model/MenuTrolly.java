/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kakao.is.model;

/**
 *
 * @author gokil
 */
/*
    MenuTrolly untuk keranjang belanja
*/
public class MenuTrolly {
    private int id;
    private String nama;
    private int harga ;
    private int qty;
    private int subtotal;
    
    public MenuTrolly(int id,String nama,int harga,int qty,int subtotal){
        this.id = id;
        this.nama = nama;
        this.harga = harga;
        this.qty = qty;
        this.subtotal = subtotal;
    }
    public void setId(int id) {
        this.id = id;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public void setHarga(int harga) {
        this.harga = harga;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public void setSubtotal(int subtotal) {
        this.subtotal = subtotal;
    }
    

    public int getId() {
        return id;
    }

    public String getNama() {
        return nama;
    }

    public int getHarga() {
        return harga;
    }

    public int getQty() {
        return qty;
    }

    public int getSubtotal() {
        return subtotal;
    }
    
    

    
}
