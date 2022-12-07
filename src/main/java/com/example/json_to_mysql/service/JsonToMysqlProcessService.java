package com.example.json_to_mysql.service;

import com.alibaba.fastjson.JSONObject;
import com.example.json_to_mysql.entity.ResultObject;

import java.util.List;

/**
 * @author TanHao
 * @date 2022/08/08 0008
 */
public interface JsonToMysqlProcessService {

    /**
     * json表创建
     * @param jsonObject json数据
     * @return 表名
     */
    List<String> jsonCreateTable(JSONObject jsonObject);


    /**
     * 读取文件内容
     * @param jsonObject json数据
     * @param tableNames 表名
     * @return 结果
     */
    ResultObject<String> fileToJson(JSONObject jsonObject , List<String> tableNames);
}
