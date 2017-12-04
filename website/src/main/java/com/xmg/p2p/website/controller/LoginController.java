package com.xmg.p2p.website.controller;

import com.xmg.p2p.base.domain.LoginInfo;
import com.xmg.p2p.base.service.ILoginInfoService;
import com.xmg.p2p.base.util.AjaxResult;
import com.xmg.p2p.base.util.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LoginController {
    @Autowired
    private ILoginInfoService loginInfoService;
    @ResponseBody
    @RequestMapping("/userLogin")
    public AjaxResult userLogin(String username, String password) {
        AjaxResult result = null;
        LoginInfo loginInfo = loginInfoService.login(username, password,LoginInfo.USERTYPE_USER);
        if (loginInfo != null) {
            UserContext.setCurrent(loginInfo);
            result = new AjaxResult("登录成功");
        } else {
            result=new AjaxResult(false,"账号密码错误");
        }

        return result;
    }

}
