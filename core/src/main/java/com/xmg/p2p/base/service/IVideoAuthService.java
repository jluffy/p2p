package com.xmg.p2p.base.service;
import com.xmg.p2p.base.page.PageResult;
import com.xmg.p2p.base.query.VideoAuthQueryObject;

public interface IVideoAuthService {

    PageResult queryPage(VideoAuthQueryObject qo);

    void audit(Long loginInfoValue, String remark, int state);
}
