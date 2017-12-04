package com.xmg.p2p.website.controller;

import com.xmg.p2p.base.page.PageResult;
import com.xmg.p2p.base.query.IpLogQueryObject;
import com.xmg.p2p.base.service.IIpLogService;
import com.xmg.p2p.base.util.UserContext;
import com.xmg.p2p.website.util.RequireLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IpLogController {
    @Autowired
    IIpLogService ipLogService;

    @RequireLogin
    @RequestMapping("/ipLog")
    public String ipLog(Model model, @ModelAttribute("qo") IpLogQueryObject qo) {
        qo.setCurrentUser(UserContext.getCurrent().getUsername());
        PageResult pageResult = ipLogService.queryPage(qo);
        model.addAttribute("pageResult", pageResult);
        return "iplog_list";
    }
}
