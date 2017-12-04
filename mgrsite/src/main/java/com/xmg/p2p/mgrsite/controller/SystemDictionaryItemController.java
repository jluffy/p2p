package com.xmg.p2p.mgrsite.controller;

import com.xmg.p2p.base.domain.SystemDictionaryItem;
import com.xmg.p2p.base.query.SystemDictionaryItemQueryObject;
import com.xmg.p2p.base.service.ISystemDictionaryItemService;
import com.xmg.p2p.base.service.ISystemDictionaryService;
import com.xmg.p2p.base.util.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SystemDictionaryItemController {
    @Autowired
    ISystemDictionaryItemService systemDictionaryItemService;
    @Autowired
    ISystemDictionaryService systemDictionaryService;

    @RequestMapping("/systemDictionaryItem_list")
    public String index(Model model, @ModelAttribute("qo") SystemDictionaryItemQueryObject qo) {
        model.addAttribute("pageResult", systemDictionaryItemService.queryPage(qo));
        model.addAttribute("systemDictionaryGroups", systemDictionaryService.selectAll());
        return "systemdic/systemDictionaryItem_list";
    }

    @RequestMapping("/systemDictionaryItem_update")
    @ResponseBody
    public AjaxResult savaOrUpdate(SystemDictionaryItem systemDictionaryItem) {
        AjaxResult result = null;
        try {
            systemDictionaryItemService.saveOrupdate(systemDictionaryItem);
            result=new AjaxResult();
        } catch (Exception e) {
            result=new AjaxResult(false,"数据明细保存失败");
            e.printStackTrace();
        }
        return  result;
    }
}
