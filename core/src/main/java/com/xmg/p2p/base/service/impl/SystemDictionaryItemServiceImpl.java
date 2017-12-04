package com.xmg.p2p.base.service.impl;

import com.xmg.p2p.base.domain.SystemDictionaryItem;
import com.xmg.p2p.base.mapper.SystemDictionaryItemMapper;
import com.xmg.p2p.base.page.PageResult;
import com.xmg.p2p.base.query.SystemDictionaryItemQueryObject;
import com.xmg.p2p.base.service.ISystemDictionaryItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SystemDictionaryItemServiceImpl implements ISystemDictionaryItemService {
    @Autowired
    private SystemDictionaryItemMapper systemDictionaryItemMapper;

    @Override
    public PageResult queryPage(SystemDictionaryItemQueryObject qo) {
        Long count = systemDictionaryItemMapper.queryPageCount(qo);
        if (count <= 0) {
            return PageResult.empty(qo.getPageSize());
        }
        List<SystemDictionaryItem> listData = systemDictionaryItemMapper.queryPageData(qo);
        return new PageResult(listData, count.intValue(), qo.getCurrentPage(), qo.getPageSize());
    }

    @Override
    public void save(SystemDictionaryItem systemDictionaryItem) {
        systemDictionaryItemMapper.insert(systemDictionaryItem);
    }

    @Override
    public void update(SystemDictionaryItem systemDictionaryItem) {
        systemDictionaryItemMapper.updateByPrimaryKey(systemDictionaryItem);
    }

    @Override
    public void saveOrupdate(SystemDictionaryItem systemDictionaryItem) {
        if (systemDictionaryItem.getId() == null) {
            this.save(systemDictionaryItem);
        } else {
            this.update(systemDictionaryItem);
        }
    }

    @Override
    public List<SystemDictionaryItem> queryListBySn(String string) {
        return  systemDictionaryItemMapper.queryListBySn(string);
    }
}
