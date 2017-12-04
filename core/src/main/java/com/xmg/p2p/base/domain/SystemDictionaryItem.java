package com.xmg.p2p.base.domain;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter@Setter
public class SystemDictionaryItem extends BaseDomain{
    //只是用到了他们的Id,所以不用写他们得关联对象
    private Long parentId;//分类ID
    private String title;//名称
    private int sequence;//顺序
    public String getJsonString(){
        Map<String,Object> param=new HashMap<String,Object>();
        param.put("id",id);
        param.put("parentId",parentId);
        param.put("title",title);
        param.put("sequence",sequence);
        return  JSON.toJSONString(param);
    }

}
