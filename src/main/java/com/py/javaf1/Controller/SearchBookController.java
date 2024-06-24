package com.py.javaf1.Controller;

import com.py.javaf1.domain.Book;
import com.py.javaf1.domain.BookWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SearchBookController {

    @FXML
    private TextField searchField;
    @FXML
    private ComboBox<String> attributeComboBox;
    @FXML
    private TableView<BookWrapper> bookTable;
    @FXML
    private TableColumn<BookWrapper, String> idColumn;
    @FXML
    private TableColumn<BookWrapper, String> titleColumn;
    @FXML
    private TableColumn<BookWrapper, String> authorColumn;
    @FXML
    private TableColumn<BookWrapper, String> publisherColumn;
    @FXML
    private TableColumn<BookWrapper, Integer> totalCopiesColumn;
    @FXML
    private TableColumn<BookWrapper, Integer> availableCopiesColumn;
    @FXML
    private TableColumn<BookWrapper, String> locationColumn;
    @FXML
    private TableColumn<BookWrapper, Integer> borrowCountColumn;

    private final ObservableList<BookWrapper> bookData = FXCollections.observableArrayList();

    public void initialize() {
        setupTableColumns();
        loadBook();
        attributeComboBox.getSelectionModel().selectFirst(); // 默认选择第一个属性
    }

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
        setBookData(books);
    }

    @FXML
    private void handleSearch() {
        String keyword = searchField.getText().trim().toLowerCase();
        String selectedAttribute = attributeComboBox.getValue();

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

        bookTable.setItems(FXCollections.observableArrayList(filteredBooks));
    }

    @FXML
    private void handleShowAll() {
        bookTable.setItems(bookData);
    }

    public void setBookData(List<Book> books) {
        bookData.clear();
        for (Book book : books) {
            bookData.add(new BookWrapper(book));
        }
        bookTable.setItems(bookData);
    }
}


