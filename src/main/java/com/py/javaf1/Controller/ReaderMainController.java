package com.py.javaf1.Controller;

import com.py.javaf1.HelloApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.Node;
import javafx.stage.Stage;

import java.io.IOException;

public class ReaderMainController {

    @FXML
    public  StackPane contentPane;


    @FXML
    private void handleShowPersonalInfo() throws IOException {
        loadContent("PersonalInfoView.fxml");
    }

    @FXML
    private void handleShowSearchBooks() throws IOException {
        loadContent("SearchBookReader.fxml");
    }

    @FXML
    private void handleShowBorrowedBooks() throws IOException {
        loadContent("BorrowedBooksReturnView.fxml");
    }

    @FXML
    private void handleShowBorrowHistory() throws IOException {
        loadContent("HistoryBorrowView.fxml");
    }

    @FXML
    private void handleShowRankings() throws IOException {
        loadContent("BookRanking.fxml");
    }

    @FXML
    private void handleShowBorrowBooks() throws IOException {
        loadContent("BorrowedBooksView.fxml");
    }

    private void loadContent(String fxml) throws IOException {
        Node node = FXMLLoader.load(HelloApplication.class.getResource("/com/py/javaf1/View/" + fxml));
        contentPane.getChildren().setAll(node);

    }
    @FXML
    public void returnToLogin(ActionEvent actionEvent) {
        try {
            // 获取当前窗口
            Stage stage = (Stage) contentPane.getScene().getWindow();
            // 加载管理员登录界面
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/com/py/javaf1/View/Login-View" +
                    ".fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 400, 320);
            // 设置新场景
            stage.setScene(scene);
            stage.setTitle("Login");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
