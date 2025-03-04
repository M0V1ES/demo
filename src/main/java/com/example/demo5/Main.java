package com.example.demo5;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class Main {

    @FXML
    private Button buttonAdd;

    @FXML
    private TableView<Person> table;
    Database db = new Database();
    @FXML
    void initialize() {
        TableColumn<Person, String> nameColumn = new TableColumn<>("Логин");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("login"));
        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        nameColumn.setEditable(true);


        nameColumn.setOnEditCommit(event -> {
            Person person = event.getRowValue();
            try {
                db.updateLogin(person.getLogin(), event.getNewValue());
            } catch (SQLException | ClassNotFoundException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Ошибка");
                alert.setContentText("Не удалось обновить логин");
                alert.showAndWait();
            }
            person.setLogin(event.getNewValue());
        });
        table.getColumns().add(nameColumn);


        TableColumn<Person, String> passwordColumn = new TableColumn<>("Пароль");
        passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
        passwordColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        passwordColumn.setEditable(true);


        passwordColumn.setOnEditCommit(event -> {
            Person person = event.getRowValue();
            try {
                db.updatePassword(person.getPassword(), event.getNewValue());
            } catch (SQLException | ClassNotFoundException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Ошибка");
                alert.setContentText("Не удалось обновить пароль");
                alert.showAndWait();
            }
            person.setPassword(event.getNewValue());
        });
        table.getColumns().add(passwordColumn);


        TableColumn<Person, Integer> blockColumn = new TableColumn<>("Блокировка");
        blockColumn.setCellValueFactory(new PropertyValueFactory<>("block"));
        blockColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        blockColumn.setEditable(true);


        blockColumn.setOnEditCommit(event -> {
            Person person = event.getRowValue();
            try {
                db.updateBlock(person.getLogin());
            } catch (SQLException | ClassNotFoundException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Ошибка");
                alert.setContentText("Не удалось обновить блокировку");
                alert.showAndWait();
            }
            person.setBlock(event.getNewValue());
        });
        table.getColumns().add(blockColumn);


        try {
            table.getItems().addAll(db.getUsers());
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    public void onClick() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("createNewUser.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }
}


