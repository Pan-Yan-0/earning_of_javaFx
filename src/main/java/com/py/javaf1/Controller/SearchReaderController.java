package com.py.javaf1.Controller;

import com.py.javaf1.HelloApplication;
import com.py.javaf1.domain.Reader;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.stage.Stage;
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
    private ComboBox<String> searchTypeComboBox; // 搜索类型下拉框
    @FXML
    private ComboBox<String> attributeComboBox; // 属性下拉框
    @FXML
    private TextField attributeValueField; // 属性值文本框
    @FXML
    private CheckBox overdueCheckBox; // 是否有超期未还复选框
    @FXML
    private CheckBox debtCheckBox; // 是否有欠费复选框
    @FXML
    private TableView<Reader> readerTable; // 读者表格
    @FXML
    private TableColumn<Reader, Integer> indexColumn; // 编号列
    @FXML
    private TableColumn<Reader, String> readerIDColumn; // 读者编号列
    @FXML
    private TableColumn<Reader, String> nameColumn; // 姓名列
    @FXML
    private TableColumn<Reader, String> studentIDColumn; // 学号列
    @FXML
    private TableColumn<Reader, Integer> borrowLimitColumn; // 可借数量列
    @FXML
    private TableColumn<Reader, String> overdueColumn; // 是否有超期未还列
    @FXML
    private TableColumn<Reader, String> debtColumn; // 是否有欠费列
    @FXML
    private TableColumn<Reader, Integer> overdueCountColumn; // 超期未还数量列
    @FXML
    private TableColumn<Reader, Double> debtAmountColumn; // 欠费金额列

    private ObservableList<Reader> readerData = FXCollections.observableArrayList(); // 读者数据

    @FXML
    private void initialize() {
        // 初始化搜索类型下拉框
        searchTypeComboBox.setItems(FXCollections.observableArrayList("模糊查找", "全体查找"));
        searchTypeComboBox.setValue("模糊查找");

        // 初始化属性下拉框
        attributeComboBox.setItems(FXCollections.observableArrayList("读者编号", "姓名", "学号", "可借数量", "是否有超期未还", "是否有欠费"));
        attributeComboBox.setValue("读者编号");

        // 设置表格列的单元格值工厂
        indexColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(readerTable.getItems().indexOf(cellData.getValue()) + 1));
        readerIDColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getReaderID()));
        nameColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getName()));
        studentIDColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getStudentID()));
        borrowLimitColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getBorrowLimit()));
        overdueColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getHasOverdue() > 0 ? "是" : "否"));
        debtColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getHasDebt() > 0 ? "是" : "否"));
        overdueCountColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getHasOverdue()));
        debtAmountColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getHasDebt()));

        // 设置表格数据
        readerTable.setItems(readerData);
    }

    @FXML
    private void handleSearchReader() {
        // 清除表格数据
        readerData.clear();
        String searchType = searchTypeComboBox.getValue(); // 获取搜索类型
        String attribute = attributeComboBox.getValue(); // 获取属性
        String attributeValue = attributeValueField.getText().trim().toLowerCase(); // 获取属性值并转换为小写
        boolean overdue = overdueCheckBox.isSelected(); // 是否勾选超期未还
        boolean debt = debtCheckBox.isSelected(); // 是否勾选欠费

        List<Reader> matchingReaders = new ArrayList<>(); // 匹配的读者列表

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

    @FXML
    public void handleReturnAdminMain() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/com/py/javaf1/View/AdminMainView.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 800, 430);
            Stage stage = (Stage) debtCheckBox.getScene().getWindow();
            stage.setTitle("AdminMain");
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
