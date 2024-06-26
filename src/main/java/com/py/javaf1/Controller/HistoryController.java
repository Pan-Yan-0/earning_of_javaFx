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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class HistoryController {

    @FXML
    private TableView<BorrowRecordWrapper> historyTable; // 表格视图控件，用于显示借阅记录
    @FXML
    private TableColumn<BorrowRecordWrapper, String> readerIDColumn; // 读者编号列
    @FXML
    private TableColumn<BorrowRecordWrapper, String> bookIDColumn; // 图书编号列
    @FXML
    private TableColumn<BorrowRecordWrapper, String> borrowDateColumn; // 借阅时间列
    @FXML
    private TableColumn<BorrowRecordWrapper, String> returnDateColumn; // 归还时间列

    private ObservableList<BorrowRecordWrapper> borrowData = FXCollections.observableArrayList(); // 存储借阅记录数据的列表

    @FXML
    private void initialize() {
        setupTableColumns(); // 设置表格列的单元格值工厂
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
        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/java/com/py/javaf1/Data/BorrowRecord"))) {
            String line;
            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
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

        filterBorrowRecords(records); // 过滤借阅记录，仅显示当前读者的借阅记录
    }

    private void filterBorrowRecords(List<BorrowRecordWrapper> records) {
        String currentReaderID = LoginController.readerId; // 假设 LoginController 中存储了当前登录用户的 readerId
        List<BorrowRecordWrapper> filteredRecords = new ArrayList<>();
        for (BorrowRecordWrapper record : records) {
            if (record.getReaderID().equals(currentReaderID)) {
                filteredRecords.add(record);
            }
        }
        borrowData.setAll(filteredRecords); // 更新表格视图中的数据
        historyTable.setItems(borrowData);
    }
}
