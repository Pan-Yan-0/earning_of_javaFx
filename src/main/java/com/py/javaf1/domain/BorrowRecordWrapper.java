package com.py.javaf1.domain;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class BorrowRecordWrapper {
    private final String readerID;
    private final String bookID;
    private final String borrowDate;
    private final String returnDate;

    public BorrowRecordWrapper(String readerID, String bookID, String borrowDate, String returnDate) {
        this.readerID = readerID;
        this.bookID = bookID;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
    }

    public String getReaderID() {
        return readerID;
    }

    public String getBookID() {
        return bookID;
    }

    public String getBorrowDate() {
        return borrowDate;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public StringProperty readerIDProperty() {
        return new SimpleStringProperty(readerID);
    }

    public StringProperty bookIDProperty() {
        return new SimpleStringProperty(bookID);
    }

    public StringProperty borrowDateProperty() {
        return new SimpleStringProperty(borrowDate);
    }

    public StringProperty returnDateProperty() {
        return new SimpleStringProperty(returnDate);
    }
}
