package com.ricky.entity;
/**
 *   ÃèÊöNFA×´Ì¬µÄÀà
 *   @author Ricky Liang   
 *   @time   2012-11-5 ÏÂÎç6:37:56   
 */
public class State {
	/**
	 * NFA×´Ì¬±àºÅ
	 */
	private int stateNo;
	
	public State(int stateNo){
		this.stateNo = stateNo;
	}
	
	public int getStateNo(){
		return stateNo;
	}
	public void setStateNo(int stateNo){
		this.stateNo = stateNo;
	}

}


