package com.py.javaf1.Controller;

import com.py.javaf1.HelloApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.*;

public class RegistController {
    @FXML
    public TextField studentNumberField;
    @FXML
    public PasswordField passwordField;
    @FXML
    public PasswordField repeatPasswordField;
    @FXML
    public Label Info;
    @FXML
    private TextField usernameField;

    public void switchToLoginView() {
        try {
            // 获取当前窗口
            Stage stage = (Stage) usernameField.getScene().getWindow();
            // 加载管理员登录界面
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/com/py/javaf1/View/Login-View" +
                    ".fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 320, 240);
            // 设置新场景
            stage.setScene(scene);
            stage.setTitle("Login");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void Regist() {
        String password = passwordField.getText();
        String repeatPassword = repeatPasswordField.getText();
        String studentNumber = studentNumberField.getText();
        String username = usernameField.getText();
        if (studentNumber.isEmpty()) {
            showAlert("Regist Failed", "学号为空！！！！！");
            return;
        }
        if (username.isEmpty()) {
            showAlert("Regist Failed", "用户名为空！！！！！！");
            return;
        }
        if (password.isEmpty() || repeatPassword.isEmpty()) {
            showAlert("Regist Failed", "密码输入为空！！！！！");
            return;
        }
        if (!password.equals(repeatPassword)) {
            showAlert("Regist Failed", "两次密码不一致！！！！！");
            return;
        }
        try {
            FileReader fileReader = new FileReader("src/main/java/com/py/javaf1/Data/ReaderData");
            BufferedReader reader = new BufferedReader(fileReader);
            String line;
            int readerId = 0;
            while ((line = reader.readLine()) != null) {
                String[] userDetails = line.split(",");
                if (userDetails.length == 7) {
                    String fileUsername = userDetails[0];
                    String fileStudentId = userDetails[3];
                    if (username.equals(fileUsername)) {
                        showAlert("Regist Failed", "用户名已存在");
                        return;
                    }
                    if (studentNumber.equals(fileStudentId)) {
                        showAlert("Regist Failed", "学号已存在");
                        return;
                    }
                }
                readerId++;
            }
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/java/com/py/javaf1/Data" +
                    "/ReaderData", true))) {
                writer.write(username + "," + password + "," + readerId + "," + studentNumber + "," + "3" + "," + "0" + "," + "0.0");
                writer.newLine();
                showAlert("Regist Successful", "用户注册成功！");
                usernameField.setText("");
                passwordField.setText("");
                repeatPasswordField.setText("");
                studentNumberField.setText("");
                switchToLoginView();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Regist Failed", "无法写入用户信息！");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
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
