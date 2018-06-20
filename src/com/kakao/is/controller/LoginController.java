/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kakao.is.controller;

import com.kakao.is.model.User;
import com.kakao.is.model.Staff;
import com.kakao.is.view.LoginFrame;

/**
 *
 * @author gokil
 */
public class LoginController {

    
    private Staff currentStaff;
    private User currentUser;
    static LoginController instance ;
    public LoginController(){
        //this.instance = getInstance(); unfinite loop
        //this.currentStaff = new Staff();
        //this.currentUser = new User();
    }
    public static LoginController getInstance(){
        if(instance==null){
            instance = new LoginController();
        }
        return instance;
    }
    public void displayLogin(){
        LoginFrame loginFrame = new LoginFrame();
        loginFrame.setVisible(true);
        loginFrame.setLocationRelativeTo(null);
    }
    public void setCurrentStaff(Staff currentStaff) {
        this.currentStaff = currentStaff;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public Staff getCurrentStaff() {
        return currentStaff;
    }

    public User getCurrentUser() {
        return currentUser;
    }
}
