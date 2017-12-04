package com.xmg.p2p.business.service.impl;

import com.xmg.p2p.business.domain.BidRequestAuditHistory;
import com.xmg.p2p.business.mapper.BidRequestAuditHistoryMapper;
import com.xmg.p2p.business.service.IBidRequestAuditHistoryService;
import com.xmg.p2p.business.service.IBidRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BidRequestAuditHistoryServiceImpl implements IBidRequestAuditHistoryService {
	@Autowired
	private BidRequestAuditHistoryMapper bidRequestAuditHistoryMapper;
	@Autowired
	private IBidRequestService bidRequestService;

	@Override
	public void save(BidRequestAuditHistory bidRequestAuditHistory) {
		bidRequestAuditHistoryMapper.insert(bidRequestAuditHistory);
	}

	@Override
	public List<BidRequestAuditHistory> queryListByRequest(Long id) {
		return bidRequestAuditHistoryMapper.queryListByRequest(id);

	}

}
