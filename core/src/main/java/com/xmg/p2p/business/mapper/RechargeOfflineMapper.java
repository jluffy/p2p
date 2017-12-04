package com.xmg.p2p.business.mapper;

import com.xmg.p2p.base.query.QueryObject;
import com.xmg.p2p.business.domain.RechargeOffline;

import java.util.List;

public interface RechargeOfflineMapper {
    int insert(RechargeOffline record);
    RechargeOffline selectByPrimaryKey(Long id);
    int updateByPrimaryKey(RechargeOffline record);
	Long queryPageCount(QueryObject qo);
	List<RechargeOffline> queryPageData(QueryObject qo);

}