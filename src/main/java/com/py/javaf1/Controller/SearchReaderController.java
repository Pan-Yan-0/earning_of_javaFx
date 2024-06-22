package com.py.javaf1.Controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/*
* @TODO 这里要加几个
* 查找欠费的人信息什么的
* */
public class SearchReaderController {
    @FXML
    private ComboBox<String> searchTypeComboBox;
    @FXML
    private ComboBox<String> attributeComboBox;
    @FXML
    private TextField attributeValueField;
    @FXML
    private TextArea resultArea;

    @FXML
    private void initialize() {
        searchTypeComboBox.setItems(FXCollections.observableArrayList("按属性查找", "全体查找"));
        searchTypeComboBox.setValue("按属性查找");

        attributeComboBox.setItems(FXCollections.observableArrayList("读者编号", "姓名", "学号", "可借数量", "是否有超期未还", "是否有欠费"));
        attributeComboBox.setValue("读者编号");
    }

    @FXML
    private void handleSearchReader() {
        resultArea.clear();
        String searchType = searchTypeComboBox.getValue();
        String attribute = attributeComboBox.getValue();
        String attributeValue = attributeValueField.getText();


        List<String[]> matchingReaders = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/java/com/py/javaf1/Data/ReaderData"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] details = line.split(",");
                if (searchType.equals("按属性查找")) {
                    if (attributeMatches(details, attribute, attributeValue)) {
                        matchingReaders.add(details);
                    }
                } else {
                    matchingReaders.add(details);
                }
            }
        } catch (IOException e) {
            showAlert("错误", "读取文件时发生错误！");
            return;
        }

        if (matchingReaders.isEmpty()) {
            showAlert("查找失败", "没有找到符合条件的读者！");
        } else {
            for (String[] details : matchingReaders) {
                resultArea.appendText("读者编号: " + details[0] + "\n");
                resultArea.appendText("姓名: " + details[1] + "\n");
                resultArea.appendText("学号: " + details[2] + "\n");
                resultArea.appendText("可借数量: " + details[3] + "\n");
                resultArea.appendText("是否有超期未还: " + details[4] + "\n");
                resultArea.appendText("是否有欠费: " + details[5] + "\n\n");
            }
        }
    }

    private boolean attributeMatches(String[] details, String attribute, String attributeValue) {
        switch (attribute) {
            case "读者编号":
                return details[0].equalsIgnoreCase(attributeValue);
            case "姓名":
                return details[1].equalsIgnoreCase(attributeValue);
            case "学号":
                return details[2].equalsIgnoreCase(attributeValue);
            case "可借数量":
                return details[3].equals(attributeValue);
            case "是否有超期未还":
                return details[4].equalsIgnoreCase(attributeValue);
            case "是否有欠费":
                return details[5].equalsIgnoreCase(attributeValue);
            default:
                return false;
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
