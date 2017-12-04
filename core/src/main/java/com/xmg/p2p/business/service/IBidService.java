package com.xmg.p2p.business.service;

import com.xmg.p2p.business.domain.Bid;

public interface IBidService {
    void save(Bid bid);

    void updateState( Long bidRequestId, int state);

}
