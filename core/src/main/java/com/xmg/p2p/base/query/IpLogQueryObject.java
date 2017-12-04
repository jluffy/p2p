package com.xmg.p2p.base.query;

import com.xmg.p2p.base.util.DateUtils;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Setter
@Getter
public class IpLogQueryObject extends QueryObject {
    private Date beginDate;
    private Date endDate;
    private String currentUser;
    private  int state=-1;
    private int userType=-1;
    private String username;

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

