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
public class Staff extends User {

    
    private int idStaff;
    private String namaStaff;
    private char jk;
    private String alamat;
    private String telp;
    private String jabatan;
    
    public Staff(int idUser, String username,String password,int idRole,String role,int idStaff, String namaStaff, char jk, String alamat, String telp, String jabatan) {
        super(idUser,  username, password, idRole, role);
        this.idStaff = idStaff;
        this.namaStaff = namaStaff;
        this.jk = jk;
        this.alamat = alamat;
        this.telp = telp;
        this.jabatan = jabatan;
        
    }

    public Staff() {
        super(-1,  "", "", -1, "");
        this.idStaff = -1;
        this.namaStaff = "";
        this.jk = '\0';
        this.alamat = "";
        this.telp = "";
        this.jabatan = "";
    }
    


    public void setNamaStaff(String namaStaff) {
        this.namaStaff = namaStaff;
    }

    public void setJk(char jk) {
        this.jk = jk;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public void setTelp(String telp) {
        this.telp = telp;
    }

    public void setJabatan(String jabatan) {
        this.jabatan = jabatan;
    }

    public int getIdStaff() {
        return idStaff;
    }

    public String getNamaStaff() {
        return namaStaff;
    }

    public char getJk() {
        return jk;
    }

    public String getAlamat() {
        return alamat;
    }

    public String getTelp() {
        return telp;
    }

    public String getJabatan() {
        return jabatan;
    }
 
}
