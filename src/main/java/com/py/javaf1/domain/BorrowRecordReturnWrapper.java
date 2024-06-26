package com.py.javaf1.domain;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Button;

public class BorrowRecordReturnWrapper {
    private final String readerID; // 读者编号
    private final String bookID; // 图书编号
    private final String title; // 图书名称
    private final String author; // 作者
    private final String borrowDate; // 借阅日期
    private final String returnDate; // 归还日期
    private final Button returnButton; // 归还按钮

    public BorrowRecordReturnWrapper(String readerID, String bookID, String title, String author, String borrowDate, String returnDate, Button returnButton) {
        this.readerID = readerID;
        this.bookID = bookID;
        this.title = title;
        this.author = author;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
        this.returnButton = returnButton;
    }

    public String getReaderID() {
        return readerID;
    }

    public String getBookID() {
        return bookID;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getBorrowDate() {
        return borrowDate;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public Button getReturnButton() {
        return returnButton;
    }

    public StringProperty readerIDProperty() {
        return new SimpleStringProperty(readerID);
    }

    public StringProperty bookIDProperty() {
        return new SimpleStringProperty(bookID);
    }

    public StringProperty titleProperty() {
        return new SimpleStringProperty(title);
    }

    public StringProperty authorProperty() {
        return new SimpleStringProperty(author);
    }

    public StringProperty borrowDateProperty() {
        return new SimpleStringProperty(borrowDate);
    }

    public StringProperty returnDateProperty() {
        return new SimpleStringProperty(returnDate);
    }
}
