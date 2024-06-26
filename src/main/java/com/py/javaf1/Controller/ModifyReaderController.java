package com.py.javaf1.Controller;

import com.py.javaf1.HelloApplication;
import com.py.javaf1.domain.Reader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ModifyReaderController {
    private final List<Reader> readerList = new ArrayList<>(); // 读者列表

    @FXML
    private Label nowName; // 当前读者姓名标签
    @FXML
    private Label nowStudentID; // 当前读者学号标签
    @FXML
    private Label nowBorrowLimit; // 当前读者可借数量标签
    @FXML
    private Label nowOverdue; // 当前读者超期未还数量标签
    @FXML
    private Label nowDebt; // 当前读者欠费金额标签
    @FXML
    private TextField readerIDField; // 读者编号输入框
    @FXML
    private TextField nameField; // 姓名输入框
    @FXML
    private TextField studentIDField; // 学号输入框
    @FXML
    private TextField borrowLimitField; // 可借数量输入框
    @FXML
    private TextField overdueField; // 超期未还数量输入框
    @FXML
    private TextField debtField; // 欠费金额输入框
    private Reader currentReader; // 当前读者对象

    @FXML
    private void initialize() {
        loadReaderData(); // 初始化时加载读者数据
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
        // 清空当前标签内容
        nowName.setText("");
        nowStudentID.setText("");
        nowBorrowLimit.setText("");
        nowOverdue.setText("");
        nowDebt.setText("");

        String readerID = readerIDField.getText().trim(); // 获取输入的读者编号
        currentReader = findReaderByID(readerID); // 查找对应读者

        if (currentReader != null) {
            // 显示查找到的读者信息
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
        // 清空当前标签内容
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

        // 更新可借数量、超期未还数量和欠费金额
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
            showAlert("修改失败", "没有要修改的字段！");
            return;
        }

        saveReaderData(); // 保存更新后的读者数据
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

    @FXML
    public void handleReturnAdminMain() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/com/py/javaf1/View/AdminMainView.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 400, 320);
            Stage stage = (Stage) nowName.getScene().getWindow();
            stage.setTitle("AdminMain");
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
