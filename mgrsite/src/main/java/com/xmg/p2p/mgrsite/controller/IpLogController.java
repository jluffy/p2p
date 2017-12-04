package com.xmg.p2p.mgrsite.controller;

import com.xmg.p2p.base.page.PageResult;
import com.xmg.p2p.base.query.IpLogQueryObject;
import com.xmg.p2p.base.service.IIpLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IpLogController {
    @Autowired
    IIpLogService ipLogService;
    @RequestMapping("/ipLog")
    public String ipLog(@ModelAttribute("qo")IpLogQueryObject qo, Model model){
        //查询出所有的记录
        PageResult pageResult=ipLogService.queryPage(qo);
        model.addAttribute("pageResult",pageResult);
        return "ipLog/list";
    }
}
