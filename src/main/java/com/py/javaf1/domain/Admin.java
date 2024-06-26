package com.py.javaf1.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Admin类表示一个管理员对象，包含了管理员的基本信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Admin {
    private String name;            // 管理员用户名
    private String password;        // 管理员登录密码
}
