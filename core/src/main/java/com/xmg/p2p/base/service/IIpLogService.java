package com.xmg.p2p.base.service;

import com.xmg.p2p.base.domain.IpLog;
import com.xmg.p2p.base.page.PageResult;
import com.xmg.p2p.base.query.IpLogQueryObject;

public interface IIpLogService {
    int insert(IpLog record);

   PageResult queryPage(IpLogQueryObject qo);
}
