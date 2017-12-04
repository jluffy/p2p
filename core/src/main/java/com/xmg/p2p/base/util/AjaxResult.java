package com.xmg.p2p.base.util;

import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class AjaxResult {
    private boolean success=true;
    private String msg;
    private Object data;

    public AjaxResult(String msg) {

        this.msg = msg;
    }

    public AjaxResult( boolean success,String msg) {
        this.msg = msg;
        this.success = success;
    }
    public AjaxResult(){

    }
}
