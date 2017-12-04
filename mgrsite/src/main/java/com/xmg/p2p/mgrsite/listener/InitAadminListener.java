package com.xmg.p2p.mgrsite.listener;

import com.xmg.p2p.base.service.ILoginInfoService;
import com.xmg.p2p.business.service.ISystemAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class InitAadminListener  implements ApplicationListener<ContextRefreshedEvent>{
    @Autowired
    ILoginInfoService loginInfoService;
    @Autowired
    ISystemAccountService systemAccountService;
    public void onApplicationEvent(ContextRefreshedEvent event) {
        //我们的配置是和loginInfo公用一套组件
        loginInfoService.initAdmin();
        //初始化系统的账户
        systemAccountService.initSystemAccount();

    }
}
