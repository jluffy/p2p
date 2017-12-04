package com.xmg.p2p.base.domain;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter@Setter
public class BaseDomain implements Serializable {//实现序列化接口 数据多时可以实在往硬盘输出
    protected  Long id;//专门给子类使用的
}
