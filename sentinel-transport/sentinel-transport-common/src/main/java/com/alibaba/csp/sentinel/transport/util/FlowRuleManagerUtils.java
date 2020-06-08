package com.alibaba.csp.sentinel.transport.util;

import com.alibaba.csp.sentinel.datasource.WritableDataSource;
import com.alibaba.csp.sentinel.log.RecordLog;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.csp.sentinel.util.StringUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import java.sql.*;
import java.util.List;

import static com.alibaba.csp.sentinel.transport.util.WritableDataSourceRegistry.getFlowDataSource;

/**
 * 流控工具
 * @author Yaosh
 * @version 1.0
 * @commpany 星瑞国际
 * @date 2020/6/5 10:03
 * @return
 */
public class FlowRuleManagerUtils {


    /**
     * 加载流控规则
     * @param data
     */
    public static boolean loadRules(String data){
        List<FlowRule> flowRules = JSONArray.parseArray(data, FlowRule.class);
        RecordLog.info(String.format(">>>> [FlowRuleManagerUtils] flowRules:%s", JSON.toJSONString(flowRules)));
        FlowRuleManager.loadRules(flowRules);
        try {
            getFlowDataSource().write(flowRules);
        } catch (Exception e) {
            RecordLog.warn("@Write data source failed", e);
            return false;
        }
        RecordLog.warn("loadRules Success!");
        return true;
    }

    /**
     * 记载流控规则（FROM DB）
     * @param driver
     * @param url
     * @param user
     * @param password
     * @param appName
     */
    public static boolean loadRules(String driver,String url,String user,String password,String appName){
        Connection conn = null;
        Statement stmt = null;
        try{
            Class.forName(driver);

            conn = DriverManager.getConnection(url,user,password);

            stmt = conn.createStatement();
            StringBuffer sqlBuffer = new StringBuffer();
            sqlBuffer.append(" SELECT id,app_name appName,`type`,rules,mq_storetime mqStoretime ");
            sqlBuffer.append(" FROM sentinel_rules ");
            sqlBuffer.append(String.format(" WHERE app_name = '%s' AND stat = '1'",appName));
            ResultSet rs = stmt.executeQuery(sqlBuffer.toString());

            while(rs.next()){
                int id  = rs.getInt("id");
                String type = rs.getString("type");
                String rules = rs.getString("rules");

                RecordLog.info(String.format(">>>> [JDBC]type:%s ,rules:%s",type,rules));
                if(StringUtil.equals("flow",type)){
                    FlowRuleManagerUtils.loadRules(rules);
                }

            }
            rs.close();
            stmt.close();
            conn.close();

            return true;
        }catch(SQLException se){
            se.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }finally{

            try{
                if(stmt!=null) stmt.close();
            }catch(SQLException se2){
            }
            try{
                if(conn!=null) conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }
        return false;
    }
}
