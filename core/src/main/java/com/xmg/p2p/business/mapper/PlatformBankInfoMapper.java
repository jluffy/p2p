package com.xmg.p2p.business.mapper;

import com.xmg.p2p.base.query.QueryObject;
import com.xmg.p2p.business.domain.PlatformBankInfo;

import java.util.List;

public interface PlatformBankInfoMapper {
    int insert(PlatformBankInfo record);
    PlatformBankInfo selectByPrimaryKey(Long id);
    int updateByPrimaryKey(PlatformBankInfo record);
	Long queryPageCount(QueryObject qo);
	List<PlatformBankInfo> queryPageData(QueryObject qo);

    List<PlatformBankInfo> selectAll();
}