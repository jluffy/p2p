package com.xmg.p2p.business.query;

import com.xmg.p2p.base.query.QueryObject;
import com.xmg.p2p.base.util.DateUtils;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.StringUtils;

import java.util.Date;

@Setter@Getter
public class BidRequestQueryObject extends QueryObject {
    private long bidRequestState=-1;
    private int[] states;//存放投标中  还款中 已还清数据
    private String orderByType;//排序操作
    public String getOrderByType(){
        return StringUtils.isEmpty(orderByType)?null: orderByType;
    }

    private Date beginDate;
    private Date endDate;

    private int bidRequestType=-1;//发表的类型

    public Date getBeginDate() {
        return DateUtils.getStartDate(beginDate);
    }
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getEndDate() {
        return DateUtils.getEndDate(endDate);
    }

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }


}
