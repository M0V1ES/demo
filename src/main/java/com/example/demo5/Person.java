package com.example.demo5;

import javafx.beans.property.*;

public class Person {
    private SimpleStringProperty login;
    private SimpleStringProperty password;
    private SimpleIntegerProperty block;

    public Person(String login, String password, Integer block) {
        this.login = new SimpleStringProperty(login);
        this.password = new SimpleStringProperty(password);
        this.block = new SimpleIntegerProperty(block);
    }

    public String getLogin() {
        return login.get();
    }

    public void setLogin(String login) {
        this.login.set(login);
    }

    public String getPassword() {
        return password.get();
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    public int getBlock() {
        return block.get();
    }

    public void setBlock(int block) {
        this.block.set(block);
    }

    // Методы property для JavaFX
    public StringProperty loginProperty() {
        return login;
    }

    public StringProperty passwordProperty() {
        return password;
    }

    public IntegerProperty blockProperty() {
        return block;
    }
}