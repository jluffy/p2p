package com.xmg.p2p.business.service;

import com.xmg.p2p.base.page.PageResult;
import com.xmg.p2p.business.domain.PaymentSchedule;
import com.xmg.p2p.business.query.PaymentScheduleQueryObject;

public interface IPaymentScheduleService {
    int save(PaymentSchedule paymentSchedule);
    int  update(PaymentSchedule paymentSchedule);

    PageResult queryPage(PaymentScheduleQueryObject qo);
}
