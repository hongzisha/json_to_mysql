package com.example.json_to_mysql.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.json_to_mysql.entity.ResultObject;
import com.example.json_to_mysql.service.JsonToMysqlProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;


/**
 * @author TanHao
 * @date 2022/08/08 0008
 * @ 文件中的key值和配置文件中的key值对应，但是列数可以不一致
 * @ json串中的数组里面的json串不能在带json串
 * @ 数据示例：{"deviceId":"zmc","type":"0","extra":{"msisdn":"14726092687","uid":{"phone":"18213556792","msisdn":{"songId":"0987","array":[{"msisdn":"14346092687"},{"msisdn":"18215757892"}],"test":"123"}}},"seqId":"123","songId":[{"paramKey":"name","paramValue":"wf"},{"paramKey":"name","paramValue":"wj"}]}
 */

@RestController
@RequestMapping("/file")
public class JsonToMysqlController {

    @Autowired
    private JsonToMysqlProcessService jsonToMysqlProcess;


    @PostMapping("/JsonToMysql")
    public ResultObject<String> jsonToMysql(@RequestBody String request) {
        JSONObject jsonObject = JSONObject.parseObject(request);
        // 创建表，并返回创建的表名
        List<String> tableNames = jsonToMysqlProcess.jsonCreateTable(jsonObject);
        System.out.println(tableNames);
        // 将数据插入对应表中
        return jsonToMysqlProcess.fileToJson(jsonObject , tableNames);
    }

}

