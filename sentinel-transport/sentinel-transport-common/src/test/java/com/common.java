package com;

import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.fastjson.JSONArray;
import org.junit.Test;

import java.util.List;

/**
 * @author Yaosh
 * @version 1.0
 * @commpany 星瑞国际
 * @date 2020/6/5 9:56
 * @return
 */
public class common {

    @Test
    public void test(){
        String data = " [{\"clusterConfig\":{\"fallbackToLocalWhenFail\":true,\"sampleCount\":10,\"strategy\":0,\"thresholdType\":0,\"windowIntervalMs\":1000},\"clusterMode\":false,\"controlBehavior\":0,\"count\":1000.0,\"grade\":1,\"limitApp\":\"default\",\"maxQueueingTimeMs\":500,\"resource\":\"WechatsignHandler\",\"strategy\":0,\"warmUpPeriodSec\":10}]";
        List<FlowRule> flowRules = JSONArray.parseArray(data, FlowRule.class);
        System.out.println(flowRules);
    }
}
