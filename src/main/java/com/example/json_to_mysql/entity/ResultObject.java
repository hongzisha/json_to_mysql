package com.example.json_to_mysql.entity;

import lombok.Data;

/**
 * @author TanHao
 * @date 2022/10/19 0019
 */
@Data
public class ResultObject<T> {
    /**
     * 返回编码
     **/
    private String code = "0000";
    /**
     * 返回信息
     **/
    private String msg;
    /**
     * 数据
     */
    private T data;
}
