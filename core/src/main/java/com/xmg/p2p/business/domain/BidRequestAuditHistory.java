package com.xmg.p2p.business.domain;

import com.xmg.p2p.base.domain.BaseAuthDomain;
import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class BidRequestAuditHistory extends BaseAuthDomain {
    public static final int PUBLISH_AUDIT = 0;//发标前审核
    public static final int FULL_AUDIT_1 = 1;//满标一审
    public static final int FULL_AUDIT_2 = 2;//满标二审

    private Long bidRequestId;//所属哪一个借款对象
    private int auditType;//审核类型  发表前 满标 满标二审等
    public String getAuditTypeDisplay(){
        switch (auditType){
            case BidRequestAuditHistory.PUBLISH_AUDIT :return "发标前审核";
            case BidRequestAuditHistory.FULL_AUDIT_1 :return "满标一审";
            case BidRequestAuditHistory.FULL_AUDIT_2 :return "满标二审";
            default :return "";
        }
    }
}
