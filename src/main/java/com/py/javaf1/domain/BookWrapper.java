package com.py.javaf1.domain;

import javafx.beans.property.*;

public class BookWrapper {
    private Book book;
    private StringProperty bookID;
    private StringProperty title;
    private StringProperty author;
    private StringProperty publisher;
    private IntegerProperty totalCopies;
    private IntegerProperty availableCopies;
    private StringProperty location;
    private IntegerProperty borrowCount;

    public BookWrapper(Book book) {
        this.book = book;
        this.bookID = new SimpleStringProperty(book.getBookID());
        this.title = new SimpleStringProperty(book.getTitle());
        this.author = new SimpleStringProperty(book.getAuthor());
        this.publisher = new SimpleStringProperty(book.getPublisher());
        this.totalCopies = new SimpleIntegerProperty(book.getTotalCopies());
        this.availableCopies = new SimpleIntegerProperty(book.getAvailableCopies());
        this.location = new SimpleStringProperty(book.getLocation());
        this.borrowCount = new SimpleIntegerProperty(book.getBorrowCount());
    }

    public Book getBook() {
        return book;
    }

    public StringProperty bookIDProperty() {
        return bookID;
    }

    public StringProperty titleProperty() {
        return title;
    }

    public StringProperty authorProperty() {
        return author;
    }

    public StringProperty publisherProperty() {
        return publisher;
    }

    public IntegerProperty totalCopiesProperty() {
        return totalCopies;
    }

    public IntegerProperty availableCopiesProperty() {
        return availableCopies;
    }

    public StringProperty locationProperty() {
        return location;
    }

    public IntegerProperty borrowCountProperty() {
        return borrowCount;
    }
}
