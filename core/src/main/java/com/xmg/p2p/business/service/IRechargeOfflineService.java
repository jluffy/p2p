package com.xmg.p2p.business.service;

import com.xmg.p2p.base.page.PageResult;
import com.xmg.p2p.business.domain.RechargeOffline;
import com.xmg.p2p.business.query.RechargeOfflineQueryObject;

public interface IRechargeOfflineService {
    int insert(RechargeOffline record);
    int updateByPrimaryKey(RechargeOffline record);

    void apply(RechargeOffline rechargeOffline);

    PageResult queryPage(RechargeOfflineQueryObject qo);

    /**
     * 充值审核
     * @param id
     * @param remark
     * @param state
     */
    void audit(Long id, String remark, int state);
}
