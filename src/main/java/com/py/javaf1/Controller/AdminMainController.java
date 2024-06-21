package com.py.javaf1.Controller;

import com.py.javaf1.HelloApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminMainController {
    @FXML
    private Label title;

    public void handleAddBook() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/com/py/javaf1/View" +
                    "/AddBookView.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 400, 320);
            Stage stage = (Stage) title.getScene().getWindow();
            stage.setTitle("AddBook");
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleEditBook() {
    }

    public void handleDeleteBook() {
    }

    public void handleSearchBook() {
    }

    public void handleSearchReader() {
    }

    public void handleEditReader() {
    }

    @FXML
    private void handleLogout() {
        // 退出登录

    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
