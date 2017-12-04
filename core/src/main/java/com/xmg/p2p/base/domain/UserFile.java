package com.xmg.p2p.base.domain;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter@Setter
public class UserFile extends  BaseAuthDomain{
    private SystemDictionaryItem fileType;
    private  String image;
    private int score;
    public String getJsonString(){
       //注册审核按钮
        Map<String,Object> param=new HashMap<String,Object>();
        param.put("id",id);
        param.put("applier",applier.getUsername());
        param.put("fileType",fileType.getTitle());
        param.put("image",image);
        return JSON.toJSONString(param);
    }
}
