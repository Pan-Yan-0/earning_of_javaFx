package com.py.javaf1.Controller;

import com.py.javaf1.domain.Book;
import com.py.javaf1.domain.BookWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.util.List;
import java.util.stream.Collectors;

public class SearchBookController {

    @FXML
    private TextField searchField;
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

    @FXML
    private void handleSearch() {
        String keyword = searchField.getText().trim().toLowerCase();
        List<BookWrapper> filteredBooks = bookData.stream()
                .filter(bookWrapper ->
                        bookWrapper.getBook().getTitle().toLowerCase().contains(keyword) ||
                                bookWrapper.getBook().getAuthor().toLowerCase().contains(keyword) ||
                                bookWrapper.getBook().getPublisher().toLowerCase().contains(keyword))
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
