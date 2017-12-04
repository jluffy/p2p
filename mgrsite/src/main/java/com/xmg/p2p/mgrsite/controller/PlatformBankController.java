package com.xmg.p2p.mgrsite.controller;

import com.xmg.p2p.business.domain.PlatformBankInfo;
import com.xmg.p2p.business.query.PlatformBankInfoQueryObject;
import com.xmg.p2p.business.service.IPlatformBankInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PlatformBankController {
    @Autowired
    private IPlatformBankInfoService platformBankInfoService;
    @RequestMapping("/companyBank_list")
    public  String index(Model model, @ModelAttribute("qo") PlatformBankInfoQueryObject qo){
        model.addAttribute("pageResult",platformBankInfoService.queryForPage(qo));
        return "platformbankinfo/list";
    }
    @RequestMapping("/companyBank_update")
    public  String SaveOrUpdate(PlatformBankInfo platformBankInfo){
        platformBankInfoService.saveOrUpdate(platformBankInfo);
        return "redirect:/companyBank_list";
    }
}
