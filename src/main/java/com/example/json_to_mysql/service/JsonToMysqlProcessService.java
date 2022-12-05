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
     * @param jsonObject
     * @return
     */
    List<String> jsonCreateTable(JSONObject jsonObject);

    /**
     * 读取文件内容
     * @param jsonObject
     */
    ResultObject fileToJson(JSONObject jsonObject , List<String> tableNames);
}
