/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kakao.is;

import com.kakao.is.controller.Database;
import com.kakao.is.controller.LoginController;
import com.kakao.is.model.Staff;
import com.kakao.is.model.User;
import com.kakao.is.view.KakaoFrame;
import com.kakao.is.view.LoginFrame;
import java.util.ArrayList;

import java.util.HashMap;

/**
 *
 * @author gokil
 */
public class KakaoIS {

    /**
     * @param args the command line arguments
     */
    LoginController loginControl;
    static KakaoIS sample;
    private KakaoIS(){
        loginControl = LoginController.getInstance();
        
    }
    public static KakaoIS getSample(){
        if(sample==null){
            sample = new KakaoIS();
        }
        return sample;
    }
    public LoginController getLoginController() {
        return loginControl;
    }
    public static HashMap<String,User> listUser=new HashMap<>();
    public static HashMap<String,Staff> listStaff = new HashMap<>();
    public static ArrayList<Staff> arrayStaff = new ArrayList<>();
    public static void main(String[] args) {
        // TODO code application logic here
        Database.connect_db();
        Database.generate_user_staff();
         /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(KakaoFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(KakaoFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(KakaoFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(KakaoFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                //agar variable displayLogin tidak static , maka diperlukan perantara
                //kegunaan static :
                /*
                    suatu static variable akan dapat digunakan secara bersama oleh seluruh objeknya (Share variable)
                    -> jika kita tidak mendefinisikan static variable, maka kita harus membuat suatu objek terlebih dahulu
                    -> static method tidak bisa mengakses non-static method
                */
                // The usability of "static"
                /*
                   1. a static variable can be used by the all instance(object) without create new instance(object)so it's mean shared variable
                        1.1 let say ,a static variable can be used by all class also 
                   2. if we don't define static variabel (normal variable), when we wanna access it we have to create the object first
                   3. a static method can't access non-static method
                */
                KakaoIS.getSample().loginControl.displayLogin();
                //LoginController.displayLogin();
            }
        });
        
        //Database.disconnect_db();
    }
    
}
