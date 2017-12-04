package com.xmg.p2p.website.controller;

import com.xmg.p2p.base.service.IVerifyCodeService;
import com.xmg.p2p.base.util.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SendVerifyCodeController {
    @Autowired
    IVerifyCodeService verifyCodeService;
    @RequestMapping("/sendVerifyCode")
    @ResponseBody
    public AjaxResult sendVerifyCode(String phoneNumber){
            AjaxResult result=null;
        try {
            verifyCodeService.sendVerifyCode(phoneNumber);
            result=new AjaxResult("发送成功");
        } catch (Exception e) {
            result=new AjaxResult(false,"发送失败"+e.getMessage());
            e.printStackTrace();
        }
        return result;

    }
}
