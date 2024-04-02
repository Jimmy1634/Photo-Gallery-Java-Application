package com.example.photos;

import java.util.ArrayList;
import java.util.List;

public class AdminController {

    public List<User> nonAdminUsers = new ArrayList<>();


    public void createUser(String username){
        User newUser = new User(username);
        nonAdminUsers.add(newUser);
    }
    public void showUsers(){

    }

}
