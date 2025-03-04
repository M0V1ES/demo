package com.example.demo5;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.sql.SQLException;

public class CreateNewUser {

    @FXML
    private Button buttonEnter;

    @FXML
    private TextField textLogin;

    @FXML
    private TextField textPassword;


    public void onclickEnter() throws SQLException, ClassNotFoundException {
        Database db = new Database();
        if (textLogin.getText() != null && textPassword.getText() != null) {
            db.createUser(textLogin.getText(), textPassword.getText());
        }
    }

}
