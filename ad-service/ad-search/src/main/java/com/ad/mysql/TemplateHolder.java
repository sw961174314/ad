package com.ad.mysql;

import com.ad.mysql.constant.OpType;
import com.ad.mysql.dto.ParseTemplate;
import com.ad.mysql.dto.TableTemplate;
import com.ad.mysql.dto.Template;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

/**
 * 处理数据库中的表模板
 */
@Slf4j
@Component
public class TemplateHolder {

    private ParseTemplate template;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private String SQL_SCHEMA = "select table_schema,table_name,column_name,ordinal_position from information_schema.columns where table_schema = ? and table_name = ?";

    /**
     * 初始化
     */
    @PostConstruct
    private void init() {
        loadJson("template.json");
    }

    /**
     * 根据给定的表名返回对应的TableTemplate对象
     * @param tableName
     * @return
     */
    public TableTemplate getTable(String tableName) {
        return template.getTableTemplateMap().get(tableName);
    }

    /**
     * 从给定路径加载Json文件 并将其解析为Template对象
     * @param path
     */
    private void loadJson(String path) {
        // 获取当前线程的类加载器(ClassLoader)
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        // 使用类加载器的getResourceAsStream方法读取指定路径的Json文件 返回一个inputStream
        InputStream inputStream = classLoader.getResourceAsStream(path);
        try {
            // 使用JSON.parseObject方法将InputStream对象解析成一个Template对象 使用系统默认的字符集
            Template template = JSON.parseObject(inputStream, Charset.defaultCharset(), Template.class);
            this.template = ParseTemplate.parse(template);
            loadMeta();
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException("fail to parse json file");
        }
    }

    /**
     * 加载元数据
     */
    private void loadMeta() {
        // 遍历template对象中的tableTemplateMap属性 获取每个表的TableTemplate对象
        for (Map.Entry<String, TableTemplate> entry : template.getTableTemplateMap().entrySet()) {
            TableTemplate table = entry.getValue();
            // 分别获取表的插入字段、更新字段和删除字段
            List<String> insertFields = table.getOpTypeFieldSetMap().get(OpType.ADD);
            List<String> updateFields = table.getOpTypeFieldSetMap().get(OpType.UPDATE);
            List<String> deleteFields = table.getOpTypeFieldSetMap().get(OpType.DELETE);
            // 使用jdbcTemplate执行SQL语句查询 传入数据库名和表名作为参数
            jdbcTemplate.query(SQL_SCHEMA,new Object[]{
                    template.getDatabase(),table.getTableName()
            },(rs,i) ->{
                // 遍历查询结果集 获取每行的ORDINAL_POSITION和COLUMN_NAME
                int pos = rs.getInt("ORDINAL_POSITION");
                String colName = rs.getString("COLUMN_NAME");
                if ((null != insertFields && insertFields.contains(colName)) || (null != updateFields && updateFields.contains(colName)) || (null != deleteFields && deleteFields.contains(colName))) {
                    table.getPosMap().put(pos - 1, colName);
                }
                return null;
            });
        }
    }
}