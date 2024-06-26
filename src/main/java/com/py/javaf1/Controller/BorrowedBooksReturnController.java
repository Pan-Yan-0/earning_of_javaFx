package com.py.javaf1.Controller;

import com.py.javaf1.domain.Book;
import com.py.javaf1.domain.Reader;
import com.py.javaf1.domain.BorrowRecord;
import com.py.javaf1.domain.BorrowRecordReturnWrapper;
import com.py.javaf1.domain.BorrowRecordWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BorrowedBooksReturnController {
    @FXML
    private TableView<BorrowRecordReturnWrapper> borrowedBooksTable; // 表格视图控件，用于显示借阅的书籍记录
    @FXML
    private TableColumn<BorrowRecordReturnWrapper, String> bookIDColumn; // 图书编号列
    @FXML
    private TableColumn<BorrowRecordReturnWrapper, String> titleColumn; // 图书名称列
    @FXML
    private TableColumn<BorrowRecordReturnWrapper, String> authorColumn; // 作者列
    @FXML
    private TableColumn<BorrowRecordReturnWrapper, String> borrowDateColumn; // 借阅日期列
    @FXML
    private TableColumn<BorrowRecordReturnWrapper, Button> returnButtonColumn; // 归还按钮列

    private ObservableList<BorrowRecordReturnWrapper> borrowedBooksData = FXCollections.observableArrayList(); // 存储借阅书籍数据的列表

    @FXML
    private void initialize() {
        setupTableColumns(); // 设置表格列的单元格值工厂
        loadBorrowedBooksData(); // 加载已借阅书籍数据
    }

    private void setupTableColumns() {
        bookIDColumn.setCellValueFactory(cellData -> cellData.getValue().bookIDProperty());
        titleColumn.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        authorColumn.setCellValueFactory(cellData -> cellData.getValue().authorProperty());
        borrowDateColumn.setCellValueFactory(cellData -> cellData.getValue().borrowDateProperty());
        returnButtonColumn.setCellValueFactory(new PropertyValueFactory<>("returnButton"));
    }

    private void loadBorrowedBooksData() {
        List<BorrowRecordReturnWrapper> records = new ArrayList<>();
        String currentReaderID = LoginController.readerId; // 获取当前登录的读者ID
        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/java/com/py/javaf1/Data/BorrowRecord"))) {
            String line;
            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
            while ((line = reader.readLine()) != null) {
                String[] details = line.split(",");
                if (details.length >= 3 && details[0].equals(currentReaderID)) {
                    String readerID = details[0];
                    String bookID = details[1];
                    String borrowDateString = details[2];
                    String returnDateString = details.length == 4 ? details[3] : "未归还";
                    if (!"未归还".equals(returnDateString)) {
                        continue; // 跳过已归还的书籍
                    }
                    // 查找书籍信息
                    Book book = findBookByID(bookID);
                    if (book != null) {
                        Button returnButton = new Button("归还");
                        returnButton.setOnAction(event -> returnBook(bookID, borrowDateString));
                        records.add(new BorrowRecordReturnWrapper(readerID, bookID, book.getTitle(), book.getAuthor(), borrowDateString, returnDateString, returnButton));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        borrowedBooksData.setAll(records);
        borrowedBooksTable.setItems(borrowedBooksData); // 将借阅书籍数据设置到表格视图中
    }

    private Book findBookByID(String bookID) {
        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/java/com/py/javaf1/Data/BookData"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] details = line.split(",");
                if (details.length >= 8 && details[0].equals(bookID)) {
                    return new Book(details[0], details[1], details[2], details[3], Integer.parseInt(details[4]),
                            Integer.parseInt(details[5]), details[6], Integer.parseInt(details[7]));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void returnBook(String bookID, String borrowDateString) {
        String currentReaderID = LoginController.readerId;
        // 更新 BorrowRecord 文件
        List<BorrowRecord> borrowRecords = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/java/com/py/javaf1/Data/BorrowRecord"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] details = line.split(",");
                if (details.length >= 3) {
                    String borrowDate = details[2];
                    String returnDate = details.length == 4 ? details[3] : null;
                    if (details[0].equals(currentReaderID) && details[1].equals(bookID) && details[2].equals(borrowDateString)) {
                        returnDate = new Date().toString(); // 更新归还日期为当前日期
                    }
                    borrowRecords.add(new BorrowRecord(details[0], details[1], borrowDate, returnDate));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/java/com/py/javaf1/Data/BorrowRecord"))) {
            for (BorrowRecord record : borrowRecords) {
                writer.write(record.getReaderID() + "," + record.getBookID() + "," + record.getBorrowDate());
                if (record.getReturnDate() != null) {
                    writer.write("," + record.getReturnDate());
                }
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 更新 BookData 文件
        List<Book> books = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/java/com/py/javaf1/Data/BookData"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] details = line.split(",");
                if (details.length >= 8) {
                    Book book = new Book(details[0], details[1], details[2], details[3], Integer.parseInt(details[4]),
                            Integer.parseInt(details[5]), details[6], Integer.parseInt(details[7]));
                    if (book.getBookID().equals(bookID)) {
                        book.setAvailableCopies(book.getAvailableCopies() + 1); // 增加可借册数
                    }
                    books.add(book);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/java/com/py/javaf1/Data/BookData"))) {
            for (Book book : books) {
                writer.write(book.getBookID() + "," + book.getTitle() + "," + book.getAuthor() + "," + book.getPublisher() + "," +
                        book.getTotalCopies() + "," + book.getAvailableCopies() + "," + book.getLocation() + "," + book.getBorrowCount());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 更新 ReaderData 文件
        List<Reader> readers = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/java/com/py/javaf1/Data/ReaderData"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] details = line.split(",");
                if (details.length >= 7) {
                    Reader readerData = new Reader(details[0], details[1], details[2], details[3], Integer.parseInt(details[4]),
                            Integer.parseInt(details[5]), Double.parseDouble(details[6]));
                    if (readerData.getReaderID().equals(currentReaderID)) {
                        readerData.setBorrowLimit(readerData.getBorrowLimit() + 1); // 增加可借数量
                    }
                    readers.add(readerData);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/java/com/py/javaf1/Data/ReaderData"))) {
            for (Reader readerData : readers) {
                writer.write(readerData.getName() + "," + readerData.getPassword() + "," + readerData.getReaderID() + "," +
                        readerData.getStudentID() + "," + readerData.getBorrowLimit() + "," + readerData.getHasOverdue() + "," +
                        readerData.getHasDebt());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        showAlert("成功", "归还书籍成功！"); // 显示归还成功的提示
        // 重新加载借阅数据以刷新表格
        loadBorrowedBooksData();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
