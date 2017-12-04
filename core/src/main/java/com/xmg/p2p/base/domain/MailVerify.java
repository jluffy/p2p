package com.xmg.p2p.base.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class MailVerify extends BaseDomain {
    private String uuid;//随机数
    private Date sendTime;
    private String email;
    private Long userInfoId;
}
