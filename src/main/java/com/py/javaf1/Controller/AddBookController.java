package com.py.javaf1.Controller;

import com.py.javaf1.HelloApplication;
import com.py.javaf1.domain.Book;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.*;

public class AddBookController {
    @FXML
    private TextField bookIDField; // 图书编号输入框
    @FXML
    private TextField titleField; // 书名输入框
    @FXML
    private TextField authorField; // 作者输入框
    @FXML
    private TextField publisherField; // 出版社输入框
    @FXML
    private TextField totalCopiesField; // 总册数输入框
    @FXML
    private TextField locationField; // 现存地址输入框

    /**
     * 处理添加书籍事件的方法
     */
    @FXML
    public void handleAddBook() {
        // 获取输入框中的值
        String bookID = bookIDField.getText();
        String title = titleField.getText();
        String author = authorField.getText();
        String publisher = publisherField.getText();
        String totalCopies = totalCopiesField.getText();
        String location = locationField.getText();

        // 检查输入框是否为空，并显示相应的提示框
        if (bookID.isEmpty()) {
            showAlert("Admin AddBook Error", "图书编号为空！！！！");
            return;
        }
        if (title.isEmpty()) {
            showAlert("Admin AddBook Error", "书名为空！！！！");
            return;
        }
        if (author.isEmpty()) {
            showAlert("Admin AddBook Error", "作者为空！！！！");
            return;
        }
        if (publisher.isEmpty()) {
            showAlert("Admin AddBook Error", "出版社为空！！！！");
            return;
        }
        if (totalCopies.isEmpty()) {
            showAlert("Admin AddBook Error", "总册数为空！！！！");
            return;
        }
        if (location.isEmpty()) {
            showAlert("Admin AddBook Error", "现存地址为空！！！！");
            return;
        }

        int totalCopie = Integer.parseInt(totalCopies);
        Book newBook = new Book(bookID, title, author, publisher, totalCopie, totalCopie, location, 0);

        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/java/com/py/javaf1/Data/BookData"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] bookDetails = line.split(",");
                if (bookDetails.length == 8) {
                    String fileBookId = bookDetails[0];
                    String fileTitle = bookDetails[1];
                    if (bookID.equals(fileBookId)) {
                        showAlert("AddBook Failed", "书籍编号已存在");
                        return;
                    }
                    if (fileTitle.equals(title)) {
                        showAlert("AddBook Failed", "书籍名称已存在");
                        return;
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/java/com/py/javaf1/Data/BookData", true))) {
            writer.write(newBook.getBookID() + "," + newBook.getTitle() + "," + newBook.getAuthor() + "," + newBook.getPublisher() + "," + newBook.getTotalCopies() + "," + newBook.getAvailableCopies() + "," + newBook.getLocation() + "," + newBook.getBorrowCount());
            writer.newLine();
            showAlert("添加书籍成功", "书籍已成功添加！");
            // 清空输入框
            bookIDField.setText("");
            titleField.setText("");
            authorField.setText("");
            publisherField.setText("");
            totalCopiesField.setText("");
            locationField.setText("");
        } catch (IOException e) {
            showAlert("错误", "写入文件时发生错误！");
        }
    }

    /**
     * 处理返回管理员主界面事件的方法
     */
    @FXML
    public void handleReturnAdminMain() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/com/py/javaf1/View/AdminMainView.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 800, 430);
            Stage stage = (Stage) bookIDField.getScene().getWindow();
            stage.setTitle("AdminMain");
            stage.setScene(scene);
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
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
