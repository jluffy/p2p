package com.xmg.p2p.base.util;

import com.xmg.p2p.base.domain.LoginInfo;
import com.xmg.p2p.base.vo.VerifyCodeVo;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class UserContext {
    public static String USER_IN_SESSION="logininfo";
    public static HttpServletRequest getRequest(){
        HttpServletRequest request= ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        return request;
    }
    public static void setCurrent(LoginInfo loginInfo){
        //getRequest().getSession().setAttribute("USER_IN_SESSION",loginInfo);//大错特错
        getRequest().getSession().setAttribute(USER_IN_SESSION,loginInfo);
    }
    public static LoginInfo getCurrent(){
        return (LoginInfo) getRequest().getSession().getAttribute(USER_IN_SESSION);
    }

    public static String getIp() {
        return  getRequest().getRemoteAddr();//HttpServletRequest对象可以拿到请求的ip
    }

    public static void setVerifyCode(VerifyCodeVo vo) {
        getRequest().getSession().setAttribute("VERIFYCODE_IN_SESSION", vo);
    }
    public static VerifyCodeVo getVerifyCodeVo(){
        return (VerifyCodeVo) getRequest().getSession().getAttribute("VERIFYCODE_IN_SESSION");
    }
}
