package com.xmg.p2p.mgrsite.controller;

import com.xmg.p2p.base.domain.LoginInfo;
import com.xmg.p2p.base.service.ILoginInfoService;
import com.xmg.p2p.base.util.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/*
* 后台用户的录操作
*
* */
@Controller
public class LoginController {
    @Autowired
    ILoginInfoService loginInfoService;
    @RequestMapping("/mgrlogin")
    @ResponseBody
    public AjaxResult index(String username,String password){
        AjaxResult result=null;
        //根据用户名和密码查询数据库判断其正确性
        LoginInfo loginInfo = loginInfoService.login(username, password, LoginInfo.USERTYPE_MANAGER);
        if(loginInfo==null){
            result=new AjaxResult(false,"登录失败,账号密码有误");
        }else{
            result=new AjaxResult(true,"登录成功");
        }
        return  result;
    }
}
