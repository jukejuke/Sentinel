package com.alibaba.csp.sentinel.transport.util;

import com.alibaba.csp.sentinel.log.RecordLog;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import java.util.List;

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
    public static void loadRules(String data){
        List<FlowRule> flowRules = JSONArray.parseArray(data, FlowRule.class);
        RecordLog.info(String.format(">>>> [FlowRuleManagerUtils] flowRules:%s", JSON.toJSONString(flowRules)));
        FlowRuleManager.loadRules(flowRules);
    }
}
