package com.py.javaf1.Controller;

import com.py.javaf1.domain.Reader;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.*;

public class PersonalInfoController {

    @FXML
    private Label nameLabel; // 显示姓名的标签
    @FXML
    private Label studentIDLabel; // 显示学号的标签
    @FXML
    private Label borrowLimitLabel; // 显示可借数量的标签
    @FXML
    private Label hasOverdueLabel; // 显示是否有超期未还的标签
    @FXML
    private Label hasDebtLabel; // 显示欠费金额的标签

    private Reader currentReader = new Reader(); // 当前读者对象

    @FXML
    private void initialize() {
        findReaderInfo(); // 查找当前读者信息
        loadReaderInfo(); // 加载当前读者信息到界面
    }

    private void findReaderInfo() {
        try {
            FileReader fileReader = new FileReader("src/main/java/com/py/javaf1/Data/ReaderData");
            BufferedReader reader = new BufferedReader(fileReader);
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userDetails = line.split(",");
                if (userDetails.length == 7) {
                    if (LoginController.loginName.equals(userDetails[0])) {
                        currentReader.setName(userDetails[0]);
                        currentReader.setPassword(userDetails[1]);
                        currentReader.setReaderID(userDetails[2]);
                        currentReader.setStudentID(userDetails[3]);
                        currentReader.setBorrowLimit(Integer.parseInt(userDetails[4]));
                        currentReader.setHasOverdue(Integer.parseInt(userDetails[5]));
                        currentReader.setHasDebt(Double.parseDouble(userDetails[6]));
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadReaderInfo() {
        // 设置界面标签显示读者信息
        nameLabel.setText(currentReader.getName());
        studentIDLabel.setText(currentReader.getStudentID());
        borrowLimitLabel.setText(String.valueOf(currentReader.getBorrowLimit()));
        hasOverdueLabel.setText(currentReader.getHasOverdue() > 0 ? "是" : "否");
        hasDebtLabel.setText(String.valueOf(currentReader.getHasDebt()));
    }
}
