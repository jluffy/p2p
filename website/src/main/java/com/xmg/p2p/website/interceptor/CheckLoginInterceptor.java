package com.xmg.p2p.website.interceptor;

import com.xmg.p2p.base.util.UserContext;
import com.xmg.p2p.website.util.RequireLogin;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CheckLoginInterceptor extends HandlerInterceptorAdapter{
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
      if (handler instanceof HandlerMethod){
          HandlerMethod method= (HandlerMethod) handler;
          if (method.getMethodAnnotation(RequireLogin.class)!=null){
             if(UserContext.getCurrent()==null){
                 response.sendRedirect("/login.html");
                 return  false;
             }
          }
      }
        return true;
    }
}
