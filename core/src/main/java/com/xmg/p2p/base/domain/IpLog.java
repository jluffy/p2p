package com.xmg.p2p.base.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter@Setter
public class IpLog extends BaseDomain{
    public static final int LOGIN_SUCCESS=0;
    public static final int LOGIN_FAILED = 1;
    private String ip;
    private String username;
    private Date loginTime;
    private int state;
    private int userType;//管理员或者普通用户

    public String getDisplay(){
        return state==LOGIN_FAILED?"登录失败":"登录成功";
    }
    public String getUserDisplay(){
        return userType==LoginInfo.USERTYPE_MANAGER?"管理员":"普通用户";
    }
}
