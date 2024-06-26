package com.py.javaf1.Controller;

import com.py.javaf1.HelloApplication;
import com.py.javaf1.domain.Book;
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

public class EditBookController {
    @FXML
    private Label showResult;          // 显示查询结果
    @FXML
    private Label nowBook;             // 当前书名
    @FXML
    private Label nowAuthor;           // 当前作者
    @FXML
    private Label nowPublisher;        // 当前出版社
    @FXML
    private Label nowTotalCopies;      // 当前总册数
    @FXML
    private Label nowLocation;         // 当前现存地址
    @FXML
    private TextField bookIDField;     // 图书编号输入框
    @FXML
    private TextField titleField;      // 书名输入框
    @FXML
    private TextField authorField;     // 作者输入框
    @FXML
    private TextField publisherField;  // 出版社输入框
    @FXML
    private TextField totalCopiesField;// 总册数输入框
    @FXML
    private TextField locationField;   // 现存地址输入框

    // 处理修改书籍的逻辑
    @FXML
    public void handleEditBook() {
        String bookID = bookIDField.getText();           // 获取图书编号
        String title = titleField.getText();             // 获取书名
        String author = authorField.getText();           // 获取作者
        String publisher = publisherField.getText();     // 获取出版社
        String totalCopies = totalCopiesField.getText(); // 获取总册数
        String location = locationField.getText();       // 获取现存地址

        // 判断图书编号是否为空
        if (bookID.isEmpty()) {
            showAlert("Search Error", "图书编号为空！！！！");
            return;
        }

        // 判断是否有要修改的字段
        if (title.isEmpty() && author.isEmpty() && publisher.isEmpty() && totalCopies.isEmpty() && location.isEmpty()) {
            showAlert("EditBook Error", "没有任何要修改的字段！！！！");
            return;
        }

        ArrayList<Book> books = new ArrayList<>();
        try {
            // 读取图书数据文件
            FileReader fileReader = new FileReader("src/main/java/com/py/javaf1/Data/BookData");
            BufferedReader reader = new BufferedReader(fileReader);
            String line;
            boolean change = false;

            // 逐行读取文件
            while ((line = reader.readLine()) != null) {
                String[] bookDetails = line.split(",");
                Book newBook = new Book();
                if (bookDetails.length == 8) {
                    String fileBookId = bookDetails[0];
                    newBook = new Book(fileBookId, bookDetails[1], bookDetails[2], bookDetails[3],
                            Integer.parseInt(bookDetails[4]), Integer.parseInt(bookDetails[5]), bookDetails[6],
                            Integer.parseInt(bookDetails[7]));

                    // 修改匹配到的图书信息
                    if (bookID.equals(fileBookId)) {
                        if (!title.isEmpty()){
                            newBook.setTitle(title);
                        }
                        if (!author.isEmpty()){
                            newBook.setAuthor(author);
                        }
                        if (!publisher.isEmpty()){
                            newBook.setPublisher(publisher);
                        }
                        /*
                         * @TODO 此处还需加逻辑处理在馆册数
                         * */
                        if (!totalCopies.isEmpty()){
                            newBook.setTotalCopies(Integer.parseInt(totalCopies));
                        }
                        if (!location.isEmpty()){
                            newBook.setLocation(location);
                        }
                        change = true;
                    }
                }
                books.add(newBook);
            }

            // 如果没有找到匹配的图书
            if (!change){
                showAlert("Search Error", "查找不到这个图书信息！！！！");
                return;
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // 将修改后的图书信息写回文件
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/java/com/py/javaf1/Data/BookData", false))) {
            for (Book newBook : books) {
                writer.write(newBook.getBookID() + "," + newBook.getTitle() + "," + newBook.getAuthor() + "," + newBook.getPublisher() + "," + newBook.getTotalCopies() + "," + newBook.getAvailableCopies() + "," + newBook.getLocation() + "," + newBook.getBorrowCount());
                writer.newLine();
            }
            showAlert("修改书籍成功", "书籍已成功修改！");
            showResult.setText("");
            nowBook.setText("");
            nowAuthor.setText("");
            nowPublisher.setText("");
            nowTotalCopies.setText("");
            nowLocation.setText("");
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

    // 处理搜索书籍的逻辑
    @FXML
    public void handleSearchBook() {
        String bookID = bookIDField.getText();   // 获取图书编号
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

            // 逐行读取文件
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
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // 如果没有找到匹配的图书
        if (Objects.isNull(newBook)) {
            showAlert("Search Error", "查找不到这个图书信息！！！！");
            return;
        }

        // 显示查询结果
        showResult.setText("查询结果");
        nowBook.setText(newBook.getTitle());
        nowAuthor.setText(newBook.getAuthor());
        nowPublisher.setText(newBook.getPublisher());
        nowTotalCopies.setText("总册数：" + newBook.getTotalCopies() + " 在馆数：" + newBook.getAvailableCopies());
        nowLocation.setText(newBook.getLocation());
    }

    // 返回管理员主界面
    @FXML
    public void handleReturnAdminMain() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/com/py/javaf1/View/AdminMainView.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 400, 320);
            Stage stage = (Stage) bookIDField.getScene().getWindow();
            stage.setTitle("AdminMain");
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 显示提示信息
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
