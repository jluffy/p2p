package com.xmg.p2p.mgrsite.controller;

import com.xmg.p2p.base.query.RealAuthQueryObject;
import com.xmg.p2p.base.service.IRealAuthService;
import com.xmg.p2p.base.util.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/*
* 后台审核实名认证控制器
* */
@Controller
public class RealAuthController {
    @Autowired
    IRealAuthService realAuthService;
    @RequestMapping("/realAuth")
    public String audit(@ModelAttribute("qo") RealAuthQueryObject qo, Model model){
        //查询出审核列表
        model.addAttribute("pageResult",realAuthService.queryPage(qo));
        return "realAuth/list";
    }
    @RequestMapping("/realAuth_audit")
    @ResponseBody
    public AjaxResult audit(Long id, String remark, int state){
        AjaxResult result=null;
        try {
            realAuthService.audit(id,remark,state);
            result=new AjaxResult();
        } catch (Exception e) {
            result=new AjaxResult(false,"审核失败");
            e.printStackTrace();
        }
        return  result;
    }
}
