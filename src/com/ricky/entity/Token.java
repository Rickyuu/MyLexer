package com.ricky.entity;
/**
 * 	 ����Token����
 *   @author Ricky Liang   
 *   @time   2012-11-10 ����2:59:23   
 */
public class Token {
	/**
	 * ������ʽ
	 * Token������
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
