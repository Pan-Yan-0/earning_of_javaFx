package com.py.javaf1.Controller;

import com.py.javaf1.HelloApplication;
import com.py.javaf1.domain.Reader;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * LoginController类处理登录界面的用户交互
 */
public class LoginController {
    public static String loginName = null; // 当前登录的用户名
    public static String readerId = null; // 当前登录的读者ID

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
            showAlert("Login Successful", "欢迎，读者！！");
            // 在这里可以跳转到读者主界面
            try {
                loginName = username;
                // 获取当前窗口
                Stage stage = (Stage) usernameField.getScene().getWindow();
                // 加载新的FXML文件
                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/com/py/javaf1/View/ReaderMainView.fxml"));
                Scene scene = new Scene(fxmlLoader.load(), 1080, 480);
                // 设置新场景
                stage.setScene(scene);
                stage.setTitle("ReaderMain");
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            showAlert("Login Failed", "用户名或者密码错误！！！！！");
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
            FileReader fileReader = new FileReader("src/main/java/com/py/javaf1/Data/ReaderData");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] userDetails = line.split(",");
                if (userDetails.length == 7) {
                    String fileUsername = userDetails[0];
                    String filePassword = userDetails[1];
                    // 创建 Reader 对象
                    Reader readerObj = new Reader(fileUsername, filePassword, userDetails[2], userDetails[3],
                            Integer.parseInt(userDetails[4]), Integer.parseInt(userDetails[5]),
                            Double.parseDouble(userDetails[6]));
                    readerId = readerObj.getReaderID();
                    // 验证用户名和密码
                    if (readerObj.getName().equals(username) && readerObj.getPassword().equals(password)) {
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
     * 切换到管理员登录界面
     */
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

    /**
     * 切换到注册界面
     */
    public void switchToRegist() {
        try {
            // 获取当前窗口
            Stage stage = (Stage) usernameField.getScene().getWindow();
            // 加载注册界面
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

    /**
     * 切换到忘记密码界面
     */
    public void switchToForgetPassword() {
        try {
            // 获取当前窗口
            Stage stage = (Stage) usernameField.getScene().getWindow();
            // 加载忘记密码界面
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
