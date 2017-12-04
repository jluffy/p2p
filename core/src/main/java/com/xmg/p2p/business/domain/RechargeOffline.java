package com.xmg.p2p.business.domain;

import com.alibaba.fastjson.JSON;
import com.xmg.p2p.base.domain.BaseAuthDomain;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Getter@Setter
public class RechargeOffline extends BaseAuthDomain {
    private PlatformBankInfo bankInfo;//
    private String tradeCode;//交易号
    private BigDecimal amount;//充值金额
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date tradeTime;//充值时间
    private String note;//充值说明
    //按钮的注册
    public String getJsonString() {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("id", id);
        param.put("username", this.applier.getUsername());
        param.put("tradeCode", tradeCode);
        param.put("amount", amount);
        param.put("tradeTime", tradeTime);
        return JSON.toJSONString(param);
    }
}
