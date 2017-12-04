package com.xmg.p2p.business.service.impl;

import com.xmg.p2p.base.page.PageResult;
import com.xmg.p2p.business.domain.PaymentSchedule;
import com.xmg.p2p.business.mapper.PaymentScheduleMapper;
import com.xmg.p2p.business.query.PaymentScheduleQueryObject;
import com.xmg.p2p.business.service.IPaymentScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentScheduleServiceImpl implements IPaymentScheduleService {
    @Autowired
    private PaymentScheduleMapper paymentScheduleMapper;

    @Override
    public int save(PaymentSchedule paymentSchedule) {
        return paymentScheduleMapper.insert(paymentSchedule);
    }

    @Override
    public int update(PaymentSchedule paymentSchedule) {
        return paymentScheduleMapper.updateByPrimaryKey(paymentSchedule);
    }

    @Override
    public PageResult queryPage(PaymentScheduleQueryObject qo) {
        Long count = paymentScheduleMapper.queryPageCount(qo);
        if(count==0){
            return PageResult.empty(qo.getPageSize());
        }
        List<PaymentSchedule> list=paymentScheduleMapper.queryPageData(qo);
        return new PageResult(list,count.intValue(),qo.getCurrentPage(),qo.getPageSize());
    }
}
