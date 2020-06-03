package com.alibaba.csp.sentinel.dashboard.config;

import java.util.ArrayList;
import java.util.List;

/**
 * 拦截记录请求日志URL
 * @author Yaosh
 * @version 1.0
 * @commpany 星瑞国际
 * @date 2020/6/3 16:04
 * @return
 */
public class LogInterceptorConfig {

    public static final List<String> LogURL = new ArrayList<String>(){
        {
            this.add("setRules");
        }
    };
}
