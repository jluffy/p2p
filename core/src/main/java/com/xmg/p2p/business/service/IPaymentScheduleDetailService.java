package com.xmg.p2p.business.service;

import com.xmg.p2p.business.domain.PaymentScheduleDetail;

public interface IPaymentScheduleDetailService {
    int save(PaymentScheduleDetail paymentScheduleDetail);
    int update(PaymentScheduleDetail paymentScheduleDetail);
}
