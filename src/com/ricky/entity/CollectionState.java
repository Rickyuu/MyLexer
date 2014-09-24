package com.ricky.entity;
import java.util.ArrayList;

/**
 *   描述DFA中状态的类
 *   @author Ricky Liang   
 *   @time   2012-11-5 下午10:43:23   
 */
public class CollectionState {
	/**
	 * 包含NFA中的状态集合
	 * DFA状态编号
	 * 是否是终止状态
	 */
	private ArrayList<State> statesInCollection;
	private int collectionStateNo;
	private boolean isTerminalState;
	
	public CollectionState(int collectionStateNo, ArrayList<State> statesInCollection){
		this.collectionStateNo = collectionStateNo;
		this.statesInCollection = statesInCollection;
		this.isTerminalState = false;
	}
	
	/**
	 * 判断状态NFA集合是否相等
	 * @param states
	 * @return
	 */
	public boolean isEqual(ArrayList<State> states){
		if(statesInCollection.size() != states.size()){
			return false;
		}
		for(int i=0; i<states.size(); i++){
			if(!statesInCollection.contains(states.get(i))){
				return false;
			}
		}
		return true;
	}
	
	public ArrayList<State> getStatesInCollection() {
		return statesInCollection;
	}

	public void setStatesInCollection(ArrayList<State> statesInCollection) {
		this.statesInCollection = statesInCollection;
	}

	public int getCollectionStateNo() {
		return collectionStateNo;
	}

	public void setCollectionStateNo(int collectionStateNo) {
		this.collectionStateNo = collectionStateNo;
	}	
	
	public boolean getIsTerminalState(){
		return isTerminalState;
	}
	
	public void setIsTerminalState(boolean isTerminalState){
		this.isTerminalState = isTerminalState;
	}
	
}
