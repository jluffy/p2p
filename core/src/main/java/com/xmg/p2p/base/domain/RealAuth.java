package com.xmg.p2p.base.domain;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Getter@Setter@ToString
public class RealAuth extends BaseAuthDomain {
    public static final int SEX_MALE=0;//男
    public static final  int SEX_FEMALE=1;//女
    private String realName;//真实名字
    private int sex;//性别
    private String idNumber;//身份证号
    private String bornDate;//出生日期
    private  String address;//身份证地址
    private String image1;//身份证正面照
    private String image2;//身份中背面照


    public String getSexDisplay(){
        return sex==1?"女":"男";
    }

    public String getJsonString(){
        Map<String,Object>  param=new HashMap<String,Object>();
        param.put("username",this.applier.getUsername());
        param.put("id",this.id);
        param.put("realName",this.realName);
        param.put("idNumber",this.idNumber);
        param.put("bornDate",this.bornDate);
        param.put("sex",getSexDisplay());
        param.put("image1",this.image1);
        param.put("image2",this.image2);
        param.put("address",this.address);
        return  JSON.toJSONString(param);
    }

    /**
     * 获取用户真实名字的隐藏字符串，只显示姓氏
     *
     * @param realName
     *            真实名字
     * @return
     */
    public String getAnonymousRealName() {
        if (StringUtils.hasLength(this.realName)) {
            int len = realName.length();
            String replace = "";
            replace += realName.charAt(0);
            for (int i = 1; i < len; i++) {
                replace += "*";
            }
            return replace;
        }
        return realName;
    }

    /**
     * 获取用户身份号码的隐藏字符串
     *
     * @param idNumber
     * @return
     */
    public String getAnonymousIdNumber() {
        if (StringUtils.hasLength(idNumber)) {
            int len = idNumber.length();
            String replace = "";
            for (int i = 0; i < len; i++) {
                if ((i > 5 && i < 10) || (i > len - 5)) {
                    replace += "*";
                } else {
                    replace += idNumber.charAt(i);
                }
            }
            return replace;
        }
        return idNumber;
    }

    /**
     * 获取用户住址的隐藏字符串
     *
     * @param currentAddress
     *            用户住址
     * @return
     */
    public String getAnonymousAddress() {
        if (StringUtils.hasLength(address) && address.length() > 4) {
            String last = address.substring(address.length() - 4,
                    address.length());
            String stars = "";
            for (int i = 0; i < address.length() - 4; i++) {
                stars += "*";
            }
            return stars + last;
        }
        return address;
    }

}
