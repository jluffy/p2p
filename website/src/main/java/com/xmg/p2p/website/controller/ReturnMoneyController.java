package com.xmg.p2p.website.controller;

import com.xmg.p2p.base.service.IAccountService;
import com.xmg.p2p.base.util.UserContext;
import com.xmg.p2p.business.query.PaymentScheduleQueryObject;
import com.xmg.p2p.business.service.IPaymentScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ReturnMoneyController {
    @Autowired
    private IPaymentScheduleService paymentScheduleService;
    @Autowired
    private IAccountService accountService;
    @RequestMapping("/borrowBidReturn_list")
    public String borrowBidReturn(Model model, @ModelAttribute("qo") PaymentScheduleQueryObject qo){
        //根据当前用户查询自己的还款明细
        qo.setBorrowUserId(UserContext.getCurrent().getId());
        //查询出账户信息
        model.addAttribute("account",accountService.get(UserContext.getCurrent().getId()));
        model.addAttribute("pageResult", paymentScheduleService.queryPage(qo) );
        return "returnmoney_list";
    }
}
