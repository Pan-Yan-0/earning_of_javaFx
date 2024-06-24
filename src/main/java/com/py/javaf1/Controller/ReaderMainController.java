package com.py.javaf1.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.StackPane;
import javafx.scene.Node;

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
        loadContent("SearchBookView.fxml");
    }

    @FXML
    private void handleShowBorrowedBooks() throws IOException {
        loadContent("BorrowedBooksView.fxml");
    }

    @FXML
    private void handleShowBorrowHistory() throws IOException {
        loadContent("BorrowHistoryView.fxml");
    }

    @FXML
    private void handleShowRankings() throws IOException {
        loadContent("RankingsView.fxml");
    }

    @FXML
    private void handleShowBorrowBooks() throws IOException {
        loadContent("BorrowBooksView.fxml");
    }

    private void loadContent(String fxml) throws IOException {
        Node node = FXMLLoader.load(getClass().getResource("/com/py/javaf1/view/" + fxml));
        contentPane.getChildren().setAll(node);
    }
    @FXML
    public void returnToLogin(ActionEvent actionEvent) {

    }
}
