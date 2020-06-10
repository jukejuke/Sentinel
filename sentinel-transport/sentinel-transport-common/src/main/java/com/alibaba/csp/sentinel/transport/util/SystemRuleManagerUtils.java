package com.alibaba.csp.sentinel.transport.util;

import com.alibaba.csp.sentinel.datasource.WritableDataSource;
import com.alibaba.csp.sentinel.log.RecordLog;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.csp.sentinel.slots.system.SystemRule;
import com.alibaba.csp.sentinel.slots.system.SystemRuleManager;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import java.util.List;

import static com.alibaba.csp.sentinel.transport.util.WritableDataSourceRegistry.getFlowDataSource;
import static com.alibaba.csp.sentinel.transport.util.WritableDataSourceRegistry.getSystemSource;

/**
 * 系统规则
 * @author Yaosh
 * @version 1.0
 * @commpany 星瑞国际
 * @date 2020/6/10 8:30
 * @return
 */
public class SystemRuleManagerUtils {

    /**
     * 加载规则
     * @param data
     */
    public static boolean loadRules(String data){
        List<SystemRule> rules = JSONArray.parseArray(data, SystemRule.class);
        RecordLog.info(String.format(">>>> [SystemRuleManagerUtils] Rules:%s", JSON.toJSONString(rules)));
        SystemRuleManager.loadRules(rules);
        try {
            WritableDataSource<List<SystemRule>> dataSource = getSystemSource();
            if(dataSource == null){
                RecordLog.info(">>>> [SystemRuleManagerUtils] @DataSource NULL...");
            }
            dataSource.write(rules);
        } catch (Exception e) {
            RecordLog.warn("[SystemRuleManagerUtils] @Write data source failed", e);
            return false;
        }
        RecordLog.warn("[SystemRuleManagerUtils] loadRules Success!");
        return true;
    }
}
