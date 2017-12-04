package com.xmg.p2p.business.service;

import com.xmg.p2p.business.domain.ExpAccount;
import lombok.Setter;
import org.apache.commons.lang3.time.DateUtils;

import java.math.BigDecimal;
import java.util.Date;

public interface IExpAccountService {
    void save(ExpAccount expAccount);
    void update(ExpAccount expAccount);

    /**
     *
     * @param id 发放体验金的id
     * @param lastTime 有效期
     * @param registerGrantExpmoney 发放的金额
     * @param expmoneyTypeRegister 发放的类型
     */
    void grantExpMoney(Long id, LastTime lastTime, BigDecimal registerGrantExpmoney, int expmoneyTypeRegister);

    /**
     * 获取一个expaccount账户
     * @param id
     * @return
     */
    ExpAccount get(Long id);

    /**
     * 有效期
     */
    @Setter
    class LastTime{
        private int amount;
        private LastTimeUnit unit;
        public LastTime(int amount,LastTimeUnit unit){
            super();
            this.amount=amount;
            this.unit=unit;

        }

        /**
         * 获取一个时间
         * @param date
         * @return
         */
        public Date getReturnTime(Date date) {
            switch(this.unit){
                case DAY:return DateUtils.addDays(date,this.amount);
                case MONTH:return DateUtils.addMonths(date,this.amount);
                case YEAR: return DateUtils.addYears(date,this.amount);
                default:return date;
            }
        }
    }

    /**
     * 持续时间单位
     */
    enum LastTimeUnit{
        DAY,MONTH,YEAR
    }
}
