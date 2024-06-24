package com.py.javaf1.Controller;

import com.py.javaf1.domain.BorrowRecord;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class BorrowedBooksController {

    @FXML
    private TableView<BorrowRecord> borrowedBooksTable;
    @FXML
    private TableColumn<BorrowRecord, String> bookIDColumn;
    @FXML
    private TableColumn<BorrowRecord, String> titleColumn;
    @FXML
    private TableColumn<BorrowRecord, String> authorColumn;
    @FXML
    private TableColumn<BorrowRecord, String> borrowDateColumn;

    @FXML
    private void initialize() {
        bookIDColumn.setCellValueFactory(new PropertyValueFactory<>("bookID"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        borrowDateColumn.setCellValueFactory(new PropertyValueFactory<>("borrowDate"));

        // 假设已经有当前读者编号
        String currentReaderID = "123"; // 这个需要从实际登录的用户中获取
        borrowedBooksTable.setItems(FXCollections.observableArrayList(BorrowRecordData.getBorrowedBooks(currentReaderID)));
    }
}
