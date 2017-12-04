package com.xmg.p2p.mgrsite.controller;

import com.xmg.p2p.base.domain.LoginInfo;
import com.xmg.p2p.base.query.VideoAuthQueryObject;
import com.xmg.p2p.base.service.ILoginInfoService;
import com.xmg.p2p.base.service.IVideoAuthService;
import com.xmg.p2p.base.util.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class VideoAuthController {
    @Autowired
    IVideoAuthService videoAuthService;
    @Autowired
    ILoginInfoService loginInfoService;
    @RequestMapping("/vedioAuth")
    public String vedioAuth(Model model, @ModelAttribute("qo") VideoAuthQueryObject qo){
        model.addAttribute("pageResult",videoAuthService.queryPage(qo));
        return  "vedioAuth/list";
    }
    @ResponseBody
    @RequestMapping("/vedioAuth_autocomplate")
     public List<LoginInfo> autoComplate(String keyword){
        List<LoginInfo> result=loginInfoService.autoComplate(keyword);
        return result;
    }
    @RequestMapping("/vedioAuth_audit")
    @ResponseBody
    public AjaxResult videoAudit(Long loginInfoValue,String remark,int state){
        AjaxResult result = null;
        try {
            videoAuthService.audit(loginInfoValue,remark, state);
            result=new AjaxResult();
        } catch (Exception e) {
            result=new AjaxResult(false,e.getMessage());
            e.printStackTrace();
        }
        return  result;
    }
}
