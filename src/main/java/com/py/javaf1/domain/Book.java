package com.py.javaf1.domain;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Book {
    String bookID;          // 图书编号
    String title;           // 图书名称
    String author;          // 作者
    String publisher;       // 出版社
    int totalCopies;        // 总共册数
    int availableCopies;    // 在馆册数
    String location;        // 现存地址
    int borrowCount;        // 借阅次数

}
