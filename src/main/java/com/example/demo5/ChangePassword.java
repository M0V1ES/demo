package com.example.demo5;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.sql.SQLException;

public class ChangePassword {

    @FXML
    private Button changePassword;

    @FXML
    private TextField textNewPassword;

    @FXML
    private TextField textNewPassword2;

    @FXML
    private TextField textOldPassword;


    public void ShowAlert(boolean login) {
        Alert alert;
        if (login){
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Успешный вход");
        }
        else {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Произошла ошибка.");
        }
        alert.setHeaderText("Вход");
        alert.showAndWait();
    }
    @FXML
    protected void onButtonClick(){
        Database db = new Database();
        try {
            if(textNewPassword2.getText().equals(textNewPassword.getText()) && (db.checkLogin(SessionMaker.SessionManager.getCurrentUser(), textOldPassword.getText()))){
                db.createPassword(SessionMaker.SessionManager.getCurrentUser(), textNewPassword2.getText());
                db.updateFirstEnter(SessionMaker.SessionManager.getCurrentUser());
            }
            else{
                ShowAlert(false);
            }

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

}
