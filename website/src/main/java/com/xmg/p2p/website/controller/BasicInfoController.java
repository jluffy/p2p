package com.xmg.p2p.website.controller;

import com.xmg.p2p.base.domain.Userinfo;
import com.xmg.p2p.base.service.ISystemDictionaryItemService;
import com.xmg.p2p.base.service.IUserinfoService;
import com.xmg.p2p.base.util.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class BasicInfoController {
    @Autowired
    ISystemDictionaryItemService systemDictionaryItemService;
    @Autowired
    IUserinfoService userinfoService;

    @RequestMapping("/basicInfo")
    public String index(Model model) {
        model.addAttribute("userinfo", userinfoService.getCurrent());
        model.addAttribute("marriages", systemDictionaryItemService.queryListBySn("marriage"));
        model.addAttribute("incomeGrades", systemDictionaryItemService.queryListBySn("incomeGrade"));
        model.addAttribute("kidCounts", systemDictionaryItemService.queryListBySn("kidCount"));
        model.addAttribute("houseConditions", systemDictionaryItemService.queryListBySn("houseCondition"));
        model.addAttribute("educationBackgrounds", systemDictionaryItemService.queryListBySn("educationBackground"));
        return "userInfo";
    }

    //保存基本信息
    @RequestMapping("/basicInfo_save")
    @ResponseBody
    public AjaxResult save(Userinfo userinfo) {
        AjaxResult result = null;
        try {
            userinfoService.basicInfoSave(userinfo);
            result=new AjaxResult();
        } catch (Exception e) {
            result=new AjaxResult();
            e.printStackTrace();
        }
        return result;
    }
}
