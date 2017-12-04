package com.xmg.p2p.business.mapper;

import com.xmg.p2p.base.query.QueryObject;
import com.xmg.p2p.business.domain.PaymentScheduleDetail;

import java.util.List;

public interface PaymentScheduleDetailMapper {
    int insert(PaymentScheduleDetail record);
    PaymentScheduleDetail selectByPrimaryKey(Long id);
    int updateByPrimaryKey(PaymentScheduleDetail record);
	Long queryPageCount(QueryObject qo);
	List<PaymentScheduleDetail> queryPageData(QueryObject qo);
    PaymentScheduleDetail  selectByPaymentScheldulId(Long id);
}