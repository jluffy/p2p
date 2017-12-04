package com.xmg.p2p.api.p2p;

import com.xmg.p2p.base.service.ILoginInfoService;
import com.xmg.p2p.base.util.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RequestMapping(value = "/api/userinfos/",produces = "application/json")
@RestController
public class UserinfoRestController {
    @Autowired
    private ILoginInfoService loginInfoService;

    /**
     * 用户注册的接口
     * @param username
     * @param password
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public AjaxResult register(String username, String password){
        AjaxResult result=null;
        try{
            loginInfoService.register(username,password);
            result=new AjaxResult();
        }catch (Exception e){
            e.printStackTrace();
           result= new AjaxResult(false,e.getMessage());
        }
        return  result;
    }


}
