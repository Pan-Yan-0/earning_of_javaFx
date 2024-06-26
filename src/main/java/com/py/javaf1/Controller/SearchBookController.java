package com.py.javaf1.Controller;

import com.py.javaf1.HelloApplication;
import com.py.javaf1.domain.Book;
import com.py.javaf1.domain.BookWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SearchBookController {

    @FXML
    private TextField searchField; // 搜索关键字输入框
    @FXML
    private ComboBox<String> attributeComboBox; // 搜索属性下拉框
    @FXML
    private TableView<BookWrapper> bookTable; // 显示书籍信息的表格
    @FXML
    private TableColumn<BookWrapper, String> idColumn; // 书籍编号列
    @FXML
    private TableColumn<BookWrapper, String> titleColumn; // 书名列
    @FXML
    private TableColumn<BookWrapper, String> authorColumn; // 作者列
    @FXML
    private TableColumn<BookWrapper, String> publisherColumn; // 出版社列
    @FXML
    private TableColumn<BookWrapper, Integer> totalCopiesColumn; // 总共册数列
    @FXML
    private TableColumn<BookWrapper, Integer> availableCopiesColumn; // 在馆册数列
    @FXML
    private TableColumn<BookWrapper, String> locationColumn; // 现存地址列
    @FXML
    private TableColumn<BookWrapper, Integer> borrowCountColumn; // 借阅次数列

    private final ObservableList<BookWrapper> bookData = FXCollections.observableArrayList(); // 存储书籍数据的列表

    public void initialize() {
        setupTableColumns(); // 初始化表格列
        loadBook(); // 加载书籍数据
        attributeComboBox.getSelectionModel().selectFirst(); // 默认选择第一个属性
    }

    // 设置表格列的数据绑定
    private void setupTableColumns() {
        idColumn.setCellValueFactory(cellData -> cellData.getValue().bookIDProperty());
        titleColumn.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        authorColumn.setCellValueFactory(cellData -> cellData.getValue().authorProperty());
        publisherColumn.setCellValueFactory(cellData -> cellData.getValue().publisherProperty());
        totalCopiesColumn.setCellValueFactory(cellData -> cellData.getValue().totalCopiesProperty().asObject());
        availableCopiesColumn.setCellValueFactory(cellData -> cellData.getValue().availableCopiesProperty().asObject());
        locationColumn.setCellValueFactory(cellData -> cellData.getValue().locationProperty());
        borrowCountColumn.setCellValueFactory(cellData -> cellData.getValue().borrowCountProperty().asObject());
    }

    // 加载书籍数据
    private void loadBook() {
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
                }
                books.add(newBook);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        setBookData(books); // 设置书籍数据到表格中
    }

    // 处理搜索事件
    @FXML
    private void handleSearch() {
        String keyword = searchField.getText().trim().toLowerCase(); // 获取搜索关键字并转为小写
        String selectedAttribute = attributeComboBox.getValue(); // 获取选择的搜索属性

        // 根据关键字和属性进行书籍过滤
        List<BookWrapper> filteredBooks = bookData.stream()
                .filter(bookWrapper -> {
                    Book book = bookWrapper.getBook();
                    switch (selectedAttribute) {
                        case "书籍编号":
                            return book.getBookID().toLowerCase().contains(keyword);
                        case "书名":
                            return book.getTitle().toLowerCase().contains(keyword);
                        case "作者":
                            return book.getAuthor().toLowerCase().contains(keyword);
                        case "出版社":
                            return book.getPublisher().toLowerCase().contains(keyword);
                        default:
                            return false;
                    }
                })
                .collect(Collectors.toList());

        bookTable.setItems(FXCollections.observableArrayList(filteredBooks)); // 更新显示过滤后的书籍信息
    }

    // 处理显示所有书籍事件
    @FXML
    private void handleShowAll() {
        bookTable.setItems(bookData); // 显示所有书籍信息
    }

    // 设置书籍数据到表格中
    public void setBookData(List<Book> books) {
        bookData.clear();
        for (Book book : books) {
            bookData.add(new BookWrapper(book));
        }
        bookTable.setItems(bookData); // 更新显示所有书籍信息
    }

    // 处理返回管理员主界面事件
    @FXML
    public void handleReturnAdminMain() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/com/py/javaf1/View/AdminMainView.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 800, 430); // 创建新的场景
            Stage stage = (Stage) searchField.getScene().getWindow(); // 获取当前窗口
            stage.setTitle("AdminMain"); // 设置窗口标题
            stage.setScene(scene); // 切换场景到管理员主界面
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
