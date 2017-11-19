package com.priadka.newsit_project.DTO;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import static com.priadka.newsit_project.Constant.F_BOOKMARK;
import static com.priadka.newsit_project.Constant.F_IMAGE;
import static com.priadka.newsit_project.Constant.F_USER;

public class UserDTO{
    private FirebaseAuth mAuth;
    private DatabaseReference myRefUsers;
    private FirebaseUser userFire;

    private String user_login;
    private String user_email;
    private Integer user_image;
    private ArrayList<Integer> user_bookmarksList;

    // Конструктор для Пользователя
    public UserDTO(String user_login, String user_email, Integer user_image, ArrayList<Integer> user_bookmarksList){
        this.user_login = user_login;
        this.user_email = user_email;
        this.user_image = user_image;
        this.user_bookmarksList = user_bookmarksList;
    }
    // Getter and Setter
    public String getUser_login() {return user_login;}
    public String getUser_email() {return user_email;}
    public int getUser_image() {return user_image;}
    public ArrayList getUser_bookmarksList() {return user_bookmarksList;}
    // При изменении изображения пользователя, списка закладок локально - отправка в БД
    public void setUser_image(int user_image) {
        this.user_image = user_image;
        mAuth = FirebaseAuth.getInstance();
        userFire = mAuth.getCurrentUser();
        myRefUsers = FirebaseDatabase.getInstance().getReference().child(F_USER).child(userFire.getUid()).child(F_IMAGE);
        myRefUsers.setValue(String.valueOf(this.user_image));
    }
    public void setUser_bookmarksList(ArrayList<Integer> user_bookmarksList) {
        this.user_bookmarksList = user_bookmarksList;
        mAuth = FirebaseAuth.getInstance();
        userFire = mAuth.getCurrentUser();
        myRefUsers = FirebaseDatabase.getInstance().getReference().child(F_USER).child(userFire.getUid()).child(F_BOOKMARK);
        String bookmarks = "";
        for (Integer s : user_bookmarksList) {
            bookmarks += String.valueOf(s) + " ";
        }
        myRefUsers.setValue(bookmarks);
    }
}