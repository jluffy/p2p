package com.xmg.p2p.mgrsite.controller;

import com.xmg.p2p.base.domain.SystemDictionary;
import com.xmg.p2p.base.query.SystemDictionaryQueryObject;
import com.xmg.p2p.base.service.ISystemDictionaryService;
import com.xmg.p2p.base.util.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SystemDictionaryController {
    @Autowired
    ISystemDictionaryService systemDictionaryService;
    @RequestMapping("/systemDictionary_list")
    public String systemDictionary(@ModelAttribute("qo") SystemDictionaryQueryObject qo, Model model){
        model.addAttribute("pageResult",systemDictionaryService.queryPage(qo));
        return "systemdic/systemDictionary_list";
    }
    @RequestMapping("/systemDictionary_update")
    @ResponseBody
    public AjaxResult updata(SystemDictionary dictionary){
        AjaxResult result=null;
        try {
            systemDictionaryService.saveOrUpdate(dictionary);
            result=new AjaxResult();
        } catch (Exception e) {
            result= new AjaxResult(false,"数据字典数据存储失败");
            e.printStackTrace();
        }
            return result;
    }

}
