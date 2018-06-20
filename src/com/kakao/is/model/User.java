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
public class User {
    
    private int idUser;
    private String username;
    private String password;
    private int idRole;
    private String role;

  
    
    public User(){
        this.idUser=-1;
        this.username="";
        this.password="";
        this.idRole=-1;
        this.role ="";
    }
    public User(int idUser, String username,String password,int idRole,String role){
        this.idUser=idUser;
        this.username=username;
        this.password=password;
        this.idRole=idRole;
        this.role = role;
    }
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getIdUser() {
        return this.idUser;
    }
    
    public void setUsername(String username) {
        this.username= username;
    }
    public String getUsername() {
        return this.username;
    }
    
    public void setPassword(String password) {
        this.password= password;
    }
    public String getPassword() {
        return this.password;
    }
    
    public void setIdRole(int idRole) {
        this.idRole= idRole;
    }
    public int getIdRole() {
        return this.idRole;
    }

}
