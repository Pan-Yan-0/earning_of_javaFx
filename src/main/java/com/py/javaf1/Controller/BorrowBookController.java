package com.py.javaf1.Controller;

import com.py.javaf1.domain.Book;
import com.py.javaf1.domain.BookWrapper;
import com.py.javaf1.domain.SelectableBookWrapper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;

import java.util.List;
import java.util.stream.Collectors;

public class BorrowBookController {

    @FXML
    private TableView<SelectableBookWrapper> bookTable;
    @FXML
    private TableColumn<SelectableBookWrapper, String> bookIDColumn;
    @FXML
    private TableColumn<SelectableBookWrapper, String> titleColumn;
    @FXML
    private TableColumn<SelectableBookWrapper, String> authorColumn;
    @FXML
    private TableColumn<SelectableBookWrapper, String> publisherColumn;
    @FXML
    private TableColumn<SelectableBookWrapper, Integer> totalCopiesColumn;
    @FXML
    private TableColumn<SelectableBookWrapper, Integer> availableCopiesColumn;
    @FXML
    private TableColumn<SelectableBookWrapper, String> locationColumn;
    @FXML
    private TableColumn<SelectableBookWrapper, Integer> borrowCountColumn;
    @FXML
    private TableColumn<SelectableBookWrapper, Boolean> selectColumn;

    private List<BookWrapper> bookData;

    public void setBookData(List<BookWrapper> bookData) {
        this.bookData = bookData;
        List<SelectableBookWrapper> wrappedBooks = bookData.stream()
                .map(SelectableBookWrapper::new)
                .collect(Collectors.toList());
        bookTable.setItems(FXCollections.observableArrayList(wrappedBooks));
    }

    @FXML
    private void initialize() {
        bookIDColumn.setCellValueFactory(cellData -> cellData.getValue().getBook().bookIDProperty());
        titleColumn.setCellValueFactory(cellData -> cellData.getValue().getBook().titleProperty());
        authorColumn.setCellValueFactory(cellData -> cellData.getValue().getBook().authorProperty());
        publisherColumn.setCellValueFactory(cellData -> cellData.getValue().getBook().publisherProperty());
        totalCopiesColumn.setCellValueFactory(cellData -> cellData.getValue().getBook().totalCopiesProperty().asObject());
        availableCopiesColumn.setCellValueFactory(cellData -> cellData.getValue().getBook().availableCopiesProperty().asObject());
        locationColumn.setCellValueFactory(cellData -> cellData.getValue().getBook().locationProperty());
        borrowCountColumn.setCellValueFactory(cellData -> cellData.getValue().getBook().borrowCountProperty().asObject());
        selectColumn.setCellValueFactory(cellData -> cellData.getValue().selectedProperty());
        selectColumn.setCellFactory(CheckBoxTableCell.forTableColumn(selectColumn));
    }

    @FXML
    private void handleBorrowSelectedBooks() {
        List<BookWrapper> selectedBooks = bookTable.getItems().stream()
                .filter(SelectableBookWrapper::isSelected)
                .map(SelectableBookWrapper::getBook)
                .collect(Collectors.toList());

        // 执行借阅逻辑，比如更新数据库或本地数据

        // 清除选中状态
        bookTable.getItems().forEach(wrapper -> wrapper.setSelected(false));
    }
}
