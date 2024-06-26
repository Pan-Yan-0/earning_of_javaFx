package com.py.javaf1.Controller;

import com.py.javaf1.HelloApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * AdminLoginController类处理管理员登录界面的用户交互
 */
public class AdminLoginController {

    @FXML
    private TextField usernameField; // 用户名输入框

    @FXML
    private PasswordField passwordField; // 密码输入框

    /**
     * 处理登录按钮点击事件
     */
    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // 验证输入是否为空
        if (username.isEmpty()) {
            showAlert("Login Failed", "用户名为空！！！！！");
            return;
        }
        if (password.isEmpty()) {
            showAlert("Login Failed", "密码为空！！！！！");
            return;
        }

        // 简单的身份验证逻辑，可以根据需要进行扩展
        if (efficacy(username, password)) {
            showAlert("Login Successful", "欢迎管理员！！！");
            // 在这里可以跳转到管理员主界面
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/com/py/javaf1/View/AdminMainView.fxml"));
                Scene scene = new Scene(fxmlLoader.load(), 400, 320);
                Stage stage = (Stage) usernameField.getScene().getWindow();
                stage.setTitle("AdminMain");
                stage.setScene(scene);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            showAlert("Login Failed", "用户名或者密码错误");
            passwordField.setText("");
        }
    }

    /**
     * 验证用户名和密码
     * @param username 输入的用户名
     * @param password 输入的密码
     * @return 是否验证成功
     */
    private boolean efficacy(String username, String password) {
        //读取文件
        try {
            FileReader fileReader = new FileReader("src/main/java/com/py/javaf1/Data/AdminData");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
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
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 切换到读者登录界面
     */
    @FXML
    private void switchToReaderLogin() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/com/py/javaf1/View/Login-View.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 800, 430);
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setTitle("ReaderLogin");
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示提示信息
     * @param title 提示框标题
     * @param message 提示信息内容
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
