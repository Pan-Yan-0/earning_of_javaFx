package com.py.javaf1.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BorrowRecord {
    String readerID;        // 读者编号
    String bookID;          // 图书编号
    Date borrowDate;        // 借阅时间
}
