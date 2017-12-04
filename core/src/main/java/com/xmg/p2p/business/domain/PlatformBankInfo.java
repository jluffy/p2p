package com.xmg.p2p.business.domain;

import com.alibaba.fastjson.JSON;
import com.xmg.p2p.base.domain.BaseDomain;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter@Setter
public class PlatformBankInfo extends BaseDomain {
    private String bankName;    //银行名称
    private String accountNumber;  //银行账号
    private String bankForkName;    //支行名称
    private String accountName; //开户人姓名
    public  String getJsonString(){
        Map<String,Object> param= new HashMap<String,Object>();
        param.put("id",id);
        param.put("bankName",bankName);
        param.put("accountName",accountName);
        param.put("accountNumber",accountNumber);
        param.put("bankForkName",bankForkName);
        return JSON.toJSONString(param);
    }
}