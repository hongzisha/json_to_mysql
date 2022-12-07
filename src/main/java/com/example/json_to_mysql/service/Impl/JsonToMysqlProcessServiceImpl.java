package com.example.json_to_mysql.service.Impl;

import com.alibaba.fastjson.JSONObject;
import com.example.json_to_mysql.entity.ResultObject;
import com.example.json_to_mysql.enumUtils.ResponseConstant;
import com.example.json_to_mysql.mapper.JsonToMysqlMapper;
import com.example.json_to_mysql.service.JsonToMysqlProcessService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author TanHao
 * @date 2022/08/08 0008
 */
@Service
public class JsonToMysqlProcessServiceImpl implements JsonToMysqlProcessService {


    @Resource
    private JsonToMysqlMapper jsonToMysqlMapper;

    /**
     * 创建解析json所需的所有表
     *
     * @param jsonObject JSON数据
     * @return 返回表名
     */
    @Override
    public List<String> jsonCreateTable(JSONObject jsonObject) {
        // 判断主表是否存在，并将主表创建
        String masterTableString = (jsonObject.getString("masterTableKey"));
        System.out.println(masterTableString);
        List<String> tableNames = new ArrayList<>();
        if (masterTableString.length() > 0) {
            // 将主表的key的json格式转换为json对象
            JSONObject masterTableKey = JSONObject.parseObject(masterTableString);
            // 通过json对象获取主表名
            String masterTableName = masterTableKey.getString("TableName");
            // 将主表对应的列名转换为json对象
            JSONObject masterTableColumn = JSONObject.parseObject(masterTableKey.getString("TableColumn"));
            // 创建主表
            jsonToMysqlMapper.addTable(masterTableColumn, masterTableName);
            // 记录创建的表名
            tableNames.add(masterTableName);
        }

        // 判断子表是否存在，并将子表创建
        String sublistString = jsonObject.getString("sublistKey");
        if (sublistString.length() > 0) {
            // 将子表集的key的json格式转换为json对象
            JSONObject sublistKeys = JSONObject.parseObject(sublistString);
            // 遍历子表的表名和列名
            sublistKeys.forEach((keys, values) -> {
                // 将子表的key的json格式转换成一个json对象
                JSONObject sublistTableString = JSONObject.parseObject(values.toString());
                // 通过json对象获取子表名
                String sublistTableName = sublistTableString.getString("TableName");
                // 将子表对应的列名转换为json对象
                JSONObject sublistTableColumn = JSONObject.parseObject(sublistTableString.getString("TableColumn"));
                // 创建子表
                jsonToMysqlMapper.addTable(sublistTableColumn, sublistTableName);
                // 记录创建的表名
                tableNames.add(sublistTableName);
            });
        }

        return tableNames;
    }

    /**
     * 读取指定路径下的json数据，一行一行的进行解析
     *
     * @param jsonObject json数据
     * @param tableNames 表名
     */
    @Override
    public ResultObject<String> fileToJson(JSONObject jsonObject, List<String> tableNames) {
        ResultObject<String> resultObject = new ResultObject<>();
        String path = jsonObject.getString("path");
        System.out.println(path);
        Reader reader = null;
        BufferedReader bf = null;
        int num = 0;
        try {
            reader = new FileReader(path);
            bf = new BufferedReader(reader);
            String str;
            while ((str = bf.readLine()) != null) {
                System.out.println(str);
                JSONObject stringToJson = JSONObject.parseObject(str);
                System.out.println(stringToJson);
                jsonToTable(stringToJson, tableNames);
                num++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bf != null) {
                try {
                    bf.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        resultObject.setCode(ResponseConstant.SUCCESS.getCode());
        resultObject.setMsg(ResponseConstant.SUCCESS.getMsg());
        resultObject.setData("共计解析" + num + "行数据");
        return resultObject;
    }


    /**
     * 遍历整条json数据
     *
     * @param jsonObject json数据
     * @param tableNames 表名
     */
    public void jsonToTable(JSONObject jsonObject, List<String> tableNames) {
        int num = 0;
        // 表创建成功后，将对应的json数据插入
        jsonToMysqlMapper.insert(jsonObject, tableNames.get(0));
        //
        function(jsonObject, num, tableNames, 1);
    }


    /**
     * 进入子json使用递归解析数据
     *
     * @param jsonObject json数据
     * @param num        表名下标
     * @param tableNames 表名
     * @return 返回表名下标
     */
    public Integer facChar(JSONObject jsonObject, int num, List<String> tableNames) {
        // 定义表名的下标
        num = num + 1;
        // 表创建成功后，将对应的数据插入
        jsonToMysqlMapper.insert(jsonObject, tableNames.get(num));
        System.out.println(tableNames.get(num));
        // 判断下面是否还有子表
        num = function(jsonObject, num, tableNames, 2);
        return num;
    }


    /**
     * 将遍历到的数组解析
     *
     * @param lists      list集合
     * @param num        表名下标
     * @param tableNames 表名
     * @return 返回表名下标
     */
    public Integer facArray(List<String> lists, int num, List<String> tableNames) {
        num = num + 1;
        // 循环处理子表的数组数据
        for (String list : lists) {
            JSONObject jsonObject = JSONObject.parseObject(list);
            jsonToMysqlMapper.insert(jsonObject, tableNames.get(num));
        }
        return num;
    }

    public Integer function(JSONObject jsonObject, int num, List<String> tableNames, int flag) {
        for (Map.Entry<String, Object> map : jsonObject.entrySet()) {
            char ch = '{';
            char charArray = '[';
            if (charArray == map.getValue().toString().toCharArray()[0]) {
                // map对象转换为JOSN字符串
                List<String> lists = JSONObject.parseArray(map.getValue().toString(), String.class);
                num = facArray(lists, num, tableNames);
            }
            if (ch == map.getValue().toString().toCharArray()[0]) {
                // map对象转换为JSON字符串
                JSONObject jsonObject1 = JSONObject.parseObject(map.getValue().toString());
                if (flag == 1) {
                    num = facChar(jsonObject1, num, tableNames);
                } else if (flag == 2) {
                    return facChar(jsonObject1, num, tableNames);
                }
            }
        }
        return num;
    }
}
