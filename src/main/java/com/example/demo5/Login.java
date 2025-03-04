package com.example.demo5;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class Login {

    @FXML
    private Button buttonEnter;

    @FXML
    private TextField textLogin;

    @FXML
    private TextField textPassword;


    public boolean ShowAlert(boolean login) {
        if (login){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Успешный вход");
            alert.setHeaderText("Вход");
            alert.showAndWait();
            return true;
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Произошла ошибка.");
            alert.setHeaderText("Вход");
            alert.showAndWait();
            return false;
        }
    }

    @FXML
    protected void buttonClick() throws SQLException, ClassNotFoundException, IOException {
        Database db = new Database();
        try {
            if (db.checkToBlock(textLogin.getText()) >= 3){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Произошла ошибка.");
                alert.setHeaderText("Блокировка аккаунта");
                alert.showAndWait();
                return;
            }
            if (ShowAlert(db.checkLogin(textLogin.getText(), textPassword.getText()))){
                SessionMaker.SessionManager.setCurrentUser(textLogin.getText());
                if (db.checkFirstEnter(textLogin.getText())) {
                    db.cancelBlock(textLogin.getText());
                    FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("main.fxml"));
                    Scene scene = new Scene(fxmlLoader.load());
                    Stage stage = new Stage();
                    stage.setScene(scene);
                    stage.show();

                }else {
                    db.cancelBlock(textLogin.getText());
                    FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("changePassword.fxml"));
                    Scene scene = new Scene(fxmlLoader.load());
                    Stage stage = new Stage();
                    stage.setScene(scene);
                    stage.show();

                }
            }else{
                db.countToBlock(textLogin.getText());
            }
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}