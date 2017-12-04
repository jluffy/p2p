package com.xmg.p2p.business.service.impl;

import com.xmg.p2p.business.domain.PaymentScheduleDetail;
import com.xmg.p2p.business.mapper.PaymentScheduleDetailMapper;
import com.xmg.p2p.business.service.IPaymentScheduleDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentScheduleDetailServiceImpl implements IPaymentScheduleDetailService {
	@Autowired
	private PaymentScheduleDetailMapper paymentScheduleDetailMapper;

	@Override
	public int save(PaymentScheduleDetail paymentScheduleDetail) {
		return paymentScheduleDetailMapper.insert(paymentScheduleDetail);
	}

	@Override
	public int update(PaymentScheduleDetail paymentScheduleDetail) {
		return paymentScheduleDetailMapper.updateByPrimaryKey(paymentScheduleDetail);
	}
}
