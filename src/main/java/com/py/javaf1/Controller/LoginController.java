package com.py.javaf1.Controller;

import com.py.javaf1.HelloApplication;
import com.py.javaf1.domain.Reader;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;

public class LoginController {
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
            showAlert("Login Successful", "Welcome, Reader!");
            // 在这里可以跳转到读者主界面

        } else {
            showAlert("Login Failed", "用户名或者密码错误！！！！！");
            passwordField.setText("");
        }
    }

    private boolean efficacy(String username, String password) {
        //读取文件
        try {
            FileReader fileReader = new FileReader("src/main/java/com/py/javaf1/Data/ReaderData");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            while ((line = bufferedReader.readLine())!=null){
                String[] userDetails = line.split(",");
                if (userDetails.length == 7) {
                    String fileUsername = userDetails[0];
                    String filePassword = userDetails[1];
                    // 创建 Reader 对象
                    Reader readerObj = new Reader(fileUsername, filePassword, userDetails[2], userDetails[3],
                            Integer.parseInt(userDetails[4]), Integer.parseInt(userDetails[5]),
                            Double.parseDouble(userDetails[6]));
                    // 验证用户名和密码
                    if (readerObj.getName().equals(username) && readerObj.getPassword().equals(password)) {
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
    protected void switchToAdminLogin() {
        try {
            // 获取当前窗口
            Stage stage = (Stage) usernameField.getScene().getWindow();
            // 加载管理员登录界面
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/com/py/javaf1/View/AdminLoginView.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 400, 320);
            // 设置新场景
            stage.setScene(scene);
            stage.setTitle("AdminLogin");
            stage.show();
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

    public void switchToRegist() {
        try {
            // 获取当前窗口
            Stage stage = (Stage) usernameField.getScene().getWindow();
            // 加载管理员登录界面
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/com/py/javaf1/View/RegistReader.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 400, 320);
            // 设置新场景
            stage.setScene(scene);
            stage.setTitle("Regist");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void switchToForgetPassword() {
        try {
            // 获取当前窗口
            Stage stage = (Stage) usernameField.getScene().getWindow();
            // 加载管理员登录界面
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/com/py/javaf1/View/ForgetPasswordView.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 400, 320);
            // 设置新场景
            stage.setScene(scene);
            stage.setTitle("ForgetPassword");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
