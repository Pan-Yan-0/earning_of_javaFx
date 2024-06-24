package com.py.javaf1.Controller;

import com.py.javaf1.domain.Reader;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ModifyReaderController {
    private final List<Reader> readerList = new ArrayList<>();
    @FXML
    private Label nowName;
    @FXML
    private Label nowStudentID;
    @FXML
    private Label nowBorrowLimit;
    @FXML
    private Label nowOverdue;
    @FXML
    private Label nowDebt;
    @FXML
    private TextField readerIDField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField studentIDField;
    @FXML
    private TextField borrowLimitField;
    @FXML
    private TextField overdueField;
    @FXML
    private TextField debtField;
    private Reader currentReader;

    @FXML
    private void initialize() {
        loadReaderData();
    }

    private void loadReaderData() {
        try (BufferedReader reader =
                     new BufferedReader(new FileReader("src/main/java/com/py/javaf1/Data/ReaderData"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] details = line.split(",");
                readerList.add(new Reader(details[0], details[1], details[2], details[3], Integer.parseInt(details[4]),
                        Integer.parseInt(details[5]), Double.parseDouble(details[6])));
            }
        } catch (IOException e) {
            showAlert("错误", "读取文件时发生错误！");
        }
    }

    @FXML
    private void handleSearchReader() {
        nowName.setText("");
        nowStudentID.setText("");
        nowBorrowLimit.setText("");
        nowOverdue.setText("");
        nowDebt.setText("");
        String readerID = readerIDField.getText().trim();
        currentReader = findReaderByID(readerID);

        if (currentReader != null) {
            nowName.setText(currentReader.getName());
            nowStudentID.setText(currentReader.getStudentID());
            nowBorrowLimit.setText(String.valueOf(currentReader.getBorrowLimit()));
            nowOverdue.setText(String.valueOf(currentReader.getHasOverdue()));
            nowDebt.setText(String.valueOf(currentReader.getHasDebt()));
        } else {
            showAlert("查找失败", "没有找到对应的读者！");
        }
    }

    @FXML
    private void handleSaveChanges() {
        nowName.setText("");
        nowStudentID.setText("");
        nowBorrowLimit.setText("");
        nowOverdue.setText("");
        nowDebt.setText("");
        if (currentReader == null) {
            showAlert("错误", "请先查找读者！");
            return;
        }
        boolean change = false;
        // 姓名和学号不允许修改，所以不在这里更新这些字段
        if (!borrowLimitField.getText().isEmpty()){
            currentReader.setBorrowLimit(Integer.parseInt(borrowLimitField.getText().trim()));
            change = true;
        }
        if (!overdueField.getText().isEmpty()){
            currentReader.setHasOverdue(Integer.parseInt(overdueField.getText().trim()));
            change = true;
        }
        if (!debtField.getText().isEmpty()){
            currentReader.setHasDebt(Double.parseDouble(debtField.getText().trim()));
            change = true;
        }
        if (!change){
            showAlert("ModifyReaderInfo Error", "没有要修改的字段！！！！");
            return;
        }
        saveReaderData();
        showAlert("成功", "读者信息修改成功！");
    }

    private Reader findReaderByID(String readerID) {
        return readerList.stream().filter(reader -> reader.getReaderID().equals(readerID)).findFirst().orElse(null);
    }

    private void saveReaderData() {
        try (BufferedWriter writer =
                     new BufferedWriter(new FileWriter("src/main/java/com/py/javaf1/Data/ReaderData"))) {
            for (Reader reader : readerList) {
                writer.write(String.format("%s,%s,%s,%s,%d,%d,%.2f%n",
                        reader.getName(),
                        reader.getPassword(),
                        reader.getReaderID(),
                        reader.getStudentID(),
                        reader.getBorrowLimit(),
                        reader.getHasOverdue(),
                        reader.getHasDebt()));
            }
        } catch (IOException e) {
            showAlert("错误", "保存文件时发生错误！");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

}
