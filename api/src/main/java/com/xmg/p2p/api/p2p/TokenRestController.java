package com.xmg.p2p.api.p2p;

import com.xmg.p2p.api.p2p.service.ITokenService;
import com.xmg.p2p.base.domain.LoginInfo;
import com.xmg.p2p.base.service.ILoginInfoService;
import com.xmg.p2p.base.util.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RequestMapping(value = "/api/tokens/", produces = "application/json")
@RestController
public class TokenRestController {
    @Autowired
    private ILoginInfoService loginInfoService;
    @Autowired
    ITokenService tokenService;
    /**
     * 创建登录成功的令牌
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public AjaxResult createToken(String username, String password, int UserType) {
        AjaxResult result = null;
        LoginInfo loginInfo = loginInfoService.login(username, password, LoginInfo.USERTYPE_USER);
        if(loginInfo!=null){
            //登录成功
            //使用tokenService 创建一个token
            String token = tokenService.createToken(loginInfo);
            result=new AjaxResult();
            result.setData(token);
            /*
            *  "success": true,
                "msg": null,
                "data": "e97c2062-0823-410c-8a76-afc2f0f76fcf"
            * */
        }else{
            //登录失败
            result=new AjaxResult(false,"用户名或者密码错误");
        }
        return result;
    }
    /**
     * 登出操作
     */
    @RequestMapping(method = RequestMethod.DELETE)
    public AjaxResult deleteToken(HttpServletRequest request){
        //只有在登录状态才能登出
        String token = request.getHeader(ITokenService.TOKEN_IN_HEADER);
        tokenService.destroyToken(token);
        return  new AjaxResult();
    }
}
