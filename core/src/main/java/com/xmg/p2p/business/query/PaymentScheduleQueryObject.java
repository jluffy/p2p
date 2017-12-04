package com.xmg.p2p.business.query;

import com.xmg.p2p.base.query.QueryObject;
import lombok.Getter;
import lombok.Setter;

@Setter@Getter
public class PaymentScheduleQueryObject extends QueryObject {
    private Long borrowUserId=-1L;
}
