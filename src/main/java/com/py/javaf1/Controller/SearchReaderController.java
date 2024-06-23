package com.py.javaf1.Controller;

import com.py.javaf1.domain.Reader;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SearchReaderController {

    @FXML
    private ComboBox<String> searchTypeComboBox;
    @FXML
    private ComboBox<String> attributeComboBox;
    @FXML
    private TextField attributeValueField;
    @FXML
    private CheckBox overdueCheckBox;
    @FXML
    private CheckBox debtCheckBox;
    @FXML
    private TableView<Reader> readerTable;
    @FXML
    private TableColumn<Reader, Integer> indexColumn;
    @FXML
    private TableColumn<Reader, String> readerIDColumn;
    @FXML
    private TableColumn<Reader, String> nameColumn;
    @FXML
    private TableColumn<Reader, String> studentIDColumn;
    @FXML
    private TableColumn<Reader, Integer> borrowLimitColumn;
    @FXML
    private TableColumn<Reader, String> overdueColumn;
    @FXML
    private TableColumn<Reader, String> debtColumn;
    @FXML
    private TableColumn<Reader, Integer> overdueCountColumn;
    @FXML
    private TableColumn<Reader, Double> debtAmountColumn;

    private ObservableList<Reader> readerData = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        searchTypeComboBox.setItems(FXCollections.observableArrayList("模糊查找", "全体查找"));
        searchTypeComboBox.setValue("模糊查找");

        attributeComboBox.setItems(FXCollections.observableArrayList("读者编号", "姓名", "学号", "可借数量", "是否有超期未还", "是否有欠费"));
        attributeComboBox.setValue("读者编号");

        indexColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(readerTable.getItems().indexOf(cellData.getValue()) + 1));
        readerIDColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getReaderID()));
        nameColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getName()));
        studentIDColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getStudentID()));
        borrowLimitColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getBorrowLimit()));
        overdueColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getHasOverdue() > 0 ? "是" : "否"));
        debtColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getHasDebt() > 0 ? "是" : "否"));
        overdueCountColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getHasOverdue()));
        debtAmountColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getHasDebt()));

        readerTable.setItems(readerData);
    }

    @FXML
    private void handleSearchReader() {
        readerData.clear();
        String searchType = searchTypeComboBox.getValue();
        String attribute = attributeComboBox.getValue();
        String attributeValue = attributeValueField.getText().trim().toLowerCase();
        boolean overdue = overdueCheckBox.isSelected();
        boolean debt = debtCheckBox.isSelected();

        List<Reader> matchingReaders = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/java/com/py/javaf1/Data/ReaderData"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] details = line.split(",");
                if (searchType.equals("模糊查找")) {
                    if (attributeMatches(details, attribute, attributeValue, overdue, debt)) {
                        matchingReaders.add(createReader(details));
                    }
                } else {
                    matchingReaders.add(createReader(details));
                }
            }
        } catch (IOException e) {
            showAlert("错误", "读取文件时发生错误！");
            return;
        }

        if (matchingReaders.isEmpty()) {
            showAlert("查找失败", "没有找到符合条件的读者！");
        } else {
            readerData.addAll(matchingReaders);
        }
    }

    private boolean attributeMatches(String[] details, String attribute, String attributeValue, boolean overdue, boolean debt) {
        switch (attribute) {
            case "读者编号":
                return details[2].toLowerCase().contains(attributeValue);
            case "姓名":
                return details[0].toLowerCase().contains(attributeValue);
            case "学号":
                return details[3].toLowerCase().contains(attributeValue);
            case "可借数量":
                return details[4].toLowerCase().contains(attributeValue);
            case "是否有超期未还":
                return (overdue && Integer.parseInt(details[5]) > 0) || (!overdue && Integer.parseInt(details[5]) == 0);
            case "是否有欠费":
                return (debt && Double.parseDouble(details[6]) > 0) || (!debt && Double.parseDouble(details[6]) == 0);
            default:
                return false;
        }
    }

    private Reader createReader(String[] details) {
        return new Reader(details[0], "", details[2], details[3], Integer.parseInt(details[4]),
                Integer.parseInt(details[5]), Double.parseDouble(details[6]));
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

}
