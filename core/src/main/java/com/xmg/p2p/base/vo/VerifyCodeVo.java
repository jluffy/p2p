package com.xmg.p2p.base.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
@Getter@Setter
public class VerifyCodeVo implements Serializable{
    private String phoneNUmber;
    private String verifyCode;
    private Date sendTime;

}
