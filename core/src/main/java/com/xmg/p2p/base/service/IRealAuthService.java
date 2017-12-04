package com.xmg.p2p.base.service;

import com.xmg.p2p.base.domain.RealAuth;
import com.xmg.p2p.base.page.PageResult;
import com.xmg.p2p.base.query.RealAuthQueryObject;

public interface IRealAuthService {
    RealAuth get(Long id);

    void apply(RealAuth realAuth);

    PageResult queryPage(RealAuthQueryObject qo);

    void audit(Long id, String remark, int state);
}
