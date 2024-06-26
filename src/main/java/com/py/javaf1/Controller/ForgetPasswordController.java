package com.py.javaf1.Controller;

import com.py.javaf1.HelloApplication;
import com.py.javaf1.domain.Reader;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;

public class ForgetPasswordController {
    public TextField usernameField; // 用户名输入字段
    public TextField studentNumberField; // 学号输入字段
    public PasswordField passwordField; // 密码输入字段
    public PasswordField repeatPasswordField; // 重复密码输入字段

    /**
     * 确认重置密码的方法
     */
    public void Sure() {
        String username = usernameField.getText(); // 获取用户名
        String studentNumber = studentNumberField.getText(); // 获取学号
        String password = passwordField.getText(); // 获取密码
        String repeatPassword = repeatPasswordField.getText(); // 获取重复密码

        // 检查用户名是否为空
        if (username.isEmpty()) {
            showAlert("ForgetPassword Error", "用户名不能为空！！！");
            return;
        }
        // 检查学号是否为空
        if (studentNumber.isEmpty()) {
            showAlert("ForgetPassword Error", "学号不能为空！！！");
            return;
        }
        // 检查密码和重复密码是否为空
        if (password.isEmpty() || repeatPassword.isEmpty()) {
            showAlert("ForgetPassword Error", "密码不能为空！！！");
            return;
        }
        // 检查两次输入的密码是否一致
        if (!password.equals(repeatPassword)) {
            showAlert("ForgetPassword Error", "两次密码不一样！！！");
            return;
        }

        // 读取文件并验证用户信息
        try {
            FileReader fileReader = new FileReader("src/main/java/com/py/javaf1/Data/ReaderData");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            ArrayList<Reader> readers = new ArrayList<>();
            boolean change = false;

            while ((line = bufferedReader.readLine()) != null) {
                String[] userDetails = line.split(",");
                if (userDetails.length == 7) {
                    String fileUsername = userDetails[0];
                    String filePassword = userDetails[1];

                    // 创建 Reader 对象
                    Reader readerObj = new Reader(fileUsername, filePassword, userDetails[2], userDetails[3],
                            Integer.parseInt(userDetails[4]), Integer.parseInt(userDetails[5]),
                            Double.parseDouble(userDetails[6]));

                    // 检查用户名和学号是否匹配
                    if (fileUsername.equals(username)) {
                        if (!studentNumber.equals(userDetails[3])) {
                            showAlert("ForgetPassword Error", "与初始的注册的学号不一致！！！！");
                            return;
                        } else {
                            readerObj.setPassword(password); // 更新密码
                            change = true;
                        }
                    }
                    readers.add(readerObj); // 将 Reader 对象添加到列表中
                }
            }

            // 如果没有匹配的用户，显示错误提示
            if (!change) {
                showAlert("ForgetPassword Error", "没有这个用户！！！！");
                return;
            }

            // 将更新后的用户信息写入文件
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/java/com/py/javaf1/Data/ReaderData", false))) {
                for (Reader reader : readers) {
                    writer.write(reader.getName() + "," + reader.getPassword() + "," + reader.getReaderID() + "," + reader.getStudentID() + "," + reader.getBorrowLimit() + "," + reader.getHasOverdue() + "," + reader.getHasOverdue());
                    writer.newLine();
                }
                showAlert("ForgetPassword Succeed", "重置密码成功！！！");
                usernameField.setText(""); // 清空用户名输入字段
                passwordField.setText(""); // 清空密码输入字段
                repeatPasswordField.setText(""); // 清空重复密码输入字段
                studentNumberField.setText(""); // 清空学号输入字段
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 切换到登录界面的方法
     */
    public void switchToLoginView() {
        try {
            Stage stage = (Stage) usernameField.getScene().getWindow(); // 获取当前窗口
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/com/py/javaf1/View/Login-View.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 800, 430);
            stage.setScene(scene); // 设置新场景
            stage.setTitle("Login"); // 设置窗口标题
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示提示框的方法
     * @param title 提示框的标题
     * @param message 提示框的内容
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION); // 创建信息提示框
        alert.setTitle(title); // 设置标题
        alert.setHeaderText(null); // 设置头部文本为空
        alert.setContentText(message); // 设置内容文本
        alert.showAndWait(); // 显示提示框并等待用户关闭
    }
}
