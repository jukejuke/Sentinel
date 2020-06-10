package com.alibaba.csp.sentinel.transport.util;

import com.alibaba.csp.sentinel.log.RecordLog;
import com.alibaba.csp.sentinel.util.StringUtil;

import java.sql.*;

/**
 * 规则工具
 * @author Yaosh
 * @version 1.0
 * @commpany 星瑞国际
 * @date 2020/6/10 8:27
 * @return
 */
public class RuleManagerUtils {

    /**
     * 记载流控规则（FROM DB）
     * @param driver
     * @param url
     * @param user
     * @param password
     * @param appName 应用名
     */
    public static boolean loadRules(String driver,String url,String user,String password,String appName){
        return loadRules(driver,url,user,password,"",appName);
    }

    /**
     * 记载流控规则（FROM DB）
     * @param driver
     * @param url
     * @param user
     * @param password
     * @param dbName 数据库名（例： xingrui_crm.）
     * @param appName 应用名
     */
    public static boolean loadRules(String driver,String url,String user,String password,String dbName,String appName){
        RecordLog.info(">>>> [FlowRuleManagerUtils] loadRules DB...");
        if(dbName==null){
            dbName = "";
        }

        Connection conn = null;
        Statement stmt = null;
        try{
            Class.forName(driver);

            conn = DriverManager.getConnection(url,user,password);

            stmt = conn.createStatement();
            StringBuffer sqlBuffer = new StringBuffer();
            sqlBuffer.append(" SELECT id,app_name appName,`type`,rules,mq_storetime mqStoretime ");
            sqlBuffer.append(" FROM "+dbName+"sentinel_rules ");
            sqlBuffer.append(String.format(" WHERE app_name = '%s' AND stat = '1'",appName));
            RecordLog.info(String.format(">>>> [FlowRuleManagerUtils] SQL:%s",sqlBuffer.toString()));
            ResultSet rs = stmt.executeQuery(sqlBuffer.toString());

            RecordLog.info(">>>> [FlowRuleManagerUtils] SQL EXECUTE...");
            while(rs.next()){
                int id  = rs.getInt("id");
                String type = rs.getString("type");
                String rules = rs.getString("rules");

                RecordLog.info(String.format(">>>> [JDBC]type:%s ,rules:%s",type,rules));
                if(StringUtil.equalsIgnoreCase("flow",type)){
                    FlowRuleManagerUtils.loadRules(rules);
                }else if(StringUtil.equalsIgnoreCase("degrade",type)){
                    DegradeRuleManagerUtils.loadRules(rules);
                }else if(StringUtil.equalsIgnoreCase("system",type)){
                    SystemRuleManagerUtils.loadRules(rules);
                }else if(StringUtil.equalsIgnoreCase("authority",type)){
                    AuthorityRuleManagerUtils.loadRules(rules);
                }

            }
            rs.close();
            stmt.close();
            conn.close();

            return true;
        }catch(SQLException se){
            RecordLog.error(">>>> [FlowRuleManagerUtils]"+se.getMessage(),se);
            se.printStackTrace();
        }catch(Exception e){
            RecordLog.error(">>>> [FlowRuleManagerUtils]"+e.getMessage(),e);
            e.printStackTrace();
        }finally{

            try{
                if(stmt!=null) {
                    stmt.close();
                }
            }catch(SQLException se2){
            }
            try{
                if(conn!=null) {
                    conn.close();
                }
            }catch(SQLException se){
                se.printStackTrace();
            }
        }
        return false;
    }
}
