package com.xmg.p2p.business.domain;

import com.xmg.p2p.base.domain.BaseDomain;
import com.xmg.p2p.base.domain.LoginInfo;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class Bid extends BaseDomain {
    private BigDecimal actualRate;//借款利率
    private BigDecimal availableAmount;//投标的金额
    private Long bidRequestId;//借款对象Id
    private String bidRequestTitle;//借款对象的标题
    private LoginInfo bidUser;//投标人
    private Date bidTime;//投标时间
    private int bidRequestState;//借款状态
}
