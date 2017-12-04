package com.xmg.p2p.business.service;

import com.xmg.p2p.base.page.PageResult;
import com.xmg.p2p.business.domain.PlatformBankInfo;
import com.xmg.p2p.business.query.PlatformBankInfoQueryObject;

import java.util.List;

public interface IPlatformBankInfoService {
    int save(PlatformBankInfo record);
    int update(PlatformBankInfo record);

    PageResult queryForPage(PlatformBankInfoQueryObject qo);

    void saveOrUpdate(PlatformBankInfo platformBankInfo);

    List<PlatformBankInfo> selectAll();
}
