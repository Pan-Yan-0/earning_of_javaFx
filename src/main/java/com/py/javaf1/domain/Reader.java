package com.py.javaf1.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Reader类表示一个读者对象，包含了读者的基本信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Reader {
    // 读者用户名
    private String name;

    // 读者登录密码
    private String password;

    // 读者编号
    private String readerID;

    // 读者学号
    private String studentID;

    // 可借数量
    private int borrowLimit;

    // 有无超期未还
    private int hasOverdue;

    // 有无欠费
    private double hasDebt;
}
