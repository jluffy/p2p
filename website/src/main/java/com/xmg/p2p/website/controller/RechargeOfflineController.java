package com.xmg.p2p.website.controller;

import com.xmg.p2p.base.util.AjaxResult;
import com.xmg.p2p.business.domain.RechargeOffline;
import com.xmg.p2p.business.service.IPlatformBankInfoService;
import com.xmg.p2p.business.service.IRechargeOfflineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class RechargeOfflineController {
    @Autowired
    private IRechargeOfflineService rechargeOfflineService;
    @Autowired
    private IPlatformBankInfoService platformBankInfoService;

    @RequestMapping("/recharge")
    public String index(Model model) {
        model.addAttribute("banks", platformBankInfoService.selectAll());
        return "recharge";
    }

    @RequestMapping("/recharge_save")
    @ResponseBody
    public AjaxResult rechargeSave(RechargeOffline rechargeOffline) {
        AjaxResult result=null;
        try {
            rechargeOfflineService.apply(rechargeOffline);
            result=new AjaxResult();

        } catch (Exception e) {
            result=new AjaxResult(false,"充值失败");
            e.printStackTrace();
        }
        return  result;
    }
}
