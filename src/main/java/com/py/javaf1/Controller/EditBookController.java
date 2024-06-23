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
    private Label       showResult;
    @FXML
    private Label           nowBook;
    @FXML
    private Label           nowAuthor;
    @FXML
    private Label           nowPublisher;
    @FXML
    private Label           nowTotalCopies;
    @FXML
    private Label       nowLocation;
    @FXML
    private TextField bookIDField;
    @FXML
    private TextField titleField;
    @FXML
    private TextField authorField;
    @FXML
    private TextField publisherField;
    @FXML
    private TextField totalCopiesField;
    @FXML
    private TextField locationField;

    @FXML
    public void handleEditBook() {
        String bookID = bookIDField.getText();
        String title = titleField.getText();
        String author = authorField.getText();
        String publisher = publisherField.getText();
        String totalCopies = totalCopiesField.getText();
        String location = locationField.getText();
        if (bookID.isEmpty()) {
            showAlert("Search Error", "图书编号为空！！！！");
            return;
        }
        if (title.isEmpty() && author.isEmpty() && publisher.isEmpty() && totalCopies.isEmpty() && location.isEmpty()) {
            showAlert("EditBook Error", "没有任何要修改的字段！！！！");
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
                Book newBook = new Book();
                if (bookDetails.length == 8) {
                    String fileBookId = bookDetails[0];
                    newBook = new Book(fileBookId, bookDetails[1], bookDetails[2], bookDetails[3],
                            Integer.parseInt(bookDetails[4]), Integer.parseInt(bookDetails[5]), bookDetails[6],
                            Integer.parseInt(bookDetails[7]));
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
                        * @TODO 此处还需加逻辑
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
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (Objects.isNull(newBook)) {
            showAlert("Search Error", "查找不到这个图书信息！！！！");
            return;
        }
        showResult.setText("查询结果");
        nowBook.setText(newBook.getTitle());
        nowAuthor.setText(newBook.getAuthor());
        nowPublisher.setText(newBook.getPublisher());
        nowTotalCopies.setText("总册数：" + newBook.getTotalCopies() + " 在馆数：" + newBook.getAvailableCopies());
        nowLocation.setText(newBook.getLocation());
    }

    @FXML
    public void handleReturnAdminMain() {
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

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
