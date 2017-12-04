package com.xmg.p2p.website.controller;

import com.xmg.p2p.base.domain.RealAuth;
import com.xmg.p2p.base.domain.Userinfo;
import com.xmg.p2p.base.service.IRealAuthService;
import com.xmg.p2p.base.service.IUserinfoService;
import com.xmg.p2p.website.util.UploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 * 前端的认证控制
 */
@Controller
public class RealAuthController {
    @Value("${uploadPath}")
    private String uploadPath;

    @Autowired
    IUserinfoService userinfoService;
    @Autowired
    IRealAuthService realAuthService;
    @RequestMapping("/realAuth")
    public  String realAauthPage(Model model){//进入申请的界面
        Userinfo userinfo = userinfoService.getCurrent();
        //1根据userinfo中的冗余字段realAuth的值进来后要判断用户是否已经实名认证
        if(userinfo.getIsRealAuth()){
            model.addAttribute("auditing",false);
            //2如果已经认证,获取到userinfo中的realAuthId查询出对应的实名认证对象,放入模型中 跳转到已认证界面
            RealAuth realAuth=realAuthService.get(userinfo.getRealAuthId());
            model.addAttribute("realAuth",realAuth);
            return "realAuth_result";
        }else{
            //3如果没有认证
            if(userinfo.getRealAuthId()!=null){
                //3.1如果userinfo中存在realAuthid
                model.addAttribute("auditing",true);
                return "realAuth_result";
            }else{
                //3.2 如果userinfo中不存在realAuthid
                return "realAuth";
            }
        }
    }
    @RequestMapping("/realAuth_save")
    public  String apply(RealAuth realAuth){
        //完成申请的逻辑
        realAuthService.apply(realAuth);
        return "redirect:/realAuth";//重定向到
    }
    //上传身份证正面
    @RequestMapping("/uploadImage")
    @ResponseBody
    public String upload(MultipartFile image){//image 根据前台确定的
        String imagePath= UploadUtil.upload(image,uploadPath);
        return imagePath;
    }

}

