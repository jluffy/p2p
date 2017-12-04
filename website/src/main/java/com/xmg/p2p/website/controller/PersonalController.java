package com.xmg.p2p.website.controller;

import com.xmg.p2p.base.domain.Account;
import com.xmg.p2p.base.service.IAccountService;
import com.xmg.p2p.base.service.IMailVerifyService;
import com.xmg.p2p.base.service.IUserinfoService;
import com.xmg.p2p.base.util.AjaxResult;
import com.xmg.p2p.base.util.UserContext;
import com.xmg.p2p.business.service.IExpAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PersonalController {
    @Autowired
    IAccountService accountService;
    @Autowired
    IUserinfoService userinfoService;
    @Autowired
    IMailVerifyService emailVerifyService;
    @Autowired
    IExpAccountService expAccountService;

    @RequestMapping("/personal")
    public String  personal (Model model){
        Account account = accountService.get(UserContext.getCurrent().getId());
        model.addAttribute("account",account);
        //将userinfo共享到域中
        model.addAttribute("userinfo",userinfoService.get(UserContext.getCurrent().getId()));
        model.addAttribute("expAccount",expAccountService.get(UserContext.getCurrent().getId()));
        return  "personal";
    }
    @ResponseBody
    @RequestMapping("/bindPhone")
    public AjaxResult bindPhone(String phoneNumber,String verifyCode){
        AjaxResult result=null;
        try {
            userinfoService.bindPhone(phoneNumber,verifyCode);
            result=new AjaxResult();

        } catch (Exception e) {
            result=new AjaxResult(false,"手机绑定失败");
            e.printStackTrace();
        }
        return  result;
    }
    @RequestMapping("/bindEmail")
    public String bindEmail(String key,Model model){
        AjaxResult result=null;
        try {
             userinfoService.bindEamil(key);
            model.addAttribute("success",true);
        } catch (Exception e) {
            model.addAttribute("success",false);
            model.addAttribute("msg",e.getMessage());
            e.printStackTrace();
        }
        return "checkmail_result";
    }
}
