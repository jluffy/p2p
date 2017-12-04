package com.xmg.p2p.base.domain;

import com.xmg.p2p.base.util.BitStatesUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter@Setter@ToString
public class Userinfo extends  BaseDomain{
    private int version;//版本号，用作乐观锁
    private long bitState = 0;//用户状态值
    private String realName;//用户实名值（冗余数据）
    private String idNumber;//用户身份证号（冗余数据）
    private String phoneNumber;//用户电话
    private String email;//电子邮箱
    private int score=0;//添加冗余字段 用户的风控资料分数
    private Long realAuthId;//当用户申请实名认证之后,在userinfo中添加该字段 第二次访问时直接取出来据其有无 确定跳转的页面(申请/待审核)
    private SystemDictionaryItem incomeGrade;//收入
    private SystemDictionaryItem marriage;//婚姻情况
    private SystemDictionaryItem kidCount;//子女情况
    private SystemDictionaryItem educationBackground;//学历
    private SystemDictionaryItem houseCondition;//住房条件
    //判断是否绑定手机验证码
    public boolean getIsBindPhone(){
        return BitStatesUtils.hasState(this.bitState,BitStatesUtils.OP_BIND_PHONE);
    }
    //更改手机验证的状态
    public void addState(long state){
       this.bitState= BitStatesUtils.addState(this.bitState,state);
    }

    public void removeState(long state) {//移除某个状态
      this.bitState= BitStatesUtils.removeState(this.bitState,state);
    }
    //判断邮箱是否绑定
    public boolean getIsBindEmail(){
        return BitStatesUtils.hasState(this.bitState,BitStatesUtils.OP_BIND_EMAIL);
    }
    //判断基本信息是否绑定
    public boolean getIsBasicInfo(){
        return BitStatesUtils.hasState(this.bitState,BitStatesUtils.OP_BASIC_INFO);
    }
    //判断身份证是否绑定
    public boolean getIsRealAuth(){

        return BitStatesUtils.hasState(this.bitState,BitStatesUtils.OP_REAL_AUTH);
    }
    //判断是否绑定实名认真
    public boolean getIsVedioAuth(){
        return BitStatesUtils.hasState(this.bitState,BitStatesUtils.OP_VEDIO_AUTH);
    }
    public boolean getHasRequestProcess(){//[判断是否含有申请借款的状态
        return BitStatesUtils.hasState(BitStatesUtils.HAS_REQUEST_PROCESS,this.bitState);
    }

}
