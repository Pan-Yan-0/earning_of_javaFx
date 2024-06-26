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
    public TextField studentNumberField; // FXML文件中定义的学号输入字段
    @FXML
    public PasswordField passwordField; // FXML文件中定义的密码输入字段
    @FXML
    public PasswordField repeatPasswordField; // FXML文件中定义的重复密码输入字段
    @FXML
    public Label Info; // FXML文件中定义的标签，用于显示信息
    @FXML
    private TextField usernameField; // FXML文件中定义的用户名输入字段

    /**
     * 切换到登录界面的方法
     */
    public void switchToLoginView() {
        try {
            Stage stage = (Stage) usernameField.getScene().getWindow(); // 获取当前窗口
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/com/py/javaf1/View/Login-View.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 400, 320);
            stage.setScene(scene); // 设置新场景
            stage.setTitle("Login"); // 设置窗口标题
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 注册新用户的方法
     */
    public void Regist() {
        String password = passwordField.getText(); // 获取密码输入字段的文本
        String repeatPassword = repeatPasswordField.getText(); // 获取重复密码输入字段的文本
        String studentNumber = studentNumberField.getText(); // 获取学号输入字段的文本
        String username = usernameField.getText(); // 获取用户名输入字段的文本

        // 检查用户名是否为空
        if (username.isEmpty()) {
            showAlert("Regist Failed", "用户名为空！！！！！！");
            return;
        }

        // 检查学号是否为空
        if (studentNumber.isEmpty()) {
            showAlert("Regist Failed", "学号为空！！！！！");
            return;
        }

        // 检查密码和重复密码是否为空
        if (password.isEmpty() || repeatPassword.isEmpty()) {
            showAlert("Regist Failed", "密码输入为空！！！！！");
            return;
        }

        // 检查两次输入的密码是否一致
        if (!password.equals(repeatPassword)) {
            showAlert("Regist Failed", "两次密码不一致！！！！！");
            return;
        }

        try {
            // 读取文件内容，检查用户名和学号是否已存在
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

            // 写入新的用户信息到文件
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/java/com/py/javaf1/Data/ReaderData", true))) {
                writer.write(username + "," + password + "," + readerId + "," + studentNumber + "," + "3" + "," + "0" + "," + "0.0");
                writer.newLine();
                showAlert("Regist Successful", "用户注册成功！");
                usernameField.setText(""); // 清空用户名输入字段
                passwordField.setText(""); // 清空密码输入字段
                repeatPasswordField.setText(""); // 清空重复密码输入字段
                studentNumberField.setText(""); // 清空学号输入字段
                switchToLoginView(); // 注册成功后切换到登录界面
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Regist Failed", "无法写入用户信息！");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 显示提示框的方法
     * @param title 提示框的标题
     * @param message 提示框的信息
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION); // 创建一个信息类型的提示框
        alert.setTitle(title); // 设置提示框标题
        alert.setHeaderText(null); // 设置提示框头部文本
        alert.setContentText(message); // 设置提示框内容文本
        alert.showAndWait(); // 显示提示框并等待用户关闭
    }
}
