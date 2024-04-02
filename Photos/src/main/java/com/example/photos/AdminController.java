package com.example.photos;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AdminController extends LogoutClass{
    //3 things: List users, create users, show users
    public static List<User> nonAdminUsers = new ArrayList<>();

    String createSuccess = "User has been created.";
    String createFail = "Username taken.";
    String deleteSuccess = "User has been deleted.";
    String deleteFail = "User not found.";
    @FXML
    Text createMessage;
    @FXML
    Text deleteMessage;
    @FXML
    TextField userToBeCreated;
    @FXML
    TextField userToBeDeleted;

    @FXML
    Text UsersList;
    String listOfUsers = "";


    private Stage stage;
    private Scene scene;
    private Parent root;

    public void createUser() {

        if (userToBeCreated == null){
            createMessage.setOpacity(1);
            createMessage.setText(createFail);
            createMessage.setFill(Color.RED);
            return;
        }
        if (userToBeCreated.getText().equals("")){
            createMessage.setOpacity(1);
            createMessage.setText("Enter a valid username.");
            createMessage.setFill(Color.RED);
            return;
        }
        String username = userToBeCreated.getText();
        //handle "admin" and "stock" case- these usernames are not allowed
        if (username.equals("admin") || username.equals("stock")){
            createMessage.setOpacity(1);
            createMessage.setText("This username is not allowed.");
            createMessage.setFill(Color.RED);
            return;
        }
        User newUser = new User(username);
        for (User i : nonAdminUsers){
            if (username.equals(i.username)){
                createMessage.setOpacity(1);
                createMessage.setText(createFail);
                createMessage.setFill(Color.RED);
                return;
            }
        }
        nonAdminUsers.add(newUser);
        createMessage.setOpacity(1);
        createMessage.setText(createSuccess);
        createMessage.setFill(Color.GREEN);
        return;
    }
    public void deleteUser() {
        if (userToBeDeleted == null){
            deleteMessage.setOpacity(1);
            deleteMessage.setText(deleteFail);
            deleteMessage.setFill(Color.RED);
            return;
        }
        if (userToBeDeleted.getText().equals("")){
            deleteMessage.setOpacity(1);
            deleteMessage.setText("Enter a valid username.");
            deleteMessage.setFill(Color.RED);
            return;
        }
        String username = userToBeDeleted.getText();
        if (username.equals("admin") || username.equals("stock")){
            deleteMessage.setOpacity(1);
            deleteMessage.setText("These users cannot be deleted.");
            deleteMessage.setFill(Color.RED);
            return;
        }
        for (User i : nonAdminUsers){
            if (username.equals(i.username)){
                nonAdminUsers.remove(i);
                deleteMessage.setOpacity(1);
                deleteMessage.setText(deleteSuccess);
                deleteMessage.setFill(Color.GREEN);
                return;
            }
        }
        deleteMessage.setOpacity(1);
        deleteMessage.setText(deleteFail);
        deleteMessage.setFill(Color.RED);
        return;
    }

    public void showUsers(){
        listOfUsers = "";
        for (User i : nonAdminUsers){
            if (listOfUsers.equals("")){
                listOfUsers = i.username;
            }
            else {
                listOfUsers = listOfUsers + ", " + i.username;
            }
        }
        UsersList.setText(listOfUsers);
        return;

    }



}
