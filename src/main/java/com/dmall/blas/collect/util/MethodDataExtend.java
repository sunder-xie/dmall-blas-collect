/**
 * 
 */
package com.dmall.blas.collect.util;

import com.alibaba.fastjson.JSON;
import com.dmall.monitor.sdk.method.MethodInfo;

/**
 * @author xiaoxin
 *
 */
public class MethodDataExtend extends MethodInfo{
	public MethodDataExtend(String projectCode, String appCode,String key, Long time,int success){
		super(projectCode, appCode,key);
		this.setExtime(time);
		this.setSuccess(success);
	}
	
	@Override
	public void completed() {
		
	}
	public static void main(String[] args) {
		MethodDataExtend data = new MethodDataExtend("dmc","agae","test", 10L,1);
		System.out.println(JSON.toJSONString(data));
	}
}
