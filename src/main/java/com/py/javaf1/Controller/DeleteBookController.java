package com.py.javaf1.Controller;

import com.py.javaf1.HelloApplication;
import com.py.javaf1.domain.Book;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.Objects;

public class DeleteBookController {
    @FXML
    private Label showDetail1;
    @FXML
    private Label showDetail;
    @FXML
    private TextField bookIDField;

    /*
     * @TODO 这里缺少一个确认所有的学生归还才能删除的逻辑
     *   也可以使用其他的方法
     * */
    @FXML
    public void handleDeleteBook() {
        String bookID = bookIDField.getText();
        if (bookID.isEmpty()) {
            showAlert("Search Error", "图书编号为空！！！！");
            return;
        }

        ArrayList<Book> books = new ArrayList<>();
        try {
            FileReader fileReader = new FileReader("src/main/java/com/py/javaf1/Data/BookData");
            BufferedReader reader = new BufferedReader(fileReader);
            String line;
            boolean change = false;
            while ((line = reader.readLine()) != null) {
                String[] bookDetails = line.split(",");
                Book newBook = null;
                if (bookDetails.length == 8) {
                    String fileBookId = bookDetails[0];
                    newBook = new Book(fileBookId, bookDetails[1], bookDetails[2], bookDetails[3],
                            Integer.parseInt(bookDetails[4]), Integer.parseInt(bookDetails[5]), bookDetails[6],
                            Integer.parseInt(bookDetails[7]));
                    if (bookID.equals(fileBookId)) {
                        change = true;
                        continue;
                    }
                }
                books.add(newBook);
            }
            if (!change){
                showAlert("Search Error", "查找不到这个图书信息！！！！");
                return;
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/java/com/py/javaf1/Data/BookData",
                false))) {
            for (Book newBook : books) {
                writer.write(newBook.getBookID() + "," + newBook.getTitle() + "," + newBook.getAuthor() + "," + newBook.getPublisher() + "," + newBook.getTotalCopies() + "," + newBook.getAvailableCopies() + "," + newBook.getLocation() + "," + newBook.getBorrowCount());
                writer.newLine();
            }
            showAlert("删除书籍成功", "书籍已成功删除！");

            bookIDField.setText("");
            showDetail.setText("");
            showDetail1.setText("");
        } catch (IOException e) {
            showAlert("错误", "写入文件时发生错误！");
        }
    }

    @FXML
    public void handleSearchBook() {
        String bookID = bookIDField.getText();
        if (bookID.isEmpty()) {
            showAlert("Search Error", "图书编号为空！！！！");
            return;
        }
        FileReader fileReader = null;
        Book newBook = null;
        try {
            fileReader = new FileReader("src/main/java/com/py/javaf1/Data/BookData");
            BufferedReader reader = new BufferedReader(fileReader);
            String line;
            while ((line = reader.readLine()) != null) {
                String[] bookDetails = line.split(",");
                if (bookDetails.length == 8) {
                    String fileBookId = bookDetails[0];
                    if (bookID.equals(fileBookId)) {
                        newBook = new Book(bookID, bookDetails[1], bookDetails[2], bookDetails[3],
                                Integer.parseInt(bookDetails[4]), Integer.parseInt(bookDetails[5]), bookDetails[6],
                                Integer.parseInt(bookDetails[7]));
                        break;
                    }
                }
            }
            if (Objects.isNull(newBook)) {
                showAlert("Search Error", "查找不到这个图书信息！！！！");
                return;
            }
            showDetail.setText("图书编号：" + newBook.getBookID() + " "
                    + "书名：" + newBook.getTitle() + " "
                    + "作者：" + newBook.getAuthor() + " ");
            showDetail1.setText("出版社：" + newBook.getPublisher() + " " +
                    "总册数：" + newBook.getTotalCopies() + " " +
                    "现存地址：" + newBook.getLocation());
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

    public void handleReturnAdminMain(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/com/py/javaf1/View" +
                    "/AdminMainView.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 400, 320);
            Stage stage = (Stage) bookIDField.getScene().getWindow();
            stage.setTitle("AdminMain");
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
