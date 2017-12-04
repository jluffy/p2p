package com.xmg.p2p.base.query;

import lombok.Getter;
import lombok.Setter;
import org.springframework.util.StringUtils;

@Setter@Getter
public class SystemDictionaryQueryObject extends QueryObject {
    private String keyword;
    public String getKeyword(){
        return StringUtils.isEmpty(keyword)?null:keyword;
    }
}
