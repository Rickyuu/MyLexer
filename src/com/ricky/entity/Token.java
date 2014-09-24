package com.ricky.entity;
/**
 * 	 描述Token的类
 *   @author Ricky Liang   
 *   @time   2012-11-10 下午2:59:23   
 */
public class Token {
	/**
	 * 正则表达式
	 * Token的名称
	 */
	private String regularExpression;
	private String tokenName;
	
	public Token(String regularExpression, String tokenName){
		this.regularExpression = regularExpression;
		this.tokenName = tokenName;
	}
	
	public String getRegularExpression() {
		return regularExpression;
	}

	public void setRegularExpression(String regularExpression) {
		this.regularExpression = regularExpression;
	}

	public String getTokenName() {
		return tokenName;
	}

	public void setTokenName(String tokenName) {
		this.tokenName = tokenName;
	}
	
}
