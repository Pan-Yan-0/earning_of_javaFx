package com.py.javaf1.domain;

import javafx.beans.property.*;

public class BookWrapper {
    private Book book; // 原始的 Book 对象
    private StringProperty bookID; // 图书编号属性
    private StringProperty title; // 图书名称属性
    private StringProperty author; // 作者属性
    private StringProperty publisher; // 出版社属性
    private IntegerProperty totalCopies; // 总册数属性
    private IntegerProperty availableCopies; // 可借册数属性
    private StringProperty location; // 位置属性
    private IntegerProperty borrowCount; // 借阅次数属性

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
