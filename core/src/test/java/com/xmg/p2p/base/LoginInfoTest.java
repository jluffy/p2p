package com.xmg.p2p.base;


import com.xmg.p2p.CoreConfig;
import com.xmg.p2p.base.service.ILoginInfoService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {CoreConfig.class})
public class LoginInfoTest {
    @Autowired
    ILoginInfoService loginInfoService;
    @Test
    public void testBean() {
        System.out.println(loginInfoService);
        loginInfoService.register("xiaozhang","11");
    }
}
