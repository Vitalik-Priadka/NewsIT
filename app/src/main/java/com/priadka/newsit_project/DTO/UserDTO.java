package com.priadka.newsit_project.DTO;

import java.util.ArrayList;

public class UserDTO{
    private String user_login;
    private String user_email;
    private String user_password;
    private Integer user_image;
    private ArrayList<Integer> user_bookmarksList;

    public UserDTO(String user_login, String user_email, String user_password, Integer user_image, ArrayList<Integer> user_bookmarksList){
        this.user_login = user_login;
        this.user_email = user_email;
        this.user_password = user_password;
        this.user_image = user_image;
        this.user_bookmarksList = user_bookmarksList;
    }

    public String getUser_login() {return user_login;}
    public String getUser_email() {return user_email;}
    public String getUser_password() {return user_password;}

    public int getUser_image() {return user_image;}
    public void setUser_image(int user_image) {this.user_image = user_image;}

    public ArrayList getUser_bookmarksList() {return user_bookmarksList;}
    public void setUser_bookmarksList(ArrayList<Integer> user_bookmarksList) {this.user_bookmarksList = user_bookmarksList;}
}