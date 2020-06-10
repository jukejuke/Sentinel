package com.alibaba.csp.sentinel.transport.util;

import com.alibaba.csp.sentinel.datasource.WritableDataSource;
import com.alibaba.csp.sentinel.log.RecordLog;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityRule;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityRuleManager;
import com.alibaba.csp.sentinel.slots.system.SystemRule;
import com.alibaba.csp.sentinel.slots.system.SystemRuleManager;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import java.util.List;

import static com.alibaba.csp.sentinel.transport.util.WritableDataSourceRegistry.getAuthorityDataSource;
import static com.alibaba.csp.sentinel.transport.util.WritableDataSourceRegistry.getSystemSource;

/**
 * 热点规则
 * @author Yaosh
 * @version 1.0
 * @commpany 星瑞国际
 * @date 2020/6/10 8:29
 * @return
 */
public class AuthorityRuleManagerUtils
{
    /**
     * 加载规则
     * @param data
     */
    public static boolean loadRules(String data){
        List<AuthorityRule> rules = JSONArray.parseArray(data, AuthorityRule.class);
        RecordLog.info(String.format(">>>> [AuthorityRuleManagerUtils] Rules:%s", JSON.toJSONString(rules)));
        AuthorityRuleManager.loadRules(rules);
        try {
            WritableDataSource<List<AuthorityRule>> dataSource = getAuthorityDataSource();
            if(dataSource == null){
                RecordLog.info(">>>> [AuthorityRuleManagerUtils] @DataSource NULL...");
            }
            dataSource.write(rules);
        } catch (Exception e) {
            RecordLog.warn("[AuthorityRuleManagerUtils] @Write data source failed", e);
            return false;
        }
        RecordLog.warn("[AuthorityRuleManagerUtils] loadRules Success!");
        return true;
    }
}
