package com.py.javaf1.Controller;

import com.py.javaf1.domain.Reader;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.*;

public class PersonalInfoController {

    @FXML
    private Label nameLabel;
    @FXML
    private Label studentIDLabel;
    @FXML
    private Label borrowLimitLabel;
    @FXML
    private Label hasOverdueLabel;
    @FXML
    private Label hasDebtLabel;

    private Reader currentReader = new Reader();

    @FXML
    private void initialize() {
        findReaderInfo();
        loadReaderInfo();
    }

    private void findReaderInfo() {
        try {
            FileReader fileReader = new FileReader("src/main/java/com/py/javaf1/Data/ReaderData");
            BufferedReader reader = new BufferedReader(fileReader);
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userDetails = line.split(",");
                if (userDetails.length == 7) {
                    if (LoginController.loginName.equals(userDetails[0])){
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
        nameLabel.setText(currentReader.getName());
        studentIDLabel.setText(currentReader.getStudentID());
        borrowLimitLabel.setText(String.valueOf(currentReader.getBorrowLimit()));
        hasOverdueLabel.setText(currentReader.getHasOverdue() > 0 ? "是" : "否");
        hasDebtLabel.setText(String.valueOf(currentReader.getHasDebt()));
    }
}
