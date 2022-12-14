package com.example.json_to_mysql.mapper;

import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Mapper;


/**
 * @author TanHao
 * @date 2022/08/08 0008
 */
@Mapper
public interface JsonToMysqlMapper {

    /**
     * 新增表
     * @param jsonObject json数据
     * @param tableName 表名
     */
    void addTable(JSONObject jsonObject, String tableName);

    /**
     * 新增数据
     * @param jsonObject json数据
     * @param tableName 表名
     */
    void insert(JSONObject jsonObject, String tableName);


}
