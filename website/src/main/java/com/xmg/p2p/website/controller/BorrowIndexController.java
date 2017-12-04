package com.xmg.p2p.website.controller;

import com.xmg.p2p.base.domain.LoginInfo;
import com.xmg.p2p.base.service.IAccountService;
import com.xmg.p2p.base.service.IUserinfoService;
import com.xmg.p2p.base.util.BidConst;
import com.xmg.p2p.base.util.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class BorrowIndexController {
    @Autowired
    IAccountService accountService;
    @Autowired
    IUserinfoService userinfoService;
    @RequestMapping("/borrowIndex")
    public String index(Model model){
        LoginInfo current = UserContext.getCurrent();
        if(current==null){
            return "redirect:/borrow.html";
        }else{
            //查询出所有的账户信息
            model.addAttribute("account",accountService.get(UserContext.getCurrent().getId()));
            model.addAttribute("userinfo",userinfoService.get(UserContext.getCurrent().getId()));
            model.addAttribute("creditBorrowScore", BidConst.CREDIT_BORROW_SCORE);
            return "borrow";
        }
    }
}
