package com.xmg.p2p.mgrsite.controller;

import com.xmg.p2p.base.util.AjaxResult;
import com.xmg.p2p.business.query.RechargeOfflineQueryObject;
import com.xmg.p2p.business.service.IPlatformBankInfoService;
import com.xmg.p2p.business.service.IRechargeOfflineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 线下充值后台审核
 */
@Controller
public class RechargeOfflineController {
    @Autowired
    private IRechargeOfflineService rechargeOfflineService;
    @Autowired
    private IPlatformBankInfoService platformBankInfoService;
    @RequestMapping("/rechargeOffline")
    public String index(Model model, @ModelAttribute("qo") RechargeOfflineQueryObject qo){
        model.addAttribute("pageResult",rechargeOfflineService.queryPage(qo));
        model.addAttribute("banks",platformBankInfoService.selectAll());
        return "rechargeoffline/list";
    }
    @RequestMapping("/rechargeOffline_audit")
    @ResponseBody
    public AjaxResult rechargeOfflineAudit(Long id, String remark, int state){
        AjaxResult result=null;
        try {
        rechargeOfflineService.audit(id,remark,state);
            result=new AjaxResult();
        } catch (Exception e) {
            result= new AjaxResult(false,"审核失败");
            e.printStackTrace();
        }
        return result;
    }
}
