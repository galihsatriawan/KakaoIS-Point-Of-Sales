/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kakao.is.controller;

import static com.kakao.is.KakaoIS.arrayStaff;
import static com.kakao.is.KakaoIS.listStaff;
import static com.kakao.is.KakaoIS.listUser;
import com.kakao.is.model.Staff;
import com.kakao.is.model.User;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import java.sql.DriverManager;
import java.sql.ResultSet;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author gokil
 */
public class Database {

    private static Statement stmt;
    private static Connection connect = null;

    public static Statement getStmt() {
        return stmt;
    }

    public static void setStmt(Statement stmt) {
        Database.stmt = stmt;
    }

    public static Connection getConnect() {
        return connect;
    }

    public static void setConnect(Connection connect) {
        Database.connect = connect;
    }

    // Driver JDBC
    private static final String DB_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_NAME = "KakaoIS";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/"+DB_NAME;
    private static final String DB_USER = "gokil";
    private static final String DB_PASS = "gokil";
    

    public static Connection connect_db() {
        //String url = "jdbc:mysql://localhost:3306/kakaoIS";
        if(connect==null){
        try {
            //panggil class
            Class.forName(DB_DRIVER);
            //connect db

            connect = (Connection) DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
        } catch (Exception ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex.getMessage());
        }}
        return connect;
    }

    public static int update(String sql) {
        int status = 0;
        try {
            //connect_db();
            stmt = (Statement) connect_db().createStatement();
            stmt.executeUpdate(sql);
            status = 1;
            //disconnect_db();
        } catch (Exception ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex.getMessage());
        }
        //disconnect_db();
        return status;
    }

    public static ResultSet query(String sql) {
        ResultSet hasil = null;

        try {
            //connect_db();
            stmt = (Statement) connect_db().createStatement();
            hasil = stmt.executeQuery(sql);
            
            //disconnect_db();
        } catch (Exception ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex.getMessage());
        }
        //disconnect_db();
        return hasil;
    }

    public static void generate_user_staff() {
        //connect_db();
        String sql = "/* Sql show data pegawai  */\n"
                + "SELECT\n"
                + "	u.idUser,u.username,u.`password`,st.idStaff,st.namaStaff,st.jk,\n"
                + "	st.alamat,st.telp,ro.idRole,ro.namaRole,jb.idJabatan,jb.namaJabatan\n"
                + "\n"
                + "FROM\n"
                + "	tb_user u,tb_dt_user du,tb_role ro,\n"
                + "	tb_staff st,tb_jabatan jb\n"
                + "WHERE\n"
                + "	u.idUser = du.idUser AND du.idRole = ro.idRole\n"
                + "AND du.idStaff = st.idStaff AND st.idJabatan = jb.idJabatan;";
        ResultSet allUser = query(sql);
        try {
            while (allUser.next()) {
                int idUser = allUser.getInt("idUser");
                String username = allUser.getString("username");
                String password = allUser.getString("password");
                int idRole = allUser.getInt("idRole");
                String role = allUser.getString("namaRole");
                int idStaff = allUser.getInt("idStaff");
                String namaStaff = allUser.getString("namaStaff");
                char jk = allUser.getString("jk").charAt(0);
                String alamat = allUser.getString("alamat");
                String telp = allUser.getString("telp");
                int idJabatan = allUser.getInt("idJabatan");
                String namaJabatan = allUser.getString("namaJabatan");
                
                //sql = 
                User baru = new User(idUser, username, password, idRole, role);
                Staff staffBaru = new Staff(idUser, username, password, idRole, role,idStaff,namaStaff,jk,alamat,telp,namaJabatan);
                //Insert into dictionary
                listUser.put(username, baru);
                listStaff.put(username,staffBaru);
                arrayStaff.add(staffBaru);
                

            }

        } catch (Exception ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
        //disconnect_db();
    }

    public static void disconnect_db() {
        if (connect != null) {
            try {
                connect.close();
            } catch (Exception ex) {
                Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println(ex.getMessage());
            }
        }
    }

}
