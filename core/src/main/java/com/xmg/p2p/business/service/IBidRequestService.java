package com.xmg.p2p.business.service;
import com.xmg.p2p.base.domain.LoginInfo;
import com.xmg.p2p.base.domain.Userinfo;
import com.xmg.p2p.base.page.PageResult;
import com.xmg.p2p.business.domain.BidRequest;
import com.xmg.p2p.business.query.BidRequestQueryObject;

import java.math.BigDecimal;
import java.util.List;

public interface IBidRequestService {
    boolean canApplyBorrow(Userinfo userinfo);

    /*
    * 用户申请借款
    * */
    void borrowApply(BidRequest bidRequest);
    int save(BidRequest bidRequest);
    int update(BidRequest bidRequest);
    BidRequest get(Long id);

    PageResult queryPage(BidRequestQueryObject qo);
    void borrowAudit(Long id, String remark, int state);

    List<BidRequest> selectBidRequestsPage(BidRequestQueryObject qo,int type);

    List<BidRequest> queryByBidRequestState(BidRequestQueryObject qo);

    /**
     * 投标操作
     * @param bidRequestId
     * @param amount
     */
    void bid(Long bidRequestId, BigDecimal amount, LoginInfo loginInfo);

    /**
     * 满标一审
     * @param id
     * @param remark
     * @param state
     */
    void borrowAudit1(Long id, String remark, int state);

    /**
     * 满标二审
     * @param id
     * @param remark
     * @param state
     */
    void borrowAudit2(Long id, String remark, int state);

    /**
     * 发布一个体验标
     * @param bidRequest
     */
    void expApply(BidRequest bidRequest);
}
