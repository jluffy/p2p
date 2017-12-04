package com.xmg.p2p.base.service;

import com.xmg.p2p.base.domain.SystemDictionaryItem;
import com.xmg.p2p.base.page.PageResult;
import com.xmg.p2p.base.query.SystemDictionaryItemQueryObject;

import java.util.List;

public interface ISystemDictionaryItemService {
    PageResult queryPage(SystemDictionaryItemQueryObject qo);
    void save(SystemDictionaryItem systemDictionaryItem);
    void update( SystemDictionaryItem systemDictionaryItem);
    void saveOrupdate(SystemDictionaryItem systemDictionaryItem);
    List<SystemDictionaryItem> queryListBySn(String marriage);
}
