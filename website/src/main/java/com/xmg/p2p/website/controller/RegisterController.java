package com.xmg.p2p.website.controller;

import com.xmg.p2p.base.service.ILoginInfoService;
import com.xmg.p2p.base.util.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class RegisterController {
    @Autowired
    private ILoginInfoService loginInfoService;

    @ResponseBody
    @RequestMapping("/userRegister")
    public AjaxResult register(String username, String password) {
        AjaxResult result = null;
        try {
            loginInfoService.register(username, password);
            return new AjaxResult("注册成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new AjaxResult(false, "注册失败," + e.getMessage());
        }
    }

    @RequestMapping("/checkUsername")
    @ResponseBody
    public boolean checkUsername(String username) {

        return loginInfoService.checkUsername(username);
    }
}
