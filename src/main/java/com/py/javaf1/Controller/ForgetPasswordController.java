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
    public TextField usernameField;
    public TextField studentNumberField;
    public PasswordField passwordField;
    public PasswordField repeatPasswordField;

    public void Sure() {
        String username = usernameField.getText();
        String studentNumber = studentNumberField.getText();
        String password = passwordField.getText();
        String repeatPassword = repeatPasswordField.getText();
        if (username.isEmpty()) {
            showAlert("ForgetPasswor Error", "用户名不能为空！！！");
            return;
        }
        if (studentNumber.isEmpty()) {
            showAlert("ForgetPasswor Error", "学号不能为空！！！");
            return;
        }
        if (password.isEmpty() || repeatPassword.isEmpty()) {
            showAlert("ForgetPasswor Error", "密码不能为空！！！");
            return;
        }
        if (!password.equals(repeatPassword)) {
            showAlert("ForgetPasswor Error", "两次密码不一样！！！");
            return;
        }
        //读取文件
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
                    if (fileUsername.equals(username)) {
                        if (!studentNumber.equals(userDetails[3])) {
                            showAlert("ForgetPasswor Error", "与初始的注册的学号不一致！！！！");
                            return;
                        } else {
                            readerObj.setPassword(password);
                            change = true;
                        }
                    }
                    readers.add(readerObj);
                }
            }
            if (!change) {
                showAlert("ForgetPasswor Error", "没有这个用户！！！！");
                return;
            }
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/java/com/py/javaf1/Data" +
                    "/ReaderData", false))) {
                for (Reader reader : readers) {
                    writer.write(reader.getName() + "," + reader.getPassword() + "," + reader.getReaderID() + "," + reader.getStudentID() + "," + reader.getBorrowLimit() + "," + reader.getHasOverdue() + "," + reader.getHasOverdue());
                    writer.newLine();
                }
                showAlert("ForgetPassword Succeed", "重置密码成功！！！");
                usernameField.setText("");
                passwordField.setText("");
                repeatPasswordField.setText("");
                studentNumberField.setText("");
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void switchToLoginView() {
        try {
            // 获取当前窗口
            Stage stage = (Stage) usernameField.getScene().getWindow();
            // 加载管理员登录界面
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/com/py/javaf1/View/Login-View" +
                    ".fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 400, 320);
            // 设置新场景
            stage.setScene(scene);
            stage.setTitle("Login");
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
}
