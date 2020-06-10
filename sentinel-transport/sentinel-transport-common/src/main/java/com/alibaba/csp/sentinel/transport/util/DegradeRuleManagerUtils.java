package com.alibaba.csp.sentinel.transport.util;

import com.alibaba.csp.sentinel.datasource.WritableDataSource;
import com.alibaba.csp.sentinel.log.RecordLog;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityRule;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityRuleManager;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import java.util.List;

import static com.alibaba.csp.sentinel.transport.util.WritableDataSourceRegistry.getAuthorityDataSource;
import static com.alibaba.csp.sentinel.transport.util.WritableDataSourceRegistry.getDegradeDataSource;

/**
 * 降级规则
 * @author Yaosh
 * @version 1.0
 * @commpany 星瑞国际
 * @date 2020/6/10 8:29
 * @return
 */
public class DegradeRuleManagerUtils {

    /**
     * 加载规则
     * @param data
     */
    public static boolean loadRules(String data){
        List<DegradeRule> rules = JSONArray.parseArray(data, DegradeRule.class);
        RecordLog.info(String.format(">>>> [DegradeRuleManagerUtils] Rules:%s", JSON.toJSONString(rules)));
        DegradeRuleManager.loadRules(rules);
        try {
            WritableDataSource<List<DegradeRule>> dataSource = getDegradeDataSource();
            if(dataSource == null){
                RecordLog.info(">>>> [DegradeRuleManagerUtils] @DataSource NULL...");
            }
            dataSource.write(rules);
        } catch (Exception e) {
            RecordLog.warn("[DegradeRuleManagerUtils] @Write data source failed", e);
            return false;
        }
        RecordLog.warn("[DegradeRuleManagerUtils] loadRules Success!");
        return true;
    }
}
