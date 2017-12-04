package com.xmg.p2p.business.domain;

import com.alibaba.fastjson.JSON;
import com.xmg.p2p.base.domain.BaseDomain;
import com.xmg.p2p.base.domain.LoginInfo;
import com.xmg.p2p.base.util.BidConst;
import com.xmg.p2p.business.util.CalculatetUtil;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 借款对象
 */
@Getter
@Setter
public class BidRequest extends BaseDomain {
    private int version;// 版本号
    private int returnType;// 还款类型(等额本息)
    private int bidRequestType;// 借款类型(信用标)
    private int bidRequestState;// 借款状态
    private BigDecimal bidRequestAmount;// 借款总金额
    private BigDecimal currentRate;// 年化利率
    private BigDecimal minBidAmount;// 最小投標金额
    private int monthes2Return;// 还款月数
    private int bidCount = 0;// 已投标次数(冗余)
    private BigDecimal totalRewardAmount;// 总回报金额(总利息)
    private BigDecimal currentSum = BidConst.ZERO;// 当前已投标总金额
    private String title;// 借款标题
    private String description;// 借款描述
    private String note;// 风控意见
    private Date disableDate;// 招标截止日期
    private int disableDays;// 招标天数
    private LoginInfo createUser;// 借款人
    private Date applyTime;// 申请时间
    private Date publishTime;// 发标时间
    private List<Bid> bids;// 针对该借款的投标

    // bidRequestStateDisplay
    public String getBidRequestStateDisplay() {
        switch (bidRequestState) {
            case BidConst.BIDREQUEST_STATE_BIDDING_OVERDUE:
                return "流标";
            case BidConst.BIDREQUEST_STATE_APPROVE_PENDING_1:
                return "满标一审";
            case BidConst.BIDREQUEST_STATE_APPROVE_PENDING_2:
                return "满标二审";
            default:
                return "";
        }
    }

    public String getReturnTypeDisplay() {
        return this.returnType == BidConst.RETURN_TYPE_MONTH_INTEREST ? "按月分期" : "按月到期";
    }
    public BigDecimal getRemainAmount(){//获取剩余额度
        return this.bidRequestAmount.subtract(currentSum);
    }
    //计算投标进度
    public BigDecimal getPresent(){
        return  currentSum.multiply(CalculatetUtil.ONE_HUNDRED).divide(bidRequestAmount,2, RoundingMode.HALF_UP);
    }
    public String getBidRequestTypeDisplay(){
        return this.bidRequestType==BidConst.BIDREQUEST_TYPE_NORMAL?"信":"体";
    }

    //按钮的注册
    public String getJsonString() {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("id", id);
        param.put("username", this.createUser.getUsername());
        param.put("title", title);
        param.put("bidRequestAmount", bidRequestAmount);
        param.put("currentRate", currentRate);
        param.put("monthes2Return", monthes2Return);
        param.put("returnType",returnType);
        param.put("totalRewardAmount", getTotalRewardAmount());
        return JSON.toJSONString(param);
    }


}
