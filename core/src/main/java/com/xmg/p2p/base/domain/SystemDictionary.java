package com.xmg.p2p.base.domain;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * 数据字典分类
 * @author Administrator
 *
 */
@Setter
@Getter
public class SystemDictionary extends BaseDomain{
	private String sn;
	private String title;
	
	/**
	 * 返回当前的json字符串
	 * @return
	 */
	public String getJsonStr(){
		Map<String,Object> json=new HashMap<String,Object>();
		json.put("sn",sn);
		json.put("id",id);//修改父类为protected
		json.put("title",title);
		return JSON.toJSONString(json);
	}
}
