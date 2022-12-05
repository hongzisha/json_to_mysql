package com.example.json_to_mysql;

import com.alibaba.fastjson.JSONObject;
import com.example.json_to_mysql.service.JsonToMysqlProcessService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

@RunWith(SpringRunner.class)
@SpringBootTest
class JsonToMysqlApplicationTests {

/*    @Resource
    private JsonToMysqlService JsonToMysql;*/

    @Autowired
    private JsonToMysqlProcessService jsonToMysqlprocess;

    @Test
    void contextLoads() {
        Reader reader = null;
        BufferedReader bf = null;
        try {
            reader = new FileReader("E://heroName.json");
            bf = new BufferedReader(reader);
            String str;

            // 创建表，判断key值
            while ((str = bf.readLine()) != null) {
                JSONObject jsonObject = JSONObject.parseObject(str);
                //jsonToMysqlprocess.fileToJson(jsonObject);
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
    }
}
