package com.alibaba.csp.sentinel.dashboard.config;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.lang.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * RocketMQ配置
 * @author Yaosh
 * @version 1.0
 * @commpany 星瑞国际
 * @date 2020/6/4 10:25
 * @return
 */
public class RocketMQConfig {

    /**
     * 启用 ROCKETMQ消息发送 0：否 1：是
     */
    private static final String SEND_MQ_RULES_FLAG = "send.mq.rules.flag";

    /**
     * NameServer Address
     */
    private static final String NAME_SRV_ADDR =  "rocketmq.name.srvaddr";

    /**
     * 组名
     */
    private static final String GROUP_NAME = "rocketmq.group_name";

    /**
     * 流控规则TOPIC
     */
    private static final String RULES_TOPIC = "rocketmq.rules.topic";

    private static final ConcurrentMap<String, Object> cacheMap = new ConcurrentHashMap<>();

    @NonNull
    private static String getConfig(String name) {
        // env
        String val = System.getenv(name);
        if (StringUtils.isNotEmpty(val)) {
            return val;
        }
        // properties
        val = System.getProperty(name);
        if (StringUtils.isNotEmpty(val)) {
            return val;
        }
        return "";
    }

    protected static String getConfigStr(String name,String defaultVal) {
        if (cacheMap.containsKey(name)) {
            return (String) cacheMap.get(name);
        }

        String val = getConfig(name);

        if (StringUtils.isBlank(val)) {
            val = defaultVal;
        }

        cacheMap.put(name, val);
        return val;
    }

    protected static int getConfigInt(String name, int defaultVal, int minVal) {
        if (cacheMap.containsKey(name)) {
            return (int)cacheMap.get(name);
        }
        int val = NumberUtils.toInt(getConfig(name));
        if (val == 0) {
            val = defaultVal;
        } else if (val < minVal) {
            val = minVal;
        }
        cacheMap.put(name, val);
        return val;
    }

    public static String getSendMQRulesFlag(){
        return getConfigStr(SEND_MQ_RULES_FLAG,"0");
    }

    public static String getNameSrvAddr(){
        return getConfigStr(NAME_SRV_ADDR,"localhost:9876");
    }

    public static String getGroupName(){
        return getConfigStr(GROUP_NAME,"Sentinel_Producer");
    }

    public static String getRulesTopic(){
        return getConfigStr(RULES_TOPIC,"Sentinel_Rules_Topic");
    }

    public static final List<String> MQPath = new ArrayList<String>(){
        {
            this.add("setRules");
        }
    };
}
