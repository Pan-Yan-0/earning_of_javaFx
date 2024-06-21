package com.py.javaf1.Controller;

import com.py.javaf1.HelloApplication;
import com.py.javaf1.domain.Reader;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class AdminLoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        if (username.isEmpty()){
            showAlert("Login Failed", "用户名为空！！！！！");
            return;
        }
        if (password.isEmpty()){
            showAlert("Login Failed", "密码为空！！！！！");
            return;
        }
        // 简单的身份验证逻辑，可以根据需要进行扩展
        if (efficacy(username,password)) {
            showAlert("Login Successful", "Welcome, Admin!");
            // 在这里可以跳转到管理员主界面
        } else {
            showAlert("Login Failed", "用户名或者密码错误");
            passwordField.setText("");
        }
    }
    private boolean efficacy(String username, String password) {
        //读取文件
        try {
            FileReader fileReader = new FileReader("src/main/java/com/py/javaf1/Data/AdminData");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            while ((line = bufferedReader.readLine())!=null){
                String[] userDetails = line.split(",");
                if (userDetails.length == 2) {
                    String fileUsername = userDetails[0];
                    String filePassword = userDetails[1];
                    // 验证用户名和密码
                    if (fileUsername.equals(username) && filePassword.equals(password)) {
                        return true;
                    }
                }
            }
            return false;
        } catch (IOException  e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    private void switchToReaderLogin() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/com/py/javaf1/View/Login-View.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 400, 320);
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setTitle("ReaderLogin");
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
