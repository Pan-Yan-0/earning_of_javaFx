package com.py.javaf1.Controller;

import com.py.javaf1.domain.Book;
import com.py.javaf1.domain.BookWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class BookRankController {
    @FXML
    private TableView<BookWrapper> rankingTable; // 表格视图控件，用于显示书籍排名
    @FXML
    private TableColumn<BookWrapper, String> bookIDColumn; // 书籍ID列
    @FXML
    private TableColumn<BookWrapper, String> titleColumn; // 书籍名称列
    @FXML
    private TableColumn<BookWrapper, String> authorColumn; // 作者列
    @FXML
    private TableColumn<BookWrapper, Number> borrowCountColumn; // 借阅次数列

    private ObservableList<BookWrapper> bookData = FXCollections.observableArrayList(); // 存储书籍数据的列表

    @FXML
    private void initialize() {
        setupTableColumns(); // 设置表格列的单元格值工厂
        loadBookData(); // 加载书籍数据
    }

    private void setupTableColumns() {
        bookIDColumn.setCellValueFactory(cellData -> cellData.getValue().bookIDProperty());
        titleColumn.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        authorColumn.setCellValueFactory(cellData -> cellData.getValue().authorProperty());
        borrowCountColumn.setCellValueFactory(cellData -> cellData.getValue().borrowCountProperty());
    }

    private void loadBookData() {
        List<Book> books = fetchBooksFromDataFile(); // 从数据文件中获取书籍数据
        books.sort(Comparator.comparingInt(Book::getBorrowCount).reversed()); // 按借阅次数降序排序书籍
        for (Book book : books) {
            bookData.add(new BookWrapper(book)); // 将每个书籍包装为 BookWrapper 并添加到数据列表中
        }
        rankingTable.setItems(bookData); // 将数据列表设置为表格视图的项目
    }

    private List<Book> fetchBooksFromDataFile() {
        List<Book> books = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/java/com/py/javaf1/Data/BookData"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] bookDetails = line.split(",");
                if (bookDetails.length == 8) {
                    String bookID = bookDetails[0];
                    String title = bookDetails[1];
                    String author = bookDetails[2];
                    String publisher = bookDetails[3];
                    int totalCopies = Integer.parseInt(bookDetails[4]);
                    int availableCopies = Integer.parseInt(bookDetails[5]);
                    String location = bookDetails[6];
                    int borrowCount = Integer.parseInt(bookDetails[7]);
                    books.add(new Book(bookID, title, author, publisher, totalCopies, availableCopies, location, borrowCount));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return books;
    }
}
