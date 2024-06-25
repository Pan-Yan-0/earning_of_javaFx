package com.py.javaf1.Controller;

import com.py.javaf1.domain.BorrowRecord;
import com.py.javaf1.domain.BorrowRecordWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HistoryController {

    @FXML
    private TableView<BorrowRecordWrapper> historyTable;
    @FXML
    private TableColumn<BorrowRecordWrapper, String> readerIDColumn;
    @FXML
    private TableColumn<BorrowRecordWrapper, String> bookIDColumn;
    @FXML
    private TableColumn<BorrowRecordWrapper, String> borrowDateColumn;
    @FXML
    private TableColumn<BorrowRecordWrapper, String> returnDateColumn;

    private ObservableList<BorrowRecordWrapper> borrowData = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        setupTableColumns();
        loadBorrowData(); // 加载借阅记录数据
    }

    private void setupTableColumns() {
        readerIDColumn.setCellValueFactory(cellData -> cellData.getValue().readerIDProperty());
        bookIDColumn.setCellValueFactory(cellData -> cellData.getValue().bookIDProperty());
        borrowDateColumn.setCellValueFactory(cellData -> cellData.getValue().borrowDateProperty());
        returnDateColumn.setCellValueFactory(cellData -> cellData.getValue().returnDateProperty());
    }

    private void loadBorrowData() {
        List<BorrowRecordWrapper> records = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/java/com/py/javaf1/Data/BorrowRecord.txt"))) {
            String line;
            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            while ((line = reader.readLine()) != null) {
                String[] details = line.split(",");
                if (details.length >= 3) {
                    String readerID = details[0];
                    String bookID = details[1];
                    String borrowDateString = details[2];
                    String returnDateString = details.length == 4 ? details[3] : "未归还";
                    records.add(new BorrowRecordWrapper(readerID, bookID, borrowDateString, returnDateString));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        filterBorrowRecords(records);
    }

    private void filterBorrowRecords(List<BorrowRecordWrapper> records) {
        String currentReaderID = LoginController.readerId; // 假设 LoginController 中存储了当前登录用户的 readerId
        List<BorrowRecordWrapper> filteredRecords = new ArrayList<>();
        for (BorrowRecordWrapper record : records) {
            if (record.getReaderID().equals(currentReaderID)) {
                filteredRecords.add(record);
            }
        }
        borrowData.setAll(filteredRecords);
        historyTable.setItems(borrowData);
    }
}
