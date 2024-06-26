package com.py.javaf1.Controller;

import com.py.javaf1.HelloApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminMainController {
    @FXML
    private Label title; // 标题标签，用于显示当前窗口的标题

    /**
     * 处理添加书籍事件的方法
     */
    public void handleAddBook() {
        try {
            // 加载添加书籍界面
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/com/py/javaf1/View/AddBookView.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 800, 430);
            Stage stage = (Stage) title.getScene().getWindow();
            stage.setTitle("AddBook"); // 设置窗口标题
            stage.setScene(scene); // 设置新的场景
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 处理编辑书籍事件的方法
     */
    public void handleEditBook() {
        try {
            // 加载编辑书籍界面
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/com/py/javaf1/View/EditBookView.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 800, 430);
            Stage stage = (Stage) title.getScene().getWindow();
            stage.setTitle("EditBook"); // 设置窗口标题
            stage.setScene(scene); // 设置新的场景
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 处理删除书籍事件的方法
     */
    public void handleDeleteBook() {
        try {
            // 加载删除书籍界面
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/com/py/javaf1/View/DeleteBookView.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 800, 430);
            Stage stage = (Stage) title.getScene().getWindow();
            stage.setTitle("DeleteBook"); // 设置窗口标题
            stage.setScene(scene); // 设置新的场景
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 处理查找书籍事件的方法
     */
    public void handleSearchBook() {
        try {
            // 加载查找书籍界面
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/com/py/javaf1/View/SearchBookView.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 800, 430);
            Stage stage = (Stage) title.getScene().getWindow();
            stage.setTitle("SearchBook"); // 设置窗口标题
            stage.setScene(scene); // 设置新的场景
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 处理查找读者信息事件的方法
     */
    public void handleSearchReader() {
        try {
            // 加载查找读者信息界面
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/com/py/javaf1/View/SearchReaderView.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 800, 430);
            Stage stage = (Stage) title.getScene().getWindow();
            stage.setTitle("SearchReader"); // 设置窗口标题
            stage.setScene(scene); // 设置新的场景
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 处理修改读者信息事件的方法
     */
    public void handleEditReader() {
        try {
            // 加载修改读者信息界面
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/com/py/javaf1/View/ModifyReaderView.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 800, 430);
            Stage stage = (Stage) title.getScene().getWindow();
            stage.setTitle("ModifyReader"); // 设置窗口标题
            stage.setScene(scene); // 设置新的场景
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 处理退出登录事件的方法
     */
    @FXML
    private void handleLogout() {
        try {
            // 加载管理员登录界面
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/com/py/javaf1/View/AdminLoginView.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 800, 430);
            Stage stage = (Stage) title.getScene().getWindow();
            stage.setTitle("AdminLogin"); // 设置窗口标题
            stage.setScene(scene); // 设置新的场景
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
