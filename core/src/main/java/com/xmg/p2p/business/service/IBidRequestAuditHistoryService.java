package com.xmg.p2p.business.service;

import com.xmg.p2p.business.domain.BidRequestAuditHistory;

import java.util.List;

public interface IBidRequestAuditHistoryService {
    void save(BidRequestAuditHistory bidRequestAuditHistory);

    List<BidRequestAuditHistory> queryListByRequest(Long id);
}
