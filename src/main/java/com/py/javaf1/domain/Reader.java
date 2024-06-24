package com.py.javaf1.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Reader {
    private String name;            // 读者用户名
    private String password;        // 读者登录密码
    private String readerID;        // 读者编号
    private String studentID;       // 读者学号
    private int borrowLimit;        // 可借数量
    private int hasOverdue;     // 有无超期未还
    private double hasDebt;        // 有无欠费

}
